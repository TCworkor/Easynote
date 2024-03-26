package com.example.myapplication


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.myapplication.part.AppDatabase
import com.example.myapplication.part.DateSelectorScreen
import com.example.myapplication.part.Iconoclasts
import com.example.myapplication.part.TextWithDate
import com.example.myapplication.part.TextWithDateRepository
import com.example.myapplication.part.TextWithDateScreen
import com.example.myapplication.part.TextWithDateViewModel
import com.example.myapplication.part.TimerScreenWithDialog
import com.example.myapplication.part.UserInput
import com.example.myapplication.part.UserInputScreen
import com.example.myapplication.part.UserInputViewModel
import com.example.myapplication.part.UserRepository
import com.example.myapplication.part.ViewModelFactory
import com.example.myapplication.part.nxuimain
import com.example.myapplication.part.uimain
import java.util.Locale


// 默认语言设置，如果需要可以修改，例如 "zh" 代表中文
// Default language setting, can be modified as needed, e.g., "zh" for Chinese
var currentLanguage = Locale.getDefault().language

// MainActivity.kt
// 主活动类，继承自 ComponentActivity
// The main activity class, inheriting from ComponentActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化数据库和仓库
        // Initialize the database and repositories
        val appDatabase = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(appDatabase.userInputDao())
        val textWithDateRepository = TextWithDateRepository(appDatabase.textWithDateDao())

        // 创建 ViewModelFactory 实例
        // Create an instance of ViewModelFactory
        val factory = ViewModelFactory(userRepository, textWithDateRepository)

        // 获取 UserInputViewModel 和 TextWithDateViewModel 实例
        // Retrieve UserInputViewModel and TextWithDateViewModel instances
        val userInputViewModel: UserInputViewModel by viewModels { factory }
        val textWithDateViewModel: TextWithDateViewModel by viewModels { factory }

        setContent {
            MaterialTheme {
                // 应用导航
                // App navigation
                AppNavigation(userInputViewModel, textWithDateViewModel)
            }
        }

        // 创建通知渠道
        // Create a notification channel
        createNotificationChannel()
    }

    // 创建通知渠道的方法
    // Method for creating a notification channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 通道名称和描述
            // Channel name and description
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("jianji", name, importance).apply {
                description = descriptionText
            }
            // 创建通知渠道
            // Create the notification channel
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

// AppNavigation.kt
// 导航主函数，定义应用内页面的导航
// Main navigation function, defines navigation within the app
@Composable
fun AppNavigation(userInputViewModel: UserInputViewModel, textWithDateViewModel: TextWithDateViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "mainleft") {
        // 定义不同页面的路由
        // Define routes for different pages
        composable("mainleft") { Mainleft(navController, userInputViewModel) }
        composable("write") { Write(navController, userInputViewModel) }
        composable("writeTextWithDate") { WriteTextWithDate(navController, textWithDateViewModel) }
        composable("mainright") { Mainright(navController, textWithDateViewModel) }
        composable("mypage") { myPage() }

        // 其他路由定义
        // Other route definitions
        composable("Iconoclasts") { Iconoclasts() } // 神秘页面的路由
        // Parameterized routes for dynamic navigation
        // 参数化路由，用于动态导航
        composable("writeTextWithDate/{textWithDateId}") { backStackEntry ->
            val textWithDateId = backStackEntry.arguments?.getString("textWithDateId")?.toInt() ?: 0
            val textWithLiveData = textWithDateViewModel.getUserInputById(textWithDateId)
            val textWithData by textWithLiveData.observeAsState()
            textWithData?.let {
                WriteTextWithDate(navController, textWithDateViewModel, it)
            }
        }

        composable("write/{userInputId}") { backStackEntry ->
            val userInputId = backStackEntry.arguments?.getString("userInputId")?.toInt() ?: 0
            val userInputLiveData = userInputViewModel.getUserInputById(userInputId)
            val userInput by userInputLiveData.observeAsState()
            userInput?.let {
                Write(navController, userInputViewModel, it)
            }
        }
    }
}


