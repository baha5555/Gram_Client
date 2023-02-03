package com.example.gramclient.presentation.screens.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    title: String
) {

    Box(modifier = Modifier.width(115.dp)){
        Image(painter = painterResource(id = R.drawable.img_fast_address), contentDescription = null)
        Image(painter = painterResource(id = R.drawable.back_fast_address), contentDescription = null)
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable { }
                .padding(top = 15.dp, start = 15.dp, end = 15.dp)
        ) {
            Text(text = title, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = "15 мин", color = Color.Black, fontSize = 12.sp)
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_car),
                contentDescription = "car_eco",
            )
        }
    }
}