package com.example.gramclient.presentation.myaddresses_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.presentation.components.CustomSearch
import com.example.gramclient.presentation.components.CustomTopBar

@Composable
fun AddAddressScreen(navController: NavHostController) {
    Scaffold(topBar = {
        CustomTopBar(
            title = "Добавление адреса",
            navController = navController
        )
    }) {
        val search = remember {
            mutableStateOf("")
        }
        Column {
            CustomSearch(search = search, "Название, например “Дом”")
            Row(
                Modifier
                    .fillMaxHeight(0.15f)
                    .padding(20.dp)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.from_marker),
                    contentDescription = null
                )
                Column(Modifier.padding(start = 15.dp)) {
                    Text(text = "Указать адрес", fontWeight = FontWeight.Medium, fontSize = 20.sp)
                    Divider(Modifier.padding(top = 5.dp))
                }
            }
            Box(
                Modifier
                    .fillMaxHeight()
                    .padding(20.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Добавить в избранное",
                    textSize = 18,
                    textBold = false
                ) {
                }
            }
        }
    }
}