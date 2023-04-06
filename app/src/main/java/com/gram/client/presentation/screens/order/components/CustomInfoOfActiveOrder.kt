package com.gram.client.presentation.screens.order.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.domain.firebase.order.RealtimeDatabaseOrder
import com.gram.client.presentation.components.CustomSwitch
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import kotlinx.coroutines.launch

class CustomInfoOfActiveOrder : Screen {
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

        }) {
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.64f)
                        .background(MaterialTheme.colors.onPrimary)
                        .padding(vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(imageVector = Icons.Default.ArrowBackIos, contentDescription = null)
                    }
                    Text(
                        text = "Детали заказа", fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp
                    )
                }
                LazyColumn() {
                    item {
                        selectRealtimeDatabaseOrder.let { order ->
                            CustomInfoTitle(title = "Маршрут")
                            order.from_address?.address?.let {
                                CustomAddressText(
                                    title = it,
                                    point = R.drawable.ic_from_address_marker
                                )
                            }
                            order.to_address?.let { toAddress ->
                                for (i in toAddress.indices) {
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 50.dp)
                                    )
                                    CustomAddressText(
                                        title = toAddress[i].address,
                                        point = if (i == toAddress.size - 1) R.drawable.ic_to_address_marker else R.drawable.ic_to_address_second_marker
                                    )
                                }
                            }
                            order.create_order?.let {
                                CustomInfoTitle(title = "Время")
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 15.dp, horizontal = 15.dp),
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
                                        .padding(vertical = 15.dp, horizontal = 15.dp)
                                ) {
                                    Text(text = it)
                                }
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 15.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 15.dp, horizontal = 15.dp)
                                ) {
                                    Text(text = "${order.price} смн.")
                                }
                            }
                            CustomInfoTitle(title = "Способ оплаты")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 15.dp, horizontal = 15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Наличные")
                            }
                            order.performer?.let { performer ->
                                CustomInfoTitle(title = "Водитель")
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 15.dp, horizontal = 15.dp),
                                ) {
                                    Text(
                                        text = (performer.first_name
                                            ?: "") + " " + (performer.last_name ?: "")
                                    )
                                }
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 15.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 15.dp, horizontal = 15.dp),
                                ) {
                                    performer.transport?.let { transport ->
                                        Text(text = "${transport.color} ${transport.model}, ${transport.car_number}")
                                    }
                                }
                            }
                            order.allowances?.let { allowance ->
                                CustomInfoTitle(title = "Надбавки")
                                CustomSelectAllowances(
                                    title = "Увеличить",
                                    text1 = "1 c",
                                    text2 = "2 c",
                                    text3 = "3 c",
                                    text4 = "4 c"
                                )
                                Divider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 15.dp)
                                        )
                                CustomSelectAllowances(
                                    title = "С детьми",
                                    text1 = "1",
                                    text2 = "2",
                                    text3 = "3",
                                    text4 = "4"
                                )
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 15.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 15.dp),
                                ) {
                                    allowance.forEach {
                                        Row(modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text(
                                                text = it.name, modifier = Modifier
                                                    .padding(vertical = 25.dp)
                                            )
                                            Row(modifier = Modifier,
                                            verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "+ ${it.price} c", modifier = Modifier
                                                        .padding(vertical = 25.dp).padding(end = 15.dp)
                                                )
                                                val switchON = remember {
                                                    mutableStateOf(false) // Initially the switch is ON
                                                }
                                                CustomSwitch(switchON = switchON) {}
                                            }

                                        }

                                        Divider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 15.dp)
                                        )
                                    }
                                }
                                CustomSelectAllowances(
                                    title = "Сдача с",
                                    text1 = "50 c",
                                    text2 = "100 c",
                                    text3 = "300 c",
                                    text4 = "500 c"
                                )
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 15.dp)
                                )
                                CustomSelectAllowances(
                                    title = "Чаевые",
                                    text1 = "1 c",
                                    text2 = "2 c",
                                    text3 = "3 c",
                                    text4 = "4 c"
                                )
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
        point: Int
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(15.dp),
                painter = painterResource(id = point),
                contentDescription = null
            )
            Text(
                text = title, modifier = Modifier
                    .padding(start = 15.dp)
                    .fillMaxWidth(0.8f)
            )
        }
    }

    @Composable
    fun CustomInfoTitle(title: String) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primaryVariant)
                .padding(vertical = 15.dp, horizontal = 15.dp)
        ) {
            Text(text = title)
        }
    }

    @Composable
    fun CustomSelectAllowances(
        title: String,
        text1: String,
        text2: String,
        text3: String,
        text4: String,
    ) {
        var selectedButton by remember { mutableStateOf(0) }

        val switchON = remember {
            mutableStateOf(false) // Initially the switch is ON
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp, end = 25.dp, bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title, modifier = Modifier
                        .padding(start = 15.dp)
                )
                CustomSwitch(switchON = switchON) {}
            }
            if(switchON.value){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                ) {
                    for (i in 0 until 4) {
                        Row(Modifier.padding(start = 10.dp)) {
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size(width = 45.dp, height = 30.dp)
                                    .wrapContentWidth()
                                    .background(
                                        if (selectedButton == i) Color.Black else Color.White,
                                        shape = RoundedCornerShape(20)
                                    )
                                    .clickable { selectedButton = i }
                                    .border(
                                        width = 1.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(20)
                                    )
                            ) {
                                Text(
                                    text = when (i) {
                                        0 -> text1
                                        1 -> text2
                                        2 -> text3
                                        3 -> text4
                                        else -> ""
                                    },
                                    color = if (selectedButton == i) Color.White else Color.Black,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxSize()
                                )
                            }
                        }

                    }
                }
            }

        }
    }
}
