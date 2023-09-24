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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gram.client.R
import com.gram.client.presentation.components.CustomStory
import java.io.InputStream

@SuppressLint("CoroutineCreationDuringComposition")
@Preview(showBackground = true)
@Composable
fun StoryScreen() {
    val context = LocalContext.current
    val titles = context.resources.getStringArray(R.array.Story_1_title)
    val texts = context.resources.getStringArray(R.array.Story_1_text)
    val img = context.resources.getStringArray(R.array.Story_1_img)
    val isActive = remember {
        mutableStateOf(0)
    }
    

    CustomStory(count = titles.size, slideDurationInSeconds = 8, activedInx = isActive) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        0f to Color(0xffd9eaff),
                        1f to Color(0xff9ccaff),
                        start = Offset(393f, 0f),
                        end = Offset(0f, 852f)
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            storyViewModel.getItemStory(1).forEach{
//                Text(text = ""+it.text)
//            }
            Spacer(modifier = Modifier.height(100.dp))
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)) {
                Text(
                    text = titles[isActive.value],
                    color = Color(0xff1c1c1c),
                    style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = texts[isActive.value],
                    color = Color(0xff1c1c1c),
                    style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp)
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            val ims: InputStream = context.assets.open("images/${img[isActive.value]}")
            val drawable: Drawable? = Drawable.createFromStream(ims, null)
            val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = if(isActive.value==0)Alignment.CenterStart else Alignment.CenterEnd){
                Image(bitmap = bitmap.asImageBitmap(), contentDescription = "", modifier = Modifier.size(350.dp))
            }
        }
    }
}