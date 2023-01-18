package com.example.gramclient.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
@Composable
fun ShimmerRectangleItem(brush: Brush){
    Row(modifier = Modifier
        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
    ) {
        repeat(4){
            Spacer(modifier = Modifier
                .weight(1f)
                .height(90.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(brush)
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}
@Composable
fun ShimmerLinearItem(brush: Brush) {
    Row(modifier = Modifier
        .fillMaxSize()
        .padding(top = 15.dp, bottom = 10.dp), verticalAlignment = Alignment.Top) {
        Spacer(modifier = Modifier.width(10.dp))
        Column(verticalArrangement = Arrangement.Center) {
            repeat(3){
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier
                        .height(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth(fraction = 0.7f)
                        .background(brush)
                    )
                    Spacer(modifier = Modifier.fillMaxWidth(fraction = 0.2f))
                    Spacer(modifier = Modifier
                        .height(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .background(brush)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
@Composable
fun CustomLinearShimmer(enabled:Boolean){
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            RepeatMode.Restart
        )
    )
    if (enabled){
        ShimmerLinearItem(brush = linearGradient(
            colors =  listOf(
                Color.LightGray.copy(alpha = 0.9f),
                Color.LightGray.copy(alpha = 0.4f),
                Color.LightGray.copy(alpha = 0.9f)
            ),
            start = Offset(10f, 10f),
            end = Offset(translateAnim, translateAnim)
        )
        )
    }
}
@Composable
fun CustomRectangleShimmer(enabled:Boolean){
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            RepeatMode.Restart
        )
    )
    if (enabled){
        ShimmerRectangleItem(brush = linearGradient(
            colors =  listOf(
                Color.LightGray.copy(alpha = 0.9f),
                Color.LightGray.copy(alpha = 0.4f),
                Color.LightGray.copy(alpha = 0.9f)
            ),
            start = Offset(10f, 10f),
            end = Offset(translateAnim, translateAnim)
        )
        )
    }
}