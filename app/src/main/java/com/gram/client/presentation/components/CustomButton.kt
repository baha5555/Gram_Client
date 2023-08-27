package com.gram.client.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(
    modifier: Modifier,
    text: String,
    textSize: Int,
    textBold: Boolean,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colors.primary,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    Button(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(15.dp),
        enabled = enabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = color, contentColor = Color.White),
        content = {
            if (isLoading){
                CircularProgressIndicator(
                    color= Color.White
                )
            }
            else{
                Text(text = text, fontWeight = if(textBold) FontWeight.Bold else FontWeight.Normal, fontSize = textSize.sp, lineHeight = 28.sp)
            }
        },
    )
}