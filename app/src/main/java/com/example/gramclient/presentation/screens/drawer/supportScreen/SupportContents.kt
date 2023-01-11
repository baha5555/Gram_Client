package com.example.gramclient.presentation.screens.drawer.supportScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.example.gramclient.utils.RoutesName
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.presentation.components.CustomCircleButton
import com.example.gramclient.ui.theme.PrimaryColor

@Composable
fun SupportContent(stateContent: MutableState<String>) {
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
                stateContent.value = "EmergencySituation"
            }
            CustomCircleButton(text = "Доверенные\nконтакты", icon = R.drawable.ic_contact) {
                stateContent.value = "Contact"
            }
            CustomCircleButton(text = "Скорая \nи полиция", icon = R.drawable.ic_alarm_light) {
                stateContent.value = "AmbulancePolice"
            }

        }
    }
}

@Composable
fun ShowContent(title: String, text: String, textBtn: String, color: Color, icon: Int) {
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
                    color = PrimaryColor,
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

@Composable
fun EmergencySituation(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Headphones,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(100))
                    .background(PrimaryColor)
                    .padding(8.dp)
            )
            Text(
                text = "Экстренная ситуация",
                fontSize = 24.sp,
                color = PrimaryColor,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        val listNames = arrayOf(
            "Произошло ДТП",
            "Жалоба на исполнителя",
            "Проблемы с оплатой",
            "Долгое ожидание",
            "В машине остались вещи",
            "Другой вопрос"
        )
        listNames.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(RoutesName.MESSAGE_SCREEN)}
                    .padding(vertical = 15.dp, horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = it, fontSize = 16.sp)
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_forward_blue),
                    contentDescription = ""
                )
            }
            if (it != "Другой вопрос") Divider()
        }
    }
}