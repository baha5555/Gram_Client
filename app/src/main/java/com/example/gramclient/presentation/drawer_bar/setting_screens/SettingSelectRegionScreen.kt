package com.example.gramclient.presentation.drawer_bar.setting_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomSearch
import com.example.gramclient.presentation.components.CustomTopBar

@Composable
fun SettingSelectRegionScreen(navController: NavHostController) {
    Scaffold(topBar = { CustomTopBar(title = "Выбор страны", navController) }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val search = remember {
                mutableStateOf("")
            }
            CustomSearch(search)
            var textRegion = arrayOf<String>("Таджикистан", "Узбекистан", "Россия")
            var iconRegion = arrayOf<ImageVector>(
                ImageVector.vectorResource(id = R.drawable.flag_tj),
                ImageVector.vectorResource(id = R.drawable.flag_uz),
                ImageVector.vectorResource(id = R.drawable.flag_ru)
            )
            for (i in textRegion.indices) {
                ListRegions(text = textRegion[i], img = iconRegion[i])
            }
        }
    }
}

@Composable
fun ListRegions(text: String, img: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            imageVector = img,
            contentDescription = "tj",
            Modifier.border(color = Color(0xFFBAC2CA), width = 1.dp)
        )
        Text(text = text, modifier = Modifier.padding(start = 20.dp), fontSize = 18.sp)
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                color = Color(0xFFBAC2CA)),
    )
}
