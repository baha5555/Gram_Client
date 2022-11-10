package com.example.gramclient.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramclient.RoutesName

@Composable
fun CustomButton(
    modifier: Modifier,
    text: String,
    textSize: Int,
    textBold: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
        },
        enabled = enabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2264D1), contentColor = Color.White),
        content = { Text(text = text, fontWeight = if(textBold) FontWeight.Bold else FontWeight.Normal, fontSize = textSize.sp, lineHeight = 28.sp) },
    )
}