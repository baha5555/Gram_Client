package com.example.gramclient.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gramclient.ui.theme.FontSilver

@Composable
fun CustomCircleButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FloatingActionButton(
            onClick = { onClick.invoke() },
            backgroundColor = Color(0xFFF5F4F2),
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Icon(imageVector = icon, "", modifier = Modifier.size(30.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = text, color = FontSilver, textAlign = TextAlign.Center)
    }
}
@Composable
fun CustomCircleButton(text: String, icon: Int, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FloatingActionButton(
            onClick = { onClick.invoke() },
            backgroundColor = Color(0xFFF5F4F2),
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Image(painter = painterResource(id = icon), "", modifier = Modifier.size(30.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = text, color = FontSilver, textAlign = TextAlign.Center)
    }
}