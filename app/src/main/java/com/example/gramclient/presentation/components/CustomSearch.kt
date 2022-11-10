package com.example.gramclient.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomSearch(search: MutableState<String>, placeholderText: String = "Поиск...") {

    TextField(
        value = search.value,
        onValueChange = {
            search.value = it
        },
        trailingIcon = {
            if (search.value.isNotEmpty())
                IconButton(
                    onClick = {
                        search.value = ""
                    },
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
        },
        placeholder = { Text(text = placeholderText, fontSize = 19.sp) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = Color(0xFF005EFF),
            focusedIndicatorColor = Color(0xFF005EFF),
            leadingIconColor = Color(0xFF005EFF)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        textStyle = TextStyle(fontSize = 20.sp),

        )

}