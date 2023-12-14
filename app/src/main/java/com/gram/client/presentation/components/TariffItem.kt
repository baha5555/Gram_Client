@file:Suppress("UNUSED_EXPRESSION")

package com.gram.client.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.gram.client.presentation.screens.main.states.CalculateResponseState
import com.valentinilk.shimmer.shimmer

@Composable
fun TariffItem(
    icon: String?,
    price: Int,
    key: String,
    name: String,
    isSelected: Boolean,
    onSelected: () -> Unit = {},
    stateCalculate: CalculateResponseState
) {

    Column(
        modifier = Modifier
            .width(73.dp)
            .height(76.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onSelected()
            }
            .background(if (isSelected) MaterialTheme.colors.secondary else Color(0x80FFFFFF))
            .padding(10.dp)


    ) {

        Image(
            modifier = Modifier.weight(1f),
            painter = rememberAsyncImagePainter(icon),
            contentDescription = "icon",
            alpha = if (isSelected) 1.0F else 0.5F
        )
        Text(text = name, fontSize = 12.sp, color = if (isSelected) Color.Black else Color.Black.copy(0.5f), maxLines = 1)
        AnimatedVisibility(visible = true) {
            Box(modifier = Modifier.height(15.dp)){
                Text(text = "$price $key", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.Black else Color.Black.copy(0.5f))
            }
        }

    }
}