package com.example.gramclient.presentation.myaddresses_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomTopBar

@Composable
fun MyAddressesScreen(navController: NavHostController) {
    Scaffold(topBar = {
        CustomTopBar(title = "Мои Адреса", navController = navController)
    }) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEBEBEB))
            ) {
                Text(
                    text = "Адреса",
                    fontSize = 22.sp,
                    modifier = Modifier.padding(15.dp),
                    color = Color(0xFF434B53)
                )
                listAddressesShow(1, "Дом", navController)
                listAddressesShow(2, "Работа", navController)
            }
            FloatingActionButton(
                backgroundColor = Color(0xFF2264D1),
                contentColor = Color.White,
                onClick = { navController.navigate(RoutesName.ADD_ADDRESSES_SCREEN) },
                modifier = Modifier.offset(-30.dp, -35.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    Modifier.size(35.dp)
                )
            }
        }
    }

}

@Composable
fun listAddressesShow(num: Int, title: String, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .padding(horizontal = 15.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .offset(-10.dp, 10.dp)
                    .clickable { navController.navigate(RoutesName.EDIT_ADDRESSES_SCREEN)},
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit_blue),
                contentDescription = "",
            )
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                Column(
                    Modifier.padding(start = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "" + num,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2264D1),
                        modifier = Modifier
                            .offset(0.dp, 3.dp)
                            .size(24.dp)
                            .border(1.5.dp, Color(0xFF2264D1), CircleShape),
                        textAlign = TextAlign.Center
                    )
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_location_blue),
                        contentDescription = null,
                        modifier = Modifier.offset(0.dp, 20.dp)
                    )
                }
                Column(Modifier.padding(start = 10.dp)) {
                    Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                    Column(Modifier.padding(top = 10.dp)) {
                        Text(
                            text = "Паприка (Меҳмонхонаи Суғдиён)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Максудчони Танбури улица",
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
}
