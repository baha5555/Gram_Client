package com.gram.client.presentation.screens.drawer.myaddresses_screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CustomTextField(textState: MutableState<String>, label: String, enabled: Boolean = true) {
    TextField(
        value = textState.value,
        onValueChange = { newText ->
            textState.value = newText
        },
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            textColor = Color.Gray,
            disabledTextColor = Color.Transparent,
            unfocusedIndicatorColor = Color(0xFFDEDEDE),
            disabledIndicatorColor = Color.Transparent
        ),
        enabled = enabled
    )
}