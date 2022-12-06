package com.example.gramclient.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gramclient.R

@Composable
fun CustomOrderExecutionButton(
    onClick:()->Unit,
    icon:Int,
    text:String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            backgroundColor=Color(0xFFF5F4F2),
            modifier = Modifier
                .size(55.dp),
            shape = RoundedCornerShape(50.dp)
        ) {
            IconButton(onClick = {
                onClick()
            }) {
                Image(
                    modifier = Modifier
                        .size(25.dp),
                    painter = painterResource(icon),
                    contentDescription = "icon",
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = text, color = Color.Gray, textAlign = TextAlign.Center)
    }
}