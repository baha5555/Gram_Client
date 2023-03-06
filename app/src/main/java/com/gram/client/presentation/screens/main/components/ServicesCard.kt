package com.gram.client.presentation.screens.main.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gram.client.R

@Composable
fun ServicesCard(
    serviceName: String
) {
    Row(
        modifier = Modifier
            .width(147.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { }
            .border(border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(20.dp)),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_box),
            contentDescription = "ic_box"
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = serviceName, color = Color.Black, fontSize = 14.sp)
    }
}