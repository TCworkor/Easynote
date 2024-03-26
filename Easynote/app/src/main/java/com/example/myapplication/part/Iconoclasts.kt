package com.example.myapplication.part

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.currentLanguage

@Composable
fun Iconoclasts() {
    // 获取当前上下文
    val context = LocalContext.current

    // MediaPlayer实例化并配置
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.iconoclasts).apply {
            isLooping = true // 如果需要循环播放
        }
    }
    // 图片资源示例
    val image = painterResource(id = R.drawable.iconoclasts)
    // 定义图片资源
    val images = listOf(
        R.drawable.lbi, // 第二张图片资源
        R.drawable.lb,
        R.drawable.sz,
        R.drawable.bla,
        R.drawable.ig,
        R.drawable.bo


    )

    // 定义中英文文本内容
    val textze = listOf(
        Pair("在这里我要感谢和推荐一款游戏， iconoclasts   ，  Iconoclasts是一款由独立开发者Joakim Sandberg开发的2D动作冒险游戏。这款游戏以其精美的像素艺术风格、扣人心弦的故事情节和丰富的关卡设计而闻名。玩家扮演主角罗宾，探索一个充满危险和谜团的科幻世界，使用各种工具和武器来解决难题和战斗敌人。",
            "Here, I want to express my gratitude and recommend a game, Iconoclasts. Iconoclasts is a 2D action-adventure game developed by the independent developer Joakim Sandberg. The game is renowned for its exquisite pixel art style, compelling storyline, and rich level design. Players take on the role of the protagonist, Robin, exploring a world filled with dangers and mysteries, utilizing a variety of tools and weapons to solve puzzles and combat enemies."),
        // 按顺序添加更多文本对
        Pair("这个游戏是我心目中的神作，我认为它的各个方面都很优秀。而它最让人津津乐道的是它真的是一款\"游戏\"。它的程度把握的很好，在各种创意和想法的组合下，它始终没有偏离它的核心意义，那就是让人放松，为人带来快乐。",
            "This game is a masterpiece in my eyes, and I believe it excels in every aspect. What makes it truly noteworthy is that it's genuinely a \"game.\" It strikes a perfect balance, combining various creative ideas and thoughts without deviating from its core purpose—to relax and bring joy to people."),

        Pair("但是，如果它只是一款简单的游戏，如果只是一个小小的快乐点子，哪还不足以触动我。它是一个有着完整剧情的游戏，虽然有人认为它的剧情不合理，但是我却认为它的剧情处理的很棒。快乐但是也让人思考，滑稽但是却又严肃。",
            "However, if it were just a simple game, a mere point of happiness, it wouldn't have touched me as deeply. It's a game with a complete storyline. Although some might find its plot unreasonable, I think it's brilliantly executed. It's joyous yet thought-provoking, humorous yet serious."),

        Pair("我很喜欢里面的角色，里面的角色都是立体且生动的，它们都是主角，而不是配角。每个人都有自己的思想，它们按照自己的方式行动，而不是为主角，为玩家而表演。我感叹于里面角色的人格魅力，怀念哪些敌人和朋友。",
            "I really like the characters in the game; they are all three-dimensional and vivid, heroes rather than side characters. Each one has their own thoughts and acts in their own way, not just for the protagonist or the player. I admire the charisma of these characters, reminiscing about those enemies and friends."),

        Pair("这部作品给了我很大的触动，而当我了解到这部作品是一个独立作者花费近10年完成的成果时，我发自真心的向作者致敬。作者是一个很有才华的人，我看过作者的其它游戏，里面的设计和点子同样让人眼前一亮。但是，一个人，负责所有的美术、音乐、编程和剧本，这是一件多么困难的事。透过这部花费近10年完成的  iconoclasts  。我可以感受到作者的理想、希望、痛苦、迷茫。以及他想为大家带来快乐的心情。",
            "This work has greatly moved me, and when I learned that it was the result of nearly a decade's effort by an independent creator, I sincerely saluted the author. The author is a talented individual. I've seen his other games, which also feature impressive designs and ideas. But for one person to handle all the art, music, programming, and scriptwriting is incredibly challenging. Through Iconoclasts, which took nearly ten years to complete, I can feel the author's ideals, hopes, pains, and confusions, as well as his desire to bring happiness to everyone."),

        Pair("虽然我可能解读过度哪，但是我想像  独立开发者  Joakim  Sandberg  一样，将  理想、希望、痛苦、迷茫   倾注于一个个作品，为这个世界带来礼物！这就是我开发  这个小作品的理由，很简单，很纯粹，这仅仅只是一个开始！！！TC让我们加油吧！！！\n" +
                "最后，再次向   独立开发者  Joakim  Sandberg   致以最崇高的敬意！！！",
            "Although I might be overinterpreting, I wish to, like the independent developer Joakim Sandberg, pour ideals, hopes, pains, and confusions into my works, bringing gifts to the world! This is the reason I developed this little project. It's simple and pure, just a beginning!!! TC, let's keep up the good work!!! Finally, I extend my highest respect to the independent developer Joakim Sandberg once again!!!")


    )


    // 创建一个记忆的滚动状态
    val scrollState = rememberScrollState()

    // 添加背景
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            ///horizontalAlignment 和 verticalArrangement 是 Column 的两个属性，用于控制内部元素在水平和垂直方向上的对齐方式。
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = if (currentLanguage == "en") "Hello, friend who has discovered this easter egg. Although this is a modest software with many imperfections, every project has its surprises!" else "你好，发现这个彩蛋的朋友，虽然这是一个小小的而且有着很多不足的软件，但是凡事都少不了彩蛋！",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier
                .height(16.dp)
                .width(64.dp))


            // UI内容，例如展示文本或按钮等
            Text(
                text = if (currentLanguage == "en") "Playing music..." else "正在播放音乐...",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Image(
                painter = image,
                contentDescription = "iconoclasts",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.White, shape = RoundedCornerShape(8.dp))
            )



            // 其他UI元素
            textze.forEachIndexed { index, text ->
                // 显示文本
                Text(
                    color = Color.White,
                    text = if (currentLanguage == "zh") text.first else text.second,
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )

                // 如果有对应的图片资源，则在文本之后显示图片
                if (index < images.size) {
                    Image(
                        painter = painterResource(id = images[index]),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .padding(bottom = 8.dp)
                    )
                }
            }

        }
    }

    // 当Iconoclasts组件首次进入组合时播放音乐
    LaunchedEffect(Unit) {
        mediaPlayer.start()
    }

    // 当离开Iconoclasts组件时停止并释放MediaPlayer资源
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }


}