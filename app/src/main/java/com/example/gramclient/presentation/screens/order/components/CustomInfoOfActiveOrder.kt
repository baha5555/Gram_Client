package com.example.gramclient.presentation.screens.order.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.gramclient.domain.firebase.order.Allowance
import com.example.gramclient.domain.firebase.order.RealtimeDatabaseOrder
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.presentation.screens.order.OrderExecutionScreen
import com.example.gramclient.presentation.screens.order.OrderExecutionViewModel
import kotlinx.coroutines.launch

class CustomInfoOfActiveOrder: Screen {
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
        val stateRealtimeDatabaseOrders =
            orderExecutionViewModel.stateRealtimeOrdersDatabase.value.response?.observeAsState()?.value
        var symbol by remember { mutableStateOf(65) }
        var selectRealtimeDatabaseOrder: RealtimeDatabaseOrder by remember {
            mutableStateOf(RealtimeDatabaseOrder())
        }
        val selectedOrder by orderExecutionViewModel.selectedOrder
        scope.launch {
            Log.e("runRe", "get")
            stateRealtimeDatabaseOrders.let { orders ->
                orders?.forEach { order ->
                    if (order.id == selectedOrder.id) {
                        Log.e("Select Order", "$selectRealtimeDatabaseOrder")
                        selectRealtimeDatabaseOrder = order
                    }
                }
            }
        }
        Scaffold(topBar = {
            TopAppBar {
                IconButton(onClick = { navigator.replace(OrderExecutionScreen()) }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(25.dp))
                Text(text = "Детали заказа", fontWeight = FontWeight.Bold)
            }
        }) {
            Column(modifier = Modifier.fillMaxHeight(0.95f)) {
                LazyColumn() {
                    item {
                        selectRealtimeDatabaseOrder.let { order ->
                            CustomInfoTitle(title = "Маршрут")
                            order.from_address?.address?.let {
                                CustomAddressText(
                                    title = it,
                                    point = "${symbol.toChar()}"
                                )
                            }
                            order.to_address?.let { toAddress ->
                                for (i in toAddress.indices) {
                                    CustomAddressText(
                                        title = toAddress[i].address,
                                        point = "${(symbol + i + 1).toChar()}"
                                    )
                                }
                            }
                            order.created_at?.let {
                                CustomInfoTitle(title = "Время")
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 15.dp, horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = it)
                                }
                            }

                            order.tariff?.let {
                                CustomInfoTitle(title = "Тариф")
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 15.dp, horizontal = 10.dp)
                                ) {
                                    Text(text = it, modifier = Modifier.padding(bottom = 15.dp))
                                }
                                Divider()
                                Text(text = "${order.price} сомони")
                            }
                            CustomInfoTitle(title = "Способ оплаты")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 15.dp, horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Наличные")
                            }
                            order.performer?.let { performer ->
                                CustomInfoTitle(title = "Водитель")
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 15.dp, horizontal = 10.dp),
                                ) {
                                    Text(
                                        text = (performer.first_name
                                            ?: "") + " " + (performer.last_name ?: "")
                                    )
                                    performer.transport?.let { transport ->
                                        Text(text = "${transport.color} ${transport.model}, ${transport.car_number}")
                                    }
                                }
                            }
                            order.allowances?.let { allowance ->
                                CustomInfoTitle(title = "Надбавки")
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 15.dp, horizontal = 10.dp),
                                ) {
                                    allowance.forEach {
                                        Text(text = it.name)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    @Composable
    fun CustomAddressText(
        title: String,
        point: String
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = point, color = Color(0xFB8B8B8B))
            Text(
                text = title, modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth(0.8f)
            )
        }
    }

    @Composable
    fun CustomInfoTitle(title: String) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFB8B8B8))
                .padding(vertical = 15.dp, horizontal = 10.dp)
        ) {
            Text(text = title)
        }
    }
}