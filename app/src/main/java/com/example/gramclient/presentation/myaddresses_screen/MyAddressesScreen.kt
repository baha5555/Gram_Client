package com.example.gramclient.presentation.myaddresses_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.presentation.components.CustomTopBar

@Composable
fun MyAddressesScreen(navController: NavHostController){
    Scaffold(topBar = { CustomTopBar(title = "Мои Адреса", navController = navController)
    }) {
        Column() {
            Text(text = "Адреса", fontSize = 22.sp, modifier = Modifier.padding(15.dp), color = Color(0xFF434B53))
        }
    }
    
}