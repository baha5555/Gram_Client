package com.gram.client.presentation.screens.story

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import java.io.InputStream

class StoryScreen(private var storySelect: Int) : Screen{
    @Composable
    override fun Content() {
        StoryScreenContent(storySelect)
    }

}
@SuppressLint("CoroutineCreationDuringComposition")
@Preview(showBackground = true)
@Composable
fun StoryScreenContent(storySelect: Int = 1) {
    val storyViewModel: StoryViewModel = hiltViewModel()
    val getStory = storyViewModel.getStories(storySelect)

    val isActive = remember {
        mutableStateOf(0)
    }
    val context = LocalContext.current

    val color1 = when (storySelect) {
        2 -> Color(0xffB6EDFF)
        6 -> Color(0xffB0FFFF)
        1 -> Color(0xffDCF7FF)
        5 -> Color(0xffC1FFED)
        4 -> Color(0xffFFFBCF)
        else -> Color(0xffd9eaff)
    }
    val color2 = when (storySelect) {
        2 -> Color(0xffF6C9FA)
        6 -> Color(0xff9DDCFF)
        1 -> Color(0xffA3EAFF)
        5 -> Color(0xff46FFC8)
        4 -> Color(0xffFFDE9A)
        else -> Color(0xff9ccaff)
    }

    CustomStory(count = getStory.size, slideDurationInSeconds = 8, activedInx = isActive) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        0f to color1,
                        1f to color2,
                        start = Offset(393f, 0f),
                        end = Offset(0f, 852f)
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            ) {
                Text(
                    text = getStory[isActive.value].title.toString(),
                    color = Color(0xff1c1c1c),
                    style = TextStyle(fontSize = 30.sp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = getStory[isActive.value].text.toString(),
                    color = Color(0xff1c1c1c),
                    style = TextStyle(fontSize = 18.sp)
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            val ims: InputStream = context.assets.open("images/${getStory[isActive.value].src.toString()}")
            val drawable: Drawable? = Drawable.createFromStream(ims, null)
            val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier
                        .height(if(storySelect==6) 350.dp else 500.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}