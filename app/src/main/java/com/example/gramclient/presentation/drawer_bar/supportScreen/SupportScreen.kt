package com.example.gramclient.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.presentation.components.CustomCircleButton
import com.example.gramclient.ui.theme.PrimaryColor

@Composable
fun SupportScreen(navController: NavHostController) {
    val stateContent = remember { mutableStateOf("") }

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
        Column(
            Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.white))
        ) {
            Text(
                text = "Поддержка",
                fontSize = 28.sp,
                modifier = Modifier.padding(25.dp),
                color = PrimaryColor
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                CustomCircleButton(text = "Экстренная\nситуация", icon = Icons.Default.Headphones) {

                }
                CustomCircleButton(text = "Доверенные\nконтакты", icon = R.drawable.ic_contact) {
                    stateContent.value = "Contact"
                }
                CustomCircleButton(text = "Скорая \nи полиция", icon = R.drawable.ic_alarm_light) {
                    stateContent.value = "AmbulancePolice"
                }

            }
        }
        when( stateContent.value){
            "Contact"->{showContent(
                "Доверенные контакты", "Эти контакты всегда будут под рукой. Сможете\n" +
                        "отправить им ваш маршрут и просьбу\n" +
                        "позвонить.",
                "Добавить контакт", PrimaryColor, R.drawable.ic_contact
            )}
            "AmbulancePolice"->{showContent(
                "Скорая и полиция", "Приготовьтесь рассказать, что призошло и где\n" +
                        "вы находитесь.", "Позвонить", Color(0xFFF93E2B), R.drawable.ic_alarm_light
            )}
        }
    }
}

@Composable
private fun showContent(title: String, text: String, textBtn: String, color: Color, icon: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(100))
                        .background(color)
                        .padding(8.dp)
                )
                Text(
                    text = title,
                    fontSize = 24.sp,
                    color = com.example.gramclient.ui.theme.PrimaryColor,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
            Spacer(modifier = Modifier.requiredHeight(20.dp))
            Text(
                text = text, fontSize = 15.sp
            )
        }

        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(57.dp),
            text = textBtn,
            textSize = 16,
            textBold = true,
            color = color
        ) {

        }
    }
}