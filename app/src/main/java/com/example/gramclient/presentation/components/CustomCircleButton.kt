package com.example.gramclient.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramclient.ui.theme.FontSilver

@Composable
fun CustomCircleButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FloatingActionButton(
            onClick = { onClick.invoke() },
        ) {
            Icon(imageVector = icon, "", modifier = Modifier.size(30.dp))
        }
        Spacer(modifier = Modifier.requiredHeight(3.dp))
        Text(text = text, fontSize = 12.sp, color = FontSilver, textAlign = TextAlign.Center)
    }
}