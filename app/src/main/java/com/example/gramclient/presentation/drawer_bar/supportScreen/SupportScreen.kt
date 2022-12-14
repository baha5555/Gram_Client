package com.example.gramclient.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.presentation.components.CustomCircleButton
import com.example.gramclient.presentation.components.CustomTopBar
import com.example.gramclient.presentation.drawer_bar.myaddresses_screen.ListAddressesShow
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.PrimaryColor

@Composable
fun SupportScreen(navController: NavHostController) {
    Scaffold(topBar = {
        TopAppBar(backgroundColor = colorResource(id = R.color.white)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                //Back
                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = { navController.popBackStack() }) {
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

                }
                CustomCircleButton(text = "Скорая \nи полиция", icon = R.drawable.ic_alarm_light) {

                }

            }
        }

    }
}