@Composable
fun Mainleft(navController: androidx.navigation.NavController,userInputViewModel: UserInputViewModel) {
    /*Button(onClick = { navController.navigate("page2") }) {
        Text("Go to Page 2")
    }*/
    uimain(navController,userInputViewModel,"日程")


}

@Composable
fun Write(navController: androidx.navigation.NavController,userInputViewModel: UserInputViewModel,dying : UserInput? = null) {
    var userInputState by remember { mutableStateOf(UserInput()) }
    /*Button(modifier = Modifier.offset(60.dp,80.dp), onClick = { navController.popBackStack() }) {
        Text("Return to Page 1")
    }*/
    AppTheme {
        Column {
            UserInputScreen(userInputViewModel, navController, userInputState, dying)

            TimerScreenWithDialog(userInputState, dying)
        }
    }
}




@Composable
fun WriteTextWithDate(navController: androidx.navigation.NavController, textWithDateViewModel: TextWithDateViewModel, dying: TextWithDate? = null) {
    var textWithDateState by remember { mutableStateOf(TextWithDate()) }

    AppTheme {
        Column {
            TextWithDateScreen(textWithDateViewModel, navController, textWithDateState, dying)
            // Add other composables related to TextWithDate if needed
            DateSelectorScreen(textWithDateState, dying)

        }
    }
}



@Composable
fun Mainright(navController: androidx.navigation.NavController,textWithDateViewModel: TextWithDateViewModel) {
    // “代办”
    nxuimain(navController,textWithDateViewModel,"代办")

}

@Composable
fun myPage() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {

        if (currentLanguage == "en"){

            // License part
            Text("License", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(
                "This application and its source code are subject to a custom license. You are free to use and modify the code, but it may not be used for commercial purposes. For detailed license content, please refer to the LICENSE file included.",
                style = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Contributor's Guide
            Text("Contributor's Guide", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(
                "We welcome and encourage community contributions! If you wish to contribute code, please follow the pull request process on GitHub. Make sure your code adheres to the best practices for Kotlin and Jetpack Compose.",
                style = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Terms of Use
            Text("Terms of Use", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(
                "This application is for personal use and learning exchange only. Any activities conducted through this software must comply with local laws and internet regulations.",
                style = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Privacy Policy
            Text("Privacy Policy", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(
                "This application does not collect any user data. All data is stored locally on the device and will not be uploaded or shared.",
                style = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Disclaimer
            Text("Disclaimer", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(
                "This software and all related content are provided 'as is', without any form of warranty. The copyright of third-party materials used in the software belongs to their original authors, and are for learning and communication purposes only.",
                style = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Acknowledgements
            Text("Acknowledgements", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(
                "Special thanks to all individuals and organizations who share knowledge and resources on the internet. Without their selfless sharing, the development of this application would not have been possible.",
                style = TextStyle(fontSize = 16.sp)
            )

        }
        else {
            // 许可证部分
            Text("许可证", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(
                "本应用及其源代码遵循自定义许可证。你可以自由使用和修改代码，但不得用于商业目的。详细许可证内容请查阅附带的LICENSE文件。",
                style = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 贡献者指南
            Text("贡献者指南", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(
                "我们欢迎并鼓励社区贡献！如果你想贡献代码，请遵循GitHub上的pull request流程。确保你的代码遵循Kotlin和Jetpack Compose的最佳实践。",
                style = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 使用条款
            Text("使用条款", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(
                "本应用仅供个人使用和学习交流，任何通过本软件进行的活动必须遵守当地法律和网络规则。",
                style = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 隐私政策
            Text("隐私政策", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(
                "本应用不收集任何用户数据。所有数据仅存储于本地设备中，并不会被上传或分享。",
                style = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 免责声明
            Text("免责声明", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(
                "本软件和所有相关内容按“现状”提供，不作任何形式的保证。软件中使用的第三方素材版权归原作者所有，仅供学习和交流使用。",
                style = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 致谢
            Text("致谢", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(
                "特此感谢互联网上所有共享知识和资源的个人和组织。没有他们的无私分享，本应用的开发将不可能完成。",
                style = TextStyle(fontSize = 16.sp)
            )
        }
    }
}

