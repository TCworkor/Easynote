package com.example.myapplication.part

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar




// 闹钟接收器类，继承自 BroadcastReceiver
// Alarm receiver class, inherits from BroadcastReceiver
class AlarmReceiver : BroadcastReceiver() {

    companion object {
        // 媒体播放器实例，用于播放铃声
        // MediaPlayer instance, used for playing ringtone
        private var mediaPlayer: MediaPlayer? = null
    }

    // 接收广播时的处理逻辑
    // Logic to handle the received broadcast
    override fun onReceive(context: Context?, intent: Intent?) {
        if ("STOP_RINGTONE" == intent?.action) {
            // 停止铃声逻辑
            // Logic to stop the ringtone
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } else {
            context?.let {
                createNotification(it)
                playRingtone(it)
                setNextAlarm(it, intent!!)
            }
        }
    }

    // 创建通知的方法
    // Method to create a notification
    private fun createNotification(context: Context?) {
        val stopIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "STOP_RINGTONE" // 自定义行动字符串，用于在接收器中识别停止铃声的意图
            // Custom action string, used to identify the intent to stop the ringtone in the receiver
        }
        val stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 创建并显示通知
        // Create and display the notification
        val notification = NotificationCompat.Builder(context, "jianji") // 确保这个 ID 与创建渠道时的 ID 相匹配
            .setSmallIcon(R.drawable.ic_launcher_foreground) // 设置通知小图标
            .setContentTitle("Alarm") // 设置通知标题
            .setContentText("Your alarm is ringing!") // 设置通知内容
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 设置优先级
            .addAction(R.drawable.ic_launcher_foreground, "Stop", stopPendingIntent) // 添加停止铃声的操作
            .build()

        notificationManager.notify(1, notification)
    }

    // 播放铃声的方法
    // Method to play the ringtone
    private fun playRingtone(context: Context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.soun).apply {
            start()
        }
    }

    // 设置下一个闹钟的方法
    // Method to set the next alarm
    private fun setNextAlarm(context: Context, intent: Intent) {
        // 从 Intent 中获取闹钟类型和其他数据
        // Retrieve the alarm type and other data from the Intent
        val alarmType = intent.getStringExtra("alarmType")
        val dayOfWeek = intent.getIntExtra("dayOfWeek", -1)
        val hours = intent.getIntExtra("hours", 0)
        val minutes = intent.getIntExtra("minutes", 0)

        // 如果是自定义闹钟，则设置下一个闹钟
        // If it is a custom alarm, set the next alarm
        if (alarmType == "Custom" && dayOfWeek != -1) {
            val calendar = getNextAlarmTime(hours, minutes, dayOfWeek)
            val nextIntent = Intent(context, AlarmReceiver::class.java)
            nextIntent.putExtra("alarmType", alarmType)
            nextIntent.putExtra("dayOfWeek", dayOfWeek)
            nextIntent.putExtra("hours", hours)
            nextIntent.putExtra("minutes", minutes)

            val requestCode = generateUniqueRequestCode()
            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }
}


// Set the alarm based on various parameters
fun setAlarm(context: Context, hours: Int, minutes: Int, seconds: Int, alarmTriggerType: String, weekDaysState: List<Boolean>, requestCode: Int) {
    CoroutineScope(Dispatchers.Default).launch {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Remove previous alarm settings
        // 移除之前的闹钟设置
        val intent = Intent(context, AlarmReceiver::class.java)

        // Create different PendingIntent with different request codes
        // 使用不同的请求码来创建不同的 PendingIntent
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        when (alarmTriggerType) {
            "Everyday", "Once", "每天", "仅一次" -> {
                val calendar: Calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hours)
                    set(Calendar.MINUTE, minutes)
                    set(Calendar.SECOND, seconds)
                    set(Calendar.MILLISECOND, 0)
                    if (before(Calendar.getInstance())) {
                        add(Calendar.DATE, 1)
                    }
                }

                // Set repeating or exact alarm based on the type
                if (alarmTriggerType == "每天" || alarmTriggerType == "Everyday") {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                }
            }
            "自定义", "Custom" -> {
                // For custom alarm type, set alarm for each selected day of the week
                weekDaysState.forEachIndexed { index, isSelected ->
                    if (isSelected) {
                        val dayOfWeek = index + 2
                        val nextAlarmTime = getNextAlarmTime(hours, minutes, dayOfWeek)
                        val customIntent = Intent(context, AlarmReceiver::class.java).apply {
                            putExtra("alarmType", alarmTriggerType)
                            putExtra("dayOfWeek", dayOfWeek)
                            putExtra("hours", hours)
                            putExtra("minutes", minutes)
                        }
                        val customPendingIntent = PendingIntent.getBroadcast(context, requestCode + index, customIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAlarmTime.timeInMillis, customPendingIntent)
                    }
                }
            }
        }
    }
}

// Calculate the next alarm time
// 计算下一个闹钟时间
fun getNextAlarmTime(hours: Int, minutes: Int, dayOfWeek: Int): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hours)
    calendar.set(Calendar.MINUTE, minutes)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    // Adjust the calendar to the next specified day of the week
    while (calendar.get(Calendar.DAY_OF_WEEK) != dayOfWeek || calendar.before(Calendar.getInstance())) {
        calendar.add(Calendar.DATE, 1)
    }

    return calendar
}

// Generate a unique request code
// 生成唯一的请求码
fun generateUniqueRequestCode(): Int {
    // Convert the current timestamp to an integer
    // 将当前时间戳转换为整数
    return System.currentTimeMillis().toInt()
}

// Cancel the alarm
// 取消闹钟
fun cancelAlarm(context: Context, requestCode: Int) {
    // Create an Intent identical to the one used when setting the alarm
    // 创建一个与设置闹钟时相同的Intent
    val intent = Intent(context, AlarmReceiver::class.java)
    // Create a PendingIntent identical to the one used when setting the alarm, using the same request code
    // 创建一个与设置闹钟时相同的PendingIntent，重要的是要使用相同的请求码
    val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

    // Get the AlarmManager
    // 获取AlarmManager
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    // Cancel the alarm
    // 取消闹钟
    alarmManager.cancel(pendingIntent)
}
