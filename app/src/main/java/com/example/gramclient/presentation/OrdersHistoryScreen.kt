package com.example.gramclient.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomTopBar

@Composable
fun OrdersHistoryScreen(navController: NavHostController) {
    Column{
        CustomTopBar(title = "История заказов", navController = navController)
        ListHistoryItem(status = "Выполнен")
        ListHistoryItem(status = "Отменен")
    }
}

@Composable
fun ListHistoryItem(status: String) {
    val expanded = remember { mutableStateOf(false) }
    Card(
        Modifier
            .padding(top = 15.dp, start = 15.dp, end = 15.dp)
            .fillMaxWidth()
    ) {
        Box(contentAlignment = TopEnd) {
            Column(
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth()
                    .padding(end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(start=35.dp)
                ) {
                    Text(
                        text = "18 апреля 2022, 14:38 - 14:54 / ",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 25.dp)
                    )
                    Text(
                        text = status,
                        fontSize = 15.sp,
                        color = if (status == "Выполнен") Color(0xFF46C258) else Color(
                            0xFFFF1100
                        )
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.fillMaxWidth(0.1f)) {
                        Image(
                            modifier = Modifier.size(25.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.from_marker),
                            contentDescription = "Logo"
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(start = 10.dp)
                    ) {
                        Text(
                            text = "Паприка (Меҳмонхонаи Суғдиён)", fontWeight = FontWeight.Bold
                        )
                        Text(text = "Максудчони Танбури улица")
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.fillMaxWidth(0.1f)) {
                        Image(
                            modifier = Modifier.size(25.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.to_marker),
                            contentDescription = "Logo"
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Паприка (Меҳмонхонаи Суғдиён)", fontWeight = FontWeight.Bold
                        )
                        Text(text = "Максудчони Танбури улица")
                    }
                }
            }
            IconButton(
                onClick = { expanded.value = true },
                modifier = Modifier
                    .fillMaxWidth(0.1f)
                    .padding(end = 5.dp)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_menu_blue),
                    contentDescription = ""
                )
                DropdownMenu(expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }) {
                    DropdownMenuItem(onClick = {}) {
                        Text("Удалить")
                    }
                    DropdownMenuItem(onClick = {}) {
                        Text("Повторить")
                    }
                    DropdownMenuItem(onClick = {}) {
                        Text("Обратный маршрут")
                    }
                }
            }

        }
    }
}