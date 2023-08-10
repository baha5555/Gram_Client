package com.gram.client.presentation.screens.drawer.orderHistoryScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.domain.orderHistory.CustomMainMapHistory
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.utils.Constants
import java.text.SimpleDateFormat

class CardOrderHistory : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: OrderHistoryViewModel = hiltViewModel()
        val order = viewModel.selectedOrder.value


        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier.fillMaxHeight(0.1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TopAppBar(
                        content = {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                IconButton(
                                    modifier = Modifier,
                                    onClick = { navigator.pop() }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_blue),
                                        contentDescription = ""
                                    )
                                }
                                val dateTimeString = order.created_at ?: ""
                                Log.e(order.created_at, "$order")
                                Text(
                                    text = "Поездка в ${
                                        if (dateTimeString != "") "${
                                            SimpleDateFormat(
                                                "yyyy-MM-dd HH:mm:ss"
                                            ).parse(dateTimeString).hours
                                        }:${
                                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                                                dateTimeString
                                            ).minutes
                                        }" else " "
                                    }",
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.Center),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                        elevation = 0.dp
                    )
                }
            }, backgroundColor = Color.White
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    CustomMainMapHistory(
                        selectedOrder = viewModel.selectedOrder.value,
                    )
                }
                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 25.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column {
                                Text(
                                    text = "Статус поездки",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${viewModel.selectedOrder.value.status}",
                                    color = if (order.status == "Выполнен") Color.Green else Color.Red,
                                    fontSize = 15.sp
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                               // if (order.status == "Выполнен"){
                                Text(
                                    text = viewModel.selectedOrder.value.performer?.first_name
                                        ?: "   ",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .background(
                                            shape = RoundedCornerShape(3.dp),
                                            color = Color(0xFFF4B91D)
                                        )
                                )
                                //}

                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_car),
                                    contentDescription = "car_eco",
                                    modifier = Modifier
                                        .offset(y = (-10).dp)
                                        .size(80.dp)
                                )
                            }
                        }/*
                        Divider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.avatar),
                                    "",
                                    modifier = Modifier.size(55.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (order.performer == null) "  " else "${order.performer.first_name}",
                                    color = FontSilver,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            CustomCircleButton(
                                text = "Связаться \n" +
                                        "с водителем",
                                icon = ImageVector.vectorResource(id = R.drawable.phone)
                            ) {
                                Toast.makeText(
                                    context,
                                    "Эти страницы на стадии разработки",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            CustomCircleButton(
                                text = "Помощь\n" +
                                        "с заказом",
                                icon = ImageVector.vectorResource(id = R.drawable.ic_help)
                            ) {
                                Toast.makeText(
                                    context,
                                    "Эти страницы на стадии разработки",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))*/
                        Divider()
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(20.dp),
                                        imageVector = ImageVector.vectorResource(R.drawable.from_marker),
                                        contentDescription = "Logo"
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Text(
                                        text = viewModel.selectedOrder.value.from_address?.name
                                            ?: "Откуда?",
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 55.dp, end = 15.dp)
                            ) {
                                Divider()
                            }
                            order.to_addresses?.forEach { address ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(15.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                    ) {
                                        Image(
                                            modifier = Modifier
                                                .size(20.dp),
                                            imageVector = if (MaterialTheme.colors.isLight) ImageVector.vectorResource(
                                                R.drawable.to_marker
                                            ) else ImageVector.vectorResource(
                                                R.drawable.to_marker_dark
                                            ),
                                            contentDescription = "Logo"
                                        )
                                        Spacer(modifier = Modifier.width(20.dp))
                                        Text(
                                            text = address.name,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 55.dp, end = 15.dp)
                                ) {
                                    Divider()
                                }
                            }
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Общая стоимость", fontSize = 16.sp)
                                    Text(text = "${order.price} смн", fontSize = 16.sp)
                                }
                                Divider()
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = "Поездка", fontSize = 16.sp)
                                        Text(
                                            text = "Повышенный спрос",
                                            fontSize = 12.sp,
                                            color = Color(0xff9C9C9C)
                                        )
                                    }
                                    Text(
                                        text = "${order.price} смн",
                                        fontSize = 16.sp,
                                        color = Color(0xff9C9C9C)
                                    )
                                }
                                Divider()
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Наличные",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 18.sp
                                    )
                                    Image(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = ImageVector.vectorResource(R.drawable.wallet_icon),
                                        contentDescription = "Logo"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

