package com.example.gramclient.presentation.setting_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomTopBar

@Composable
fun SettingRegionScreen(navController: NavHostController) {
    Scaffold(topBar = { CustomTopBar(title = "Регион", navController = navController) }) {
        Column() {
            ListRegion("Худжанд", "Согдийская область")
            ListRegion("Душанбе", "Город республиканского значения")
            ListRegion("Худжанд", "Хатлонская область")
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
