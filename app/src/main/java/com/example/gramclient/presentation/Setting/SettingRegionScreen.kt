package com.example.gramclient.presentation.Setting

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.gramclient.presentation.components.CustomTopBar

@Composable
fun SettingRegionScreen(navController: NavHostController){
    Scaffold(topBar = { CustomTopBar(title = "Регион", navController = navController) }) {
        Text(text = "Setting Region")
    }
}
