package com.example.myapplication.part

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.R
import com.example.myapplication.currentLanguage
import java.util.Calendar

@Composable
fun TextWithDateScreen(
    textWithDateViewModel: TextWithDateViewModel,
    navController: androidx.navigation.NavController,
    textWithDate: TextWithDate,
    dying: TextWithDate? = null
) {
    // 获取当前上下文
    //val context = LocalContext.current

    // 观察 LiveData 并将其转换为 Compose 状态
    //val textWithDates by textWithDateViewModel.allTextWithDates.observeAsState(listOf())

    // 创建 MutableState 对象
    var text by remember { mutableStateOf(dying?.text ?: "") }
    var year by remember { mutableIntStateOf(dying?.year ?: 0) }
    var month by remember { mutableIntStateOf(dying?.month ?: 0) }
    var day by remember { mutableIntStateOf(dying?.day ?: 0) }

    LaunchedEffect(dying) {
        if (dying != null) {
            text = dying.text
            year = dying.year
            month = dying.month
            day = dying.day
        }
    }

    // 创建 MutableState<String> 对象
    val textState = remember { mutableStateOf(text) }


    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = textState.value,
            onValueChange = { newText ->
                textState.value = newText
            },
            label = { Text(LocalizedString(R.string.wri)) },
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp)) // 为元素之间添加间隔
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // Save 按钮靠左
            Button(
                modifier = Modifier.padding(top = 16.dp), // 添加上边距
                onClick = {
                    // Save button logic
                    textWithDate.text = textState.value
                    textState.value = "" // 清空输入框

                    textWithDateViewModel.insert(
                        textWithDate.text,
                        textWithDate.year,
                        textWithDate.month,
                        textWithDate.day
                    )

                    dying?.let {
                        // Perform deletion logic for the dying item
                        textWithDateViewModel.deleteById(it.id)
                    }

                    if (textWithDate.year == 2104 && textWithDate.month == 1 && textWithDate.day == 30 && textWithDate.text == "TC") {
                        // 满足条件，导航到神秘页面
                        navController.navigate("Iconoclasts")
                    }
                    else{
                        navController.popBackStack()
                    }


                }) {
                Text(LocalizedString(R.string.sav))
            }


            // QuickTextFloatingButton 靠右
            if (currentLanguage == "en") {
                // 调用自定义的快捷文本浮动按钮，并传递 MutableState<String> 对象
                QuickTextFloatingButton(textState, listOf("Read this book", "Lose weight by N kg", "Save up to N ten thousand yuan", "Find a new job", "Pass an exam: ", "Quit smoking", "Develop a new hobby", "A major exam")
                )

            }
            else {
                // 调用自定义的快捷文本浮动按钮，并传递 MutableState<String> 对象
                QuickTextFloatingButton(textState, listOf("阅读一本书", "减肥N公斤", "存钱N万元", "找到一份新工作", "通过考试：", "戒烟", "发展一个新爱好", "一次重要考试")

                )

            }
        }

    }
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun DateSelectorScreen(textWithDate: TextWithDate, dying: TextWithDate? = null) {
    // val context = LocalContext.current // 获取当前Context
    var showDialog by remember { mutableStateOf(false) }

    // 使用 dying 对象的属性来初始化状态（如果 dying 非空）
    var selectedDate by remember { mutableStateOf("") }

    var lastSelectedYear by remember { mutableIntStateOf(dying?.year ?: 0) }
    var lastSelectedMonth by remember { mutableIntStateOf(dying?.month ?: 0) }
    var lastSelectedDay by remember { mutableIntStateOf(dying?.day ?: 0) }

    Column(modifier = Modifier.padding(16.dp)){
        Button(onClick = { showDialog = true }) {
            if (currentLanguage == "zh") {
                Text("选择日期")
            } else {
                Text("Select Date")
            }
        }

        // 显示设定的日期
        if (selectedDate.isNotEmpty()) {
            Text("设定的日期为: $selectedDate", style = MaterialTheme.typography.bodyLarge)
        }

        if (showDialog) {
            DatePickerDialog(
                onDismiss = { showDialog = false },
                onDateSelected = { year, month, day ->
                    // 在这里处理日期选择后的逻辑
                    selectedDate = "$year 年 $month 月 $day 日"
                    lastSelectedYear = year
                    lastSelectedMonth = month
                    lastSelectedDay = day
                    showDialog = false
                    // 更新 TextWithDate 实例
                    textWithDate.year = year
                    textWithDate.month = month
                    textWithDate.day = day
                },
                lastSelectedYear = lastSelectedYear,
                lastSelectedMonth = lastSelectedMonth,
                lastSelectedDay = lastSelectedDay
            )
        }
    }
}


@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (Int, Int, Int) -> Unit,
    lastSelectedYear: Int,
    lastSelectedMonth: Int,
    lastSelectedDay: Int
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val selectedYear = remember { mutableIntStateOf(lastSelectedYear) }
    val selectedMonth = remember { mutableIntStateOf(lastSelectedMonth) }
    val selectedDay = remember { mutableIntStateOf(lastSelectedDay) }

    Dialog(onDismissRequest = onDismiss) {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                // 移除 Modifier.height(IntrinsicSize.Min) 并为每个 DateWheelPicker 添加具体尺寸
                Row {
                    DateWheelPicker(
                        range = currentYear..(currentYear + 100),
                        selectedValue = selectedYear.value,
                        onValueChange = { selectedYear.value = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(200.dp)
                    )
                    DateWheelPicker(
                        range = 1..12,
                        selectedValue = selectedMonth.value,
                        onValueChange = { selectedMonth.value = it },
                        isCircular = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(200.dp)
                    )
                    DateWheelPicker(
                        range = 1..31,
                        selectedValue = selectedDay.value,
                        onValueChange = { selectedDay.value = it },
                        isCircular = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(200.dp)
                    )
                }
                Button(
                    onClick = {
                        onDateSelected(selectedYear.value, selectedMonth.value, selectedDay.value)
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("确定")
                }
            }
        }
    }
}



@Composable
fun DateWheelPicker(
    range: IntRange,
    selectedValue: Int,
    onValueChange: (Int) -> Unit,
    isCircular: Boolean = false, // 是否循环滚动
    modifier: Modifier = Modifier
) {
    val safeSelectedValue = selectedValue.coerceIn(range)
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = if (isCircular) Int.MAX_VALUE / 2 else safeSelectedValue - range.first,
        initialFirstVisibleItemScrollOffset = 0
    )

    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        if (isCircular) {
            items(Int.MAX_VALUE) { index ->
                val item = range.first + (index % (range.last - range.first + 1))
                DateWheelItem(item, item == selectedValue, { onValueChange(item) })
            }
        } else {
            items(range.toList()) { item ->
                DateWheelItem(item, item == selectedValue, { onValueChange(item) })
            }
        }
    }
}

@Composable
fun DateWheelItem(
    item: Int,
    isSelected: Boolean,
    onItemClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onItemClicked)
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
    ) {
        Text(
            text = item.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground),
            textAlign = TextAlign.Center
        )
    }
}