package com.example.myapplication.part

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.R
import com.example.myapplication.currentLanguage
import kotlin.math.roundToInt

@Composable
fun UserInputScreen(
    userInputViewModel: UserInputViewModel,
    navController: androidx.navigation.NavController,
    userInput: UserInput,
    dying: UserInput? = null
) {
    // Store temporary text input state
    // 用于存储临时文本输入的状态
    val context = LocalContext.current
    var text by remember { mutableStateOf(dying?.text ?: "") }

    LaunchedEffect(dying) {
        if (dying != null) {
            text = dying.text
        }
    }

    // Create MutableState<String> object
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
        Spacer(Modifier.height(8.dp)) // Add space between elements
        // 为元素之间添加间隔
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){

            Button(
                modifier = Modifier.padding(top = 16.dp), // Add top padding
                // 添加上边距
                onClick = {
                    userInput.text = textState.value
                    textState.value = "" // Clear the text field
                    // 清空输入框
                    userInput.requestcode = generateUniqueRequestCode()

                    setAlarm(
                        context,
                        userInput.hours,
                        userInput.minutes,
                        userInput.seconds,
                        userInput.alarmTriggerType,
                        convertStringToWeekDays(userInput.weekDaysState),
                        userInput.requestcode
                    )

                    userInputViewModel.insert(userInput)

                    dying?.let {
                        cancelAlarm(context, it.requestcode)
                        userInputViewModel.deleteById(it.id)
                    }

                    navController.popBackStack()
                }) {
                Text(LocalizedString(R.string.sav))
            }

            if (currentLanguage == "en") {
                // Call custom QuickTextFloatingButton and pass MutableState<String> object
                // 调用自定义的快捷文本浮动按钮，并传递 MutableState<String> 对象
                QuickTextFloatingButton(textState, listOf("Wake up", "Buy something", "Exercise time", "Start studying", "Have a meeting", "Pick up/drop off someone", "Pending chores:", "Finish housework", "Review vocabulary", "Play games", "Take medicine", "Have a date"))
            }
            else {
                // Call custom QuickTextFloatingButton and pass MutableState<String> object
                // 调用自定义的快捷文本浮动按钮，并传递 MutableState<String> 对象
                QuickTextFloatingButton(textState, listOf("起床了", "要买什么东西？", "运动时间", "开始学习", "有一场会议", "要去接送某人", "未完成的家务：", "背单词", "玩游戏", "吃药", "有一场约会"))
            }

        }
    }
}



@SuppressLint("UnrememberedMutableState")
@Composable
fun TimerScreenWithDialog(userInput: UserInput, dying : UserInput? = null) {

    // 获取当前Context
    // val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) } // 控制对话框的显示状态

    // 使用 dying 对象的属性来初始化状态（如果 dying 非空）
    // 用于显示设定的时间
    var selectedTime by remember { mutableStateOf("") } // 存储设定的时间
    // 闹钟触发方式，默认为每天
    var alarmTriggerType by remember { mutableStateOf(dying?.alarmTriggerType ?: "每天") } // 存储闹钟触发方式
    val initialWeekDaysState = mutableStateListOf<Boolean>().apply {
        val convertedWeekDays = dying?.weekDaysState?.let { convertStringToWeekDays(it) }
        addAll(convertedWeekDays ?: listOf(false, false, false, false, false, false, false))
    }
    // 自定义触发的勾选状态
    val weekDaysState = remember { initialWeekDaysState } // 存储自定义触发的勾选状态

    var lastSelectedHours by remember { mutableStateOf(dying?.hours ?: 0) } // 存储上次选择的小时
    var lastSelectedMinutes by remember { mutableStateOf(dying?.minutes ?: 0) } // 存储上次选择的分钟
    var lastSelectedSeconds by remember { mutableStateOf(dying?.seconds ?: 0) } // 存储上次选择的秒

    Column(modifier = Modifier.padding(16.dp)){
        Button(onClick = { showDialog = true }) {
            if (currentLanguage == "zh") {
                Text("设置时间") // 显示设置时间按钮
            } else {
                Text("Set Time") // 显示设置时间按钮
            }
        }

        // 显示设定的时间
        if (selectedTime.isNotEmpty()) {
            if (currentLanguage == "zh") {
                Text("设定的时间为: $selectedTime", style = MaterialTheme.typography.bodyLarge) // 显示设定的时间
            } else {
                Text("The set time is: $selectedTime", style = MaterialTheme.typography.bodyLarge) // 显示设定的时间
            }
        }

        // 触发方式的选择
        AlarmTriggerTypeSelection(alarmTriggerType) { selected ->
            alarmTriggerType = selected // 更新触发方式
        }

        // 自定义触发方式的勾选框
        if (alarmTriggerType == "自定义" || alarmTriggerType == "Custom") {
            WeekDaysSelection(weekDaysState) // 显示自定义触发方式的勾选框
        }

        if (showDialog) {
            TimerSetupDialog(
                onDismiss = { showDialog = false },
                onTimeSelected = { hours, minutes, seconds ->
                    // 在这里处理时间选择后的逻辑
                    selectedTime = "$hours 时 $minutes 分 $seconds 秒" // 更新设定的时间
                    lastSelectedHours = hours // 更新上次选择的小时
                    lastSelectedMinutes = minutes // 更新上次选择的分钟
                    lastSelectedSeconds = seconds // 更新上次选择的秒
                    showDialog = false // 关闭对话框
                    //setAlarm(context, hours, minutes, seconds, alarmTriggerType, weekDaysState)
                    //更新
                    userInput.hours = hours
                    userInput.minutes = minutes
                    userInput.seconds = seconds
                    userInput.alarmTriggerType = alarmTriggerType
                    userInput.weekDaysState = convertWeekDaysToString(weekDaysState)
                },
                lastSelectedHours = lastSelectedHours, // 传递上次选择的小时
                lastSelectedMinutes = lastSelectedMinutes, // 传递上次选择的分钟
                lastSelectedSeconds = lastSelectedSeconds // 传递上次选择的秒
            )
        }
    }
}



