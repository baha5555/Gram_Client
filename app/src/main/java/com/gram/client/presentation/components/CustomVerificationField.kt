package com.gram.client.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegistrationCodeInput(codeLength: Int, initialCode: String, onTextChanged: (String) -> Unit) {
    val code = remember(initialCode) {
        mutableStateOf(TextFieldValue(initialCode, TextRange(initialCode.length)))
    }
    val focusRequester = FocusRequester()
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        BasicTextField(
            value = code.value,
            onValueChange = { onTextChanged(it.text) },
            modifier = Modifier.focusRequester(focusRequester = focusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            decorationBox = {
                CodeInputDecoration(code.value.text, codeLength)
            }
        )
    }
}

@Composable
private fun CodeInputDecoration(code: String, length: Int) {
    Box(modifier = Modifier) {
        Row(horizontalArrangement = Arrangement.Center) {
            for (i in 0 until length) {
                val text = if (i < code.length) code[i].toString() else ""
                CodeEntry(text)
            }
        }
    }
}

@Composable
private fun CodeEntry(text: String) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(65.dp)
            .height(65.dp),
        contentAlignment = Alignment.Center
    ) {
        val color = animateColorAsState(
            targetValue = if (text.isEmpty()) Color.Gray.copy(alpha = .8f)
            else Color.Blue.copy(.8f)
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.onSurface
        )
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 6.dp, end = 6.dp, bottom = 8.dp)
                .height(2.dp)
                .fillMaxWidth()
                .background(color.value)
        )
    }
}
