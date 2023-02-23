package com.example.gramclient.presentation.screens.main.components

import android.text.style.BackgroundColorSpan
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.gramclient.ui.theme.PrimaryColor


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