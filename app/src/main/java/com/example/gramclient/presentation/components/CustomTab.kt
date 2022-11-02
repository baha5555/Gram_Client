package com.example.gramclient.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomTab() {
    var selectedIndex by remember { mutableStateOf(0) }

    val list = listOf("Такси", "Доставка")

    TabRow(selectedTabIndex = selectedIndex,
        backgroundColor = Color(0xFFE2EAF2),
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50)),
        indicator = { tabPositions: List<TabPosition> ->
            Box {}
        }
    ) {
        list.forEachIndexed { index, text ->
            val selected = selectedIndex == index
            Tab(
                modifier = if (selected) Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        Color(0xff1E76DA)
                    )
                else Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        Color(
                            0xFFE2EAF2
                        )
                    ),
                selected = selected,
                onClick = { selectedIndex = index },
                text = { Text(text = text, color = if (selected) Color(0xFFFFFFFF) else Color(0xFF000000)) }
            )
        }
    }
}