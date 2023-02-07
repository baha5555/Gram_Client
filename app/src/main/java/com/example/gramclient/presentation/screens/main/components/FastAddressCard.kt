package com.example.gramclient.presentation.screens.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramclient.R


@Composable
fun FastAddressCard(
    title: String,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.size(115.dp)) {
        Image(
            painter = painterResource(id = R.drawable.img_fast_address),
            contentDescription = null
        )
        Image(
            painter = painterResource(id = R.drawable.back_fast_address),
            contentDescription = null,
            modifier = Modifier.clip(RoundedCornerShape(20.dp)).clickable {
                onClick.invoke()
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
                .background(color = Color(0x1400BCD4))
                .padding(top = 15.dp, start = 15.dp, end = 15.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            //Text(text = "15 мин", color = Color.Black, fontSize = 12.sp)
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_car),
                contentDescription = "car_eco",
            )
        }
    }
}