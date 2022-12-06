package com.example.gramclient.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.gramclient.ui.theme.PrimaryColor

@Composable
fun CustomSwitch(
    switchON: MutableState<Boolean>,
    scale: Float = 1.7f,
    width: Dp = 28.dp,
    height: Dp = 17.dp,
    strokeWidth: Dp = 1.dp,
    checkedTrackColor: Color = PrimaryColor,
    uncheckedTrackColor: Color = Color(0xFF707070),
    gapBetweenThumbAndTrackEdge: Dp = 3.dp,
    onClick: () -> Unit
) {
    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge
    val animatePosition = animateFloatAsState(
        targetValue = if (switchON.value)
            with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
        else
            with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
    )
    Canvas(
        modifier = Modifier
            .size(width = width, height = height)
            .scale(scale = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        // This is called when the user taps on the canvas
                        switchON.value = !switchON.value
                        onClick()
                    }
                )
            }.background( Color(0xFF928F8F), shape = CircleShape)
    ) {
        // Track
        drawRoundRect(
            color = if (switchON.value) PrimaryColor else Color(0xFFB6B4B4),
            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
        )

        // Thumb
        drawCircle(
            color =  Color.White,
            radius = if (switchON.value) thumbRadius.toPx()+2 else thumbRadius.toPx() ,
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )
    }
}