package com.gram.client.presentation.screens.main.components

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.presentation.screens.story.StoryScreen
import java.io.InputStream

@Preview(showBackground = true)
@Composable
fun StoriesList() {
    val navigator = LocalNavigator.currentOrThrow
    val res = LocalContext.current.resources
    val storiesList = res.getStringArray(R.array.StoryList)
    val storiesListImage = res.getStringArray(R.array.StoryListImage)
    val color1 = arrayOf(
        Color(0xFF9DDCFF),
        Color(0xFFB6EDFF),
        Color(0xFF9CCAFF),
        Color(0xFFFFFBCF),
        Color(0xFF46FFC8),
        Color(0xFFA3EAFF)
    )
    val color2 = arrayOf(
        Color(0xFFB0FFFF),
        Color(0xFFF6C9FA),
        Color(0xFFD9EAFF),
        Color(0xFFFFDE9A),
        Color(0xFFC1FFED),
        Color(0xFFDCF7FF)
    )
    Column(Modifier.fillMaxSize()) {
        for (i in 0 until storiesList.size step 2) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
            ) {

                StoriesCard(
                    modifier = Modifier
                        .height(115.dp)
                        .weight(1f),
                    text = storiesList[i],
                    image = storiesListImage[i],
                    color1 = color1[i],
                    color2 = color2[i]
                ){
                    when(i){
                        0-> navigator.push(StoryScreen(5))
                            2->navigator.push(StoryScreen(1))
                        4->navigator.push(StoryScreen(3))
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                StoriesCard(
                    modifier = Modifier
                        .height(115.dp)
                        .weight(1f),
                    text = storiesList[i + 1],
                    image = storiesListImage[i + 1],
                    color1 = color1[i+1],
                    color2 = color2[i+1]
                ) {
                    when(i+1){
                        1->navigator.push(StoryScreen(6))
                            3->navigator.push(StoryScreen(2))
                        5->navigator.push(StoryScreen(4))
                    }
                }
            }
        }
    }
}

@Composable
private fun StoriesCard(
    modifier: Modifier,
    text: String,
    image: String,
    color1: Color,
    color2: Color,
    function: () -> Unit
) {
    val ims: InputStream = LocalContext.current.assets.open("images/${image}")
    val drawable: Drawable? = Drawable.createFromStream(ims, null)
    val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    0f to color1,
                    1f to color2,
                    start = Offset(393f, 0f),
                    end = Offset(0f, 852f)
                )
            ).clickable { function() },
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "",
            modifier = Modifier
                .size(90.dp)
                .offset(5.dp, 10.dp)
                .align(
                    Alignment.BottomEnd
                )
        )
        Text(text = text, fontSize = 16.sp, modifier = Modifier.padding(15.dp))
    }
}