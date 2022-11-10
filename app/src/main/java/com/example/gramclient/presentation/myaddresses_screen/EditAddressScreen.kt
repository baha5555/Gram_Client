package com.example.gramclient.presentation.myaddresses_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import com.example.gramclient.presentation.components.CustomSearch
import com.example.gramclient.presentation.components.CustomTopBar

@Composable
fun EditAddressScreen(navController: NavHostController) {
    Scaffold(topBar = { CustomTopBar("Редактирование адреса", navController, 2) }) {
        val search = remember {
            mutableStateOf("Дом")
        }
        Column() {
            CustomSearch(search = search, "Введите название адреса")
            Row() {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(0.2f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.from_marker),
                        contentDescription = null
                    )
                }
                Column(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(1f).padding(end=20.dp),
                    Arrangement.SpaceEvenly
                ) {
                    Column() {
                        Text(
                            text = "Паприка (Меҳмонхонаи Суғдиён)",
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp
                        )
                        Text(text = "Максудчони Танбури улица", fontSize = 16.sp)
                    }
                    Box(
                        modifier = Modifier
                            .size(200.dp, 30.dp)
                            .background(Color(0xDFE2EAF2), CircleShape)
                            .border(1.dp, Color(0xFFBAC2CA), CircleShape),
                        contentAlignment = Alignment.Center
                        ) {
                        Text(text = "Куда лучше подъехать?", color = Color(0xFF6A727A))
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
            }
        }
    }
}