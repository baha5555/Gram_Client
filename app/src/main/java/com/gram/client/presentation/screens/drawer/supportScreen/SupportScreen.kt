package com.gram.client.presentation.screens.drawer.supportScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import com.gram.client.R
import com.gram.client.ui.theme.PrimaryColor

var stateContent: MutableState<String> = mutableStateOf("")

@Composable
fun SupportScreen(navController: NavHostController) {

    Scaffold(topBar = {
        TopAppBar(backgroundColor = colorResource(id = R.color.white)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                //Back
                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = {
                        if (stateContent.value.isNotEmpty()) stateContent.value = ""
                        else navController.popBackStack()
                    }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
                            contentDescription = "back"
                        )
                    }
                }
            }
        }
    }) {
        when( stateContent.value){
            ""->{ SupportContent(stateContent)
            }
            "Contact"->{
                ShowContent(
                    "Доверенные контакты", "Эти контакты всегда будут под рукой. Сможете\n" +
                            "отправить им ваш маршрут и просьбу\n" +
                            "позвонить.",
                    "Добавить контакт", PrimaryColor, R.drawable.ic_contact
                )
            }
            "AmbulancePolice"->{
                ShowContent(
                    "Скорая и полиция", "Приготовьтесь рассказать, что призошло и где\n" +
                            "вы находитесь.", "Позвонить", Color(0xFFF93E2B), R.drawable.ic_alarm_light
                )
            }
            "EmergencySituation"->{
                EmergencySituation(navController)
            }
        }
    }
}

