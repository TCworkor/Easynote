package com.example.myapplication.part

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.compose.AppTheme
import com.example.myapplication.R
import com.example.myapplication.currentLanguage

@Composable
fun uimain(navController: androidx.navigation.NavController,userInputViewModel: UserInputViewModel,st: String) {
    var currentPage by remember { mutableStateOf(st) }
    val configuration = LocalConfiguration.current

    val context = LocalContext.current

    // 获取当前主题对应的背景图片资源ID
    val backgroundImageResId = when (ThemePreference.getTheme(context)) {
        1 -> R.drawable.nih // 主题1的背景
        2 -> R.drawable.blue // 主题2的背景
        3 -> R.drawable.red // 主题1的背景
        // 其他主题的背景...
        else -> R.drawable.gree // 默认背景
    }
    // 一次性加载背景图片
    // 在 remember 中保存图片资源ID以避免重复加载
    val backgroundImage = painterResource(id = backgroundImageResId)

    // 获取设备的最大宽度和高度
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    val inputs by userInputViewModel.allInputs.observeAsState(initial = listOf())

    AppTheme{
        Box(modifier = Modifier.fillMaxSize()) { // 使用Box作为最外层布局

            // 背景图片
            Image(
                painter = backgroundImage,
                contentDescription = null,
                contentScale = ContentScale.Crop, // 根据需要调整缩放方式
                modifier = Modifier.matchParentSize() // 确保图片填满父布局
            )


            Column(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)) {

                // 主内容区域
                Surface(// 第一份空白，用于展示主要内容
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth(),
                    border = BorderStroke(1.dp, Color.LightGray), // 选择适合的边框颜色和宽度
                    shape = MaterialTheme.shapes.medium // 使用主题中定义的形状，以保持一致性
                ) {


                    // 背景图片
                    Image(
                        painter = painterResource(id = backgroundImageResId),
                        contentDescription = null,
                        contentScale = ContentScale.Crop, // 根据需要调整缩放方式
                    )


                    // 主要内容...
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MiniCalendar() // 小日历组件
                        Spacer(modifier = Modifier.width(16.dp)) // 在组件之间添加16dp的宽度间隔
                        DateBasedImage() // 新的基于日期显示图片的组件
                    }

                }

                //空白
                Spacer(modifier = Modifier.height(10.dp))

                Surface(//正文
                    modifier = Modifier
                        .padding(1.dp)
                        .weight(10f)
                        .width(maxWidth),
                    border = BorderStroke(1.dp, Color.LightGray), // 添加边框
                    shape = MaterialTheme.shapes.medium // 添加圆角
                ) {


                    // 背景图片
                    Image(
                        painter = backgroundImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop, // 根据需要调整缩放方式
                    )



                    // 正文内容...
                    val scrollState = rememberScrollState()

                    Column(modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(8.dp)
                    ) {
                        inputs.forEach { userInput ->
                            SwipeToDeleteItem(
                                userInput = userInput,
                                onDelete = {
                                    //////////working
                                    cancelAlarm(context, userInput.requestcode)

                                    userInputViewModel.deleteById(userInput.id)
                                    //////////working
                                },
                                onEdit = {
                                    // 导航到write页面，并传递UserInput的ID或其他标识
                                    navController.navigate("write/${userInput.id}")
                                    ///当前设置编辑功能，滑动按钮已准备，现在要在这里填充功能函数。
                                }

                            )
                            Spacer(modifier = Modifier.height(8.dp)) // 添加边距
                        }

                    }

                    Box(//悬浮按钮
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth() // 根据需要调整这里，例如使用 maxWidth 来代替 weight
                    ) {
                        FloatingActionButton(
                            onClick = { navController.navigate("write") /* 在这里处理点击事件 */ },
                            modifier = Modifier
                                .align(Alignment.BottomEnd) // 在 Box 中定位到底部右侧
                                .padding(16.dp), // 根据需要添加内边距以调整位置
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Add")
                        }
                    }


                }

                //空白
                Spacer(modifier = Modifier.height(10.dp))

                Surface(//尾巴
                    modifier = Modifier
                        .padding(1.dp)
                        .weight(2f)
                        .width(maxWidth),
                    border = BorderStroke(1.dp, Color.LightGray), // 选择适合的边框颜色和宽度
                    shape = MaterialTheme.shapes.medium // 使用主题中定义的形状，以保持一致性
                ) {




                    // 背景图片
                    Image(
                        painter = backgroundImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop, // 根据需要调整缩放方式
                    )

                    Row(
                        modifier = Modifier.width(maxWidth),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // 左边偏移
                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = { navController.navigate("mainleft")
                                if (currentPage != "日程") {
                                    currentPage = "日程"
                                    // 跳转到日程页面的逻辑
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentPage == "日程") Color.Blue else Color.Gray
                            ),
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(maxHeight / 60)
                        ) {
                            Text(text = LocalizedString(R.string.richeng), style = TextStyle(fontSize = 30.sp))
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = { navController.navigate("mainright")
                                if (currentPage != "代办") {
                                    currentPage = "代办"
                                    // 跳转到代办页面的逻辑
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentPage == "代办") Color.Blue else Color.Gray
                            ),
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(maxHeight / 60)
                        ) {
                            Text(text = LocalizedString(R.string.daiban), style = TextStyle(fontSize = 30.sp))
                        }

                        // 右边偏移
                        Spacer(modifier = Modifier.weight(1f))

                    }
                }
            }

            // 悬浮的齿轮按钮和展开内容
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.End
            ) {
                var expanded by remember { mutableStateOf(false) }
                var rotation by remember { mutableStateOf(0f) }
                val animatedRotation by animateFloatAsState(
                    targetValue = rotation,
                    animationSpec = TweenSpec(durationMillis = 2000)
                )

                IconButton(onClick = {
                    expanded = !expanded
                    rotation += if (expanded) 360f else -360f
                }) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.graphicsLayer(rotationZ = animatedRotation)
                    )
                }

                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically()
                ) {
                    Column {
                        Button(
                            onClick = { /* 实现切换主题风格的逻辑 */
                                val context = context
                                val currentTheme = ThemePreference.getTheme(context)
                                // 计算下一个主题
                                val nextTheme = (currentTheme % 4) + 1 // 假设有3个主题，1, 2, 3
                                // 更新主题设置
                                ThemePreference.setTheme(context, nextTheme)
                                // 调用回调以通知主题已更改
                                navController.navigate("mainleft")
                            },
                            modifier = Modifier
                                .width(200.dp) // 设置固定宽度
                                .height(50.dp), // 设置固定高度
                            colors = ButtonDefaults.buttonColors(

                            ),
                            border = BorderStroke(1.dp, Color.Gray), // 添加边框
                            shape = RoundedCornerShape(8.dp) // 设置圆角
                        ) {
                            Text(text = LocalizedString(R.string.or1))
                        }
                        Button(
                            onClick = {
                                // 这里切换语言，例如从英文切换到中文
                                currentLanguage = if (currentLanguage == "en") "zh" else "en"
                                navController.navigate("mainleft")
                                // 可能需要刷新UI或执行其他操作来应用语言更改
                            },
                            modifier = Modifier
                                .width(200.dp) // 设置固定宽度
                                .height(50.dp), // 设置固定高度
                            colors = ButtonDefaults.buttonColors(

                            ),
                            border = BorderStroke(1.dp, Color.Gray), // 添加边框
                            shape = RoundedCornerShape(8.dp) // 设置圆角
                        ) {
                            Text(text = if (currentLanguage == "en") "Switch to Chinese" else "切换到英文")
                        }
                        Button(
                            onClick = { navController.navigate("mypage") },
                            modifier = Modifier
                                .width(200.dp) // 设置固定宽度
                                .height(50.dp), // 设置固定高度
                            colors = ButtonDefaults.buttonColors(

                            ),
                            border = BorderStroke(1.dp, Color.Gray), // 添加边框
                            shape = RoundedCornerShape(8.dp) // 设置圆角
                        ) {
                            Text(text = LocalizedString(R.string.or3), style = TextStyle(fontSize = 30.sp))
                        }
                    }
                }



            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class, ExperimentalWearMaterialApi::class)
@Composable
fun SwipeToDeleteItem(userInput: UserInput, onDelete: () -> Unit, onEdit: () -> Unit) {
    val swipeableState = rememberSwipeableState(initialValue = false)
    val anchors = mapOf(0f to false, -300f to true) // 定义滑动锚点

    // 动态计算文本的右侧内边距，当按钮即将显示时增加内边距
    val paddingRight = if (swipeableState.offset.value < -150) 72.dp else 0.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.25f) },
                orientation = Orientation.Horizontal
            )
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .padding(end = paddingRight) // 应用动态计算的右侧内边距
        ) {
            Text(
                text = "${LocalizedString(R.string.tim)}: ${userInput.hours}:${userInput.minutes}:${userInput.seconds}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(4.dp)) // 添加少量间隔
            Text(
                text = userInput.text,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis // 根据需要调整文本溢出行为
            )
        }

        // 按钮布局，保持在Box的右侧，与文本内容不重叠
        if (swipeableState.offset.value < -150) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }

                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}