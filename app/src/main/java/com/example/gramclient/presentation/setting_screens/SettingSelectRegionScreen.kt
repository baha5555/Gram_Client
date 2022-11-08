package com.example.gramclient.presentation.setting_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomTopBar

@Composable
fun SettingSelectRegionScreen(navController: NavHostController) {
    Scaffold(topBar = { CustomTopBar(title = "Выбор страны", navController) }) {
        Row(){
            Image(imageVector = ImageVector.vectorResource(id = R.drawable.flag_tj), contentDescription = "tj")
            Text(text = "Таджикистан")
        }
    }
}