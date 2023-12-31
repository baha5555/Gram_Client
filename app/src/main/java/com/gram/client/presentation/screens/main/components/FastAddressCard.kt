package com.gram.client.presentation.screens.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gram.client.R
import com.gram.client.domain.mainScreen.Address

@Composable
fun FastAddressCard(
    title: String,
    address: Address,
    icon: Int = R.drawable.ic_clock,
    onClick: () -> Unit
) {
    val localDensity = LocalDensity.current
    var widthIs by remember {
        mutableStateOf(0.dp)
    }
    Column(
        modifier = Modifier
            .height(115.dp)
            .widthIn(115.dp, 150.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(color = Color(0xFFEEEEEE))
            .clickable {
                onClick()
            }
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.onGloballyPositioned { coordinates ->
            widthIs = with(localDensity) { coordinates.size.width.toDp() }
        }) {
            Text(
                text = title,
                color = Color.Black,
                maxLines = 2,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(text = address.city, color = Color.Black, fontSize = 12.sp)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.widthIn(if(widthIs<115.dp) 100.dp else widthIs)
        ) {
            Image(
                painter = painterResource(id = R.drawable.car_fast_addresses),
                contentDescription = "car_eco",
            )
            Image(
                painter = painterResource(id = icon),
                contentDescription = "car_eco",
            )
        }
    }
}