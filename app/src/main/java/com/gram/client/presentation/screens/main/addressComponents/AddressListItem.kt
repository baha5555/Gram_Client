package com.gram.client.presentation.screens.main.addressComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gram.client.domain.mainScreen.Address

@Composable
fun AddressListItem(it: Address, onItemClick: (String) -> Unit) {
    Column( modifier = Modifier.padding(vertical = 10.dp).clickable { onItemClick(if(it.type == "address") "${it.street}, ${it.name}" else it.name) }) {
        Text(
            text = if(it.type == "address") "${it.street}, ${it.name}" else it.name,
            fontSize = 18.sp,
            maxLines = 1
        )
        Text(
            text = if(it.type == "address") "${it.city}, ${it.region}" else "${it.street}",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF989898),

                ),
        )
    }
}