package com.gram.client.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gram.client.utils.Comments

@Composable
fun CustomBigButton(
    text: String,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(57.dp)
        .padding(horizontal = 15.dp)
        .offset(0.dp, -15.dp),
    enabled: Boolean = true,
    onCLick: () -> Unit
) {
    Button(
        onClick = {
            onCLick()
        },
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        elevation = ButtonDefaults.elevation(0.dp)
    ) {
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    }
}