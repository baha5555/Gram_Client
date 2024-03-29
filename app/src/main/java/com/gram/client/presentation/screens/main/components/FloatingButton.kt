package com.gram.client.presentation.screens.main.components

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.gram.client.ui.theme.PrimaryColor


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FloatingButton(
    icon: ImageVector,
    backgroundColor: Color = PrimaryColor,
    contentColor: Color = Color.White,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = Modifier
            .size(50.dp)
            .offset(y = (-35).dp),
        backgroundColor = backgroundColor,
        onClick = {
            onClick.invoke()
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Menu", tint = contentColor,
            modifier = Modifier.size(25.dp)
        )
    }
}

@Composable
fun FloatingButton2(
    icon: ImageVector,
    backgroundColor: Color = PrimaryColor,
    contentColor: Color = Color.White,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = Modifier
            .size(50.dp)
            .offset(y = (-10).dp),
        backgroundColor = backgroundColor,
        onClick = {
            onClick.invoke()
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Menu", tint = contentColor,
            modifier = Modifier.size(25.dp)
        )
    }
}