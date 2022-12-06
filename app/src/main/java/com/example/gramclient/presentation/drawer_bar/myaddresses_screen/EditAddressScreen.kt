package com.example.gramclient.presentation.drawer_bar.myaddresses_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.presentation.components.CustomSearch
import com.example.gramclient.presentation.components.CustomTopAppBar
import com.example.gramclient.presentation.components.CustomTopBar

@Composable
fun EditAddressScreen(navController: NavHostController) {
    Scaffold(topBar = { CustomTopAppBar(title = "Дом", navController = navController) }) {
        Column(Modifier.padding(10.dp)) {
            var text by remember { mutableStateOf(TextFieldValue("")) }
            TextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                label = { Text(text = "Название")},
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )
            Column {
                Text(text = "")
            }
        }
    }
}