@Composable
fun AlarmTriggerTypeSelection(selected: String, onSelectionChanged: (String) -> Unit) {
    Column {
        Text(LocalizedString(R.string.tih), style = MaterialTheme.typography.bodyLarge)
        val options = if (currentLanguage == "zh") {
            listOf("每天", "仅一次", "自定义")
        } else {
            listOf("Everyday", "Once", "Custom")
        }

        options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selected == option,
                    onClick = { onSelectionChanged(option) }
                )
                Text(option, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun TimerSetupDialog(onDismiss: () -> Unit, onTimeSelected: (Int, Int, Int) -> Unit, lastSelectedHours: Int, lastSelectedMinutes: Int, lastSelectedSeconds: Int) {
    var hours by remember { mutableStateOf(lastSelectedHours.toFloat()) } // 使用上次选择的小时作为初始值
    var minutes by remember { mutableStateOf(lastSelectedMinutes.toFloat()) } // 使用上次选择的分钟作为初始值
    var seconds by remember { mutableStateOf(lastSelectedSeconds.toFloat()) } // 使用上次选择的秒作为初始值

    // 对话框的其他代码保持不变

    Dialog(onDismissRequest = onDismiss) {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally // 添加对齐方式为居中
            ) {
                // 更醒目的时间显示
                // 独立显示的“当前时间”标题
                Text(
                    "设定时间",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp) // 增加与时间显示的间距
                )

                // 使用Box来创建带有边框的时间显示  12
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(4.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = when {
                            hours.roundToInt() < 12 -> "上午 ${hours.roundToInt()} :${
                                minutes.roundToInt().toString().padStart(2, '0')
                            }:${seconds.roundToInt().toString().padStart(2, '0')}"

                            hours.roundToInt() == 12 -> "中午 ${hours.roundToInt()} :${
                                minutes.roundToInt().toString().padStart(2, '0')
                            }:${seconds.roundToInt().toString().padStart(2, '0')}"

                            hours.roundToInt() > 12 -> "下午 ${(hours.roundToInt() % 12).takeIf { it > 0 } ?: 12} :${
                                minutes.roundToInt().toString().padStart(2, '0')
                            }:${seconds.roundToInt().toString().padStart(2, '0')}"

                            else -> "错误的时间格式"
                        },
                        style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.primary),
                        textAlign = TextAlign.Center
                    )


                }


                // 使用Box来实现带边框的时间显示 24
                Box(
                    contentAlignment = Alignment.Center, // 内容居中
                    modifier = Modifier
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(4.dp)
                        ) // 添加边框
                        .padding(8.dp)
                ) {
                    Text(
                        "${hours.roundToInt()} : ${minutes.roundToInt()} : ${seconds.roundToInt()}",
                        style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.primary),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(16.dp)) // 增加额外的间距


                // 小时滑动条
                Slider(
                    value = hours,
                    onValueChange = { hours = it },
                    valueRange = 0f..23f,
                    steps = 22
                )
                Text("小时: ${hours.roundToInt()}", style = MaterialTheme.typography.bodyLarge)
                // 分钟滑动条
                Slider(
                    value = minutes,
                    onValueChange = { minutes = it },
                    valueRange = 0f..59f,
                    steps = 58
                )
                Text(
                    "分钟: ${minutes.roundToInt()}",
                    style = MaterialTheme.typography.bodyLarge
                )
                // 秒滑动条
                Slider(
                    value = seconds,
                    onValueChange = { seconds = it },
                    valueRange = 0f..59f,
                    steps = 58
                )
                Text("秒: ${seconds.roundToInt()}", style = MaterialTheme.typography.bodyLarge)
                // 确定按钮
                Button(
                    onClick = {
                        onTimeSelected(
                            hours.roundToInt(),
                            minutes.roundToInt(),
                            seconds.roundToInt()
                        )
                        onDismiss()
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("确定", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }

}


@Composable
fun WeekDaysSelection(weekDaysState: MutableList<Boolean>) {
    val weekDays = if (currentLanguage == "zh") {
        listOf("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日")
    } else {
        listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp) // 限制最大高度以确保在内容多时可以滚动
            .verticalScroll(rememberScrollState())
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) // 添加边框以增强视觉效果
            .padding(8.dp) // 添加内间距
    ) {
        Text(
            if (currentLanguage == "zh") "选择重复的天" else "Select repeated days",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp)) // 添加一点间距
        weekDays.forEachIndexed { index, day ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = weekDaysState[index],
                    onCheckedChange = { newState ->
                        weekDaysState[index] = newState
                    }
                )
                Text(day, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(4.dp)) // 在选项之间添加间距
        }
    }
}

// 将布尔值列表转换为字符串
fun convertWeekDaysToString(weekDays: MutableList<Boolean>): String {
    return weekDays.joinToString(separator = ",")
}

// 将字符串转换为布尔值列表
fun convertStringToWeekDays(weekDaysString: String): MutableList<Boolean> {
    return weekDaysString.split(",").map { it.toBoolean() }.toMutableList()
}