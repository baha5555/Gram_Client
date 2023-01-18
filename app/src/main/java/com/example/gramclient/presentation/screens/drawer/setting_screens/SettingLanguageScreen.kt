package com.example.gramclient.presentation.screens.drawer.setting_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.gramclient.presentation.components.CustomCheckBox
import com.example.gramclient.presentation.components.CustomTopBar
import com.example.gramclient.ui.theme.BackgroundColor

@Composable
fun SettingLanguageScreen(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(BackgroundColor),
    ) {
        CustomTopBar(title = "Язык", navController = navController)
        CheckBoxGroupGroup()


    }
}


@Composable
private fun CheckBoxGroupGroup(
    items: List<String> = arrayListOf("Русский", "English", "Francais", "Украiнська"),
    items_2: List<String> = arrayListOf("", "Английский", "Французский", "Украинский")
) {
    val state = remember { mutableStateOf("") }
    Column {
        items.forEach { item ->
            Row(
                modifier = Modifier.clickable { state.value = item }
                    .padding(start = 10.dp, end = 22.dp, top = 12.dp, bottom = 10.dp)
                    .fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(start = 16.dp),
                        fontSize = 18.sp,
                        color =Color.Black
                    )
                    if (items_2[items.lastIndexOf(item)] != "") {
                        Text(
                            text = items_2[items.lastIndexOf(item)],
                            modifier = Modifier
                                .padding(start = 16.dp),
                            fontSize = 15.sp,
                            color = Color(0xFF565E66)
                        )
                    }
                }

                CustomCheckBox(
                    isChecked = state.value == item,
                    onChecked = { state.value = item })
            }
        }
    }
}