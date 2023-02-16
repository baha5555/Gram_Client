package com.example.gramclient.presentation.screens.order.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomCircleButton


@Composable
fun actionSection(cancelOrderOnClick:()->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colors.background)
            .padding(20.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ){
        CustomCircleButton(text = "Отменить\nзаказ",
            icon = Icons.Default.Close,cancelOrderOnClick)
        Spacer(modifier = Modifier.width(20.dp))
        CustomCircleButton(text = "Отправить\nмаршрут",
            icon = ImageVector.vectorResource(id = R.drawable.share_icon)) {
            //method
        }
        Spacer(modifier = Modifier.width(20.dp))
        CustomCircleButton(text = "Безопас-\nность",
            icon = ImageVector.vectorResource(id = R.drawable.safety_icon)) {
            //method
        }
    }
}