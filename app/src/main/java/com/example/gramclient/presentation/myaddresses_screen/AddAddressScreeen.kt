package com.example.gramclient.presentation.myaddresses_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun AddAddressScreen(navController: NavHostController){
    Scaffold(topBar = { CustomTopBar(title = "Добавление адреса", navController = navController) }) {
        val search = remember {
            mutableStateOf("")
        }
        Column() {
            CustomSearch(search = search, "Название, например “Дом”")
            Row(Modifier.padding(20.dp)) {
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.from_marker), contentDescription = null)
                Column(Modifier.padding(start = 15.dp)) {
                    Text(text = "Указать адрес", fontWeight = FontWeight.Medium, fontSize = 20.sp)
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