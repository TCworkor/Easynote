package com.example.myapplication.part

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.currentLanguage
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale




// 主题偏好设置单例对象，用于存储和获取应用主题设置
// Singleton object for theme preference settings, used for storing and retrieving app theme settings
object ThemePreference {
    // SharedPreferences文件的名称
    // Name of the SharedPreferences file
    private const val PREFERENCES_NAME = "app_theme_preferences"

    // 用于存储主题偏好的键
    // Key used for storing theme preference
    private const val KEY_THEME = "theme"

    // 获取当前主题设置，如果未设置，默认为1
    // Get the current theme setting, default to 1 if not set
    fun getTheme(context: Context): Int {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_THEME, 1) // Default theme is 1
    }

    // 设置应用的主题
    // Set the app's theme
    fun setTheme(context: Context, theme: Int) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_THEME, theme)
        editor.apply()
    }
}

// 用于获取本地化字符串的Composable函数
// A Composable function to retrieve localized strings
@Composable
fun LocalizedString(@StringRes id: Int): String {
    val context = LocalContext.current
    val config = Configuration(context.resources.configuration)
    when (currentLanguage) {
        "zh" -> config.setLocale(Locale.SIMPLIFIED_CHINESE) // 设置为简体中文
        else -> config.setLocale(Locale.ENGLISH) // Default to English
    }
    return context.createConfigurationContext(config).resources.getString(id)
}

// 快速文本输入的浮动按钮组件
// Floating button component for quick text input
@Composable
fun QuickTextFloatingButton(text: MutableState<String>, options: List<String>) {
    var expanded by remember { mutableStateOf(false) } // 记录是否展开
    Box(contentAlignment = Alignment.BottomEnd) {
        // 浮动操作按钮，用于展开或收起选项
        FloatingActionButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                if (expanded) Icons.Filled.Close else Icons.Filled.Add,
                contentDescription = if (expanded) "Close" else "Open"
            )
        }

        // 展开时显示的快速输入选项列表
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .offset(x = (-96).dp, y = 0.dp) // 调整位置
                    .heightIn(max = 140.dp) // 最大高度
                    .background(Color.White) // 白色背景
            ) {
                items(options) { option ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp) // 内边距
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)) // 边框
                    ) {
                        TextButton(
                            onClick = {
                                text.value = option // 更新文本状态
                                expanded = false // 收起列表
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(35.dp) // 固定高度
                        ) {
                            Text(option)
                        }
                    }
                }
            }
        }
    }
}




// 简洁日历组件
// A minimalistic calendar component
@Composable
fun MiniCalendar() {
    // 获取当前日期的实例
    // Get an instance of the current date
    val calendar = Calendar.getInstance()
    val dayOfMonth = SimpleDateFormat("d", Locale.getDefault()).format(calendar.time) // 日
    val month = SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.time) // 月份
    val dayOfWeek = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time) // 星期

    // 使用Box布局显示日历，其中包含月份、日期和星期
    // Display the calendar in a Box layout containing the month, date, and weekday
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(100.dp) // 设置尺寸
            .background( // 设置背景为垂直渐变
                brush = Brush.verticalGradient(
                    colors = listOf(Color.LightGray, Color.Gray),
                    startY = 0f,
                    endY = 100f
                ),
                shape = RoundedCornerShape(8.dp) // 设置圆角
            )
            .padding(8.dp) // 设置内边距

    ) {
        // 使用Column布局垂直排列文本组件
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 显示月份
            Text(
                text = month.uppercase(), // 将月份转为大写
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray) // 设置样式
            )
            // 显示日期
            Text(
                text = dayOfMonth, // 显示日期
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Color.Black) // 设置样式
            )
            // 显示星期
            Text(
                text = dayOfWeek, // 显示星期
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray) // 设置样式
            )
        }
    }
}

//感恩节
fun calculateThanksgivingDate(year: Int): String {
    val month = 11 // 感恩节在11月
    var dayOfMonth = 1
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, dayOfMonth)

    // 找到11月的第一个星期四
    while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY) {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    // 添加21天到达第四个星期四
    calendar.add(Calendar.DAY_OF_MONTH, 21)
    val thanksgivingDay = calendar.get(Calendar.DAY_OF_MONTH)

    // 构造MMdd格式的日期字符串
    return String.format("%02d%02d", month, thanksgivingDay)
}


@Composable
fun DateBasedImage() {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MMdd")
    val todayFormatted = today.format(formatter)

    val thanksgiving = calculateThanksgivingDate(today.year)

    // 定义一个节日和对应图片资源的Map
    val festivalImages = mapOf(
        "0101" to R.drawable.new_year_day, // 元旦
        "0214" to R.drawable.valentines_day, // 情人节
        "0308" to R.drawable.international_women_day, // 妇女节
        "0422" to R.drawable.earth_day, // 地球日
        "0501" to R.drawable.international_workers_day, // 劳动节
        "0601" to R.drawable.international_children_day, // 儿童节
        "0921" to R.drawable.international_day_of_peace, // 国际和平日
        "1031" to R.drawable.halloween, // 万圣节
        "1225" to R.drawable.christmas_day, // 圣诞节
        thanksgiving to R.drawable.thanksgiving_day, // 感恩节
//        "0214" to R.drawable.valentines_day, // 情人节
//        "0214" to R.drawable.valentines_day, // 情人节
//        "0214" to R.drawable.valentines_day, // 情人节
//        "0214" to R.drawable.valentines_day, // 情人节
        // 在这里添加更多节日和对应的图片资源
    )

    // 定义星期一至星期日的图片资源
    val weekdayImages = listOf(
        R.drawable.sunday,
        R.drawable.monday,
        R.drawable.tuesday,
        R.drawable.wednesday,
        R.drawable.thursday,
        R.drawable.friday,
        R.drawable.saturday
        // 根据实际情况调整资源
    )

    // 根据当前日期判断显示哪个图片资源
    val imageRes = festivalImages[todayFormatted]
        ?: weekdayImages[today.dayOfWeek.value % 7] // 注意：DayOfWeek是从1开始的

    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "根据日期显示的图片",
        modifier = Modifier.size(100.dp) // 或其他合适的尺寸
    )


}
