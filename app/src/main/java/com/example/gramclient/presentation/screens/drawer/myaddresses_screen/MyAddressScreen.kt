package com.example.gramclient.presentation.screens.drawer.myaddresses_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.utils.RoutesName
import com.example.gramclient.presentation.components.CustomTopAppBar

@Composable
fun MyAddressesScreen(navController: NavHostController) {
    Scaffold(topBar = {
        CustomTopAppBar(navController = navController, action = { AddAddress(navController) })
    }) {
        Column(
            Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.white))
        ) {
            Text(
                text = "Мои адреса",
                fontSize = 28.sp,
                modifier = Modifier.padding(25.dp),
                color = Color(0xFF434B53)
            )
            ListAddressesShow(R.drawable.ic_home, "Дом","пр. И.Сомони 46а, подъезд 1", navController)
            ListAddressesShow(R.drawable.ic_work, "Работа", "пр. И.Сомони 46а, подъезд 1", navController)
            ListAddressesShow(R.drawable.ic_favorites, "Отдых", "пр. И.Сомони 46а, подъезд 1", navController)
            ListAddressesShow(R.drawable.ic_favorites, "К тёте", "пр. И.Сомони 46а, подъезд 1", navController)

        }
    }

}

@Composable
fun AddAddress(navController: NavHostController) {
    IconButton(onClick = {navController.navigate(RoutesName.EDIT_ADDRESSES_SCREEN) }) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_add),
            contentDescription = "back"
        )
    }
}


@Composable
fun ListAddressesShow(image: Int, title: String, text: String, navController: NavHostController) {
    Row(Modifier.padding(vertical = 5.dp, horizontal = 10.dp)) {
        Box(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(0.15f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = ImageVector.vectorResource(image),
                contentDescription = "home"
            )
        }
        Column(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text=title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(text=text, fontSize = 12.sp)
        }
    }
}
