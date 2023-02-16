package com.example.gramclient.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gramclient.ui.theme.FontSilver

@Composable
fun CustomCircleButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FloatingActionButton(
            onClick = { onClick.invoke() },
            backgroundColor = MaterialTheme.colors.secondary,
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Icon(imageVector = icon, "", modifier = Modifier.size(30.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = text, color = FontSilver, textAlign = TextAlign.Center)
    }
}