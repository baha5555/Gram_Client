package com.gram.client.presentation.screens.drawer.setting_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gram.client.presentation.components.CustomTopBar

@Composable
fun SettingRegionScreen(navController: NavHostController) {
    Scaffold(topBar = { CustomTopBar(title = "Регион", 1) }) {
        Column() {
            val search = remember {
                mutableStateOf("")
            }
//            CustomSearch(search)
            ListRegion("Худжанд", "Согдийская область")
//            ListRegion("Душанбе", "Город республиканского значения")
//            ListRegion("Худжанд", "Хатлонская область")
        }
    }

}

@Composable
fun ListRegion(title: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column() {
            Text(text = title, fontSize = 18.sp)
            Text(text = text, fontSize = 15.sp, color = Color(0xFF565E66))
        }
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                color = Color(0xFFBAC2CA)
            ),
    )
}
