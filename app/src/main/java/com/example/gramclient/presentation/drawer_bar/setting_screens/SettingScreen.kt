package com.example.gramclient.presentation.drawer_bar.setting_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomSwitch
import com.example.gramclient.presentation.components.CustomTopBar
import com.example.gramclient.ui.theme.BackgroundColor

@Composable
fun SettingScreen(navController: NavHostController) {
    Scaffold(topBar = { CustomTopBar("Параметры", navController) }) {
        Column(
            Modifier
                .fillMaxSize()
                .background(BackgroundColor)
        ) {
            Column(Modifier.clickable {
                navController.navigate(RoutesName.SETTING_REGION_SCREEN)
            }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
                        Text(text = "Регион", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = "Худжанд", fontSize = 15.sp, color = Color(0xFF565E66))
                    }
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_blue_2),
                        contentDescription = ""
                    )
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
            Column(Modifier.clickable { navController.navigate(RoutesName.SETTING_LANGUAGE_SCREEN) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
                        Text(
                            text = "Язык приложения",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(text = "Русский", fontSize = 15.sp, color = Color(0xFF565E66))
                    }
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_blue_2),
                        contentDescription = ""
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            color = Color(0xFFBAC2CA)
                        )
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 30.dp, top = 20.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                Text(text = "Геолокация", fontSize = 18.sp)
                val switchON = remember {
                    mutableStateOf(false) // Initially the switch is ON
                }
                CustomSwitch(switchON)
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 30.dp, top = 25.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                Text(text = "Не звонить", fontSize = 18.sp)
                val switchON = remember {
                    mutableStateOf(false) // Initially the switch is ON
                }
                CustomSwitch(switchON)
            }

        }
    }
}



