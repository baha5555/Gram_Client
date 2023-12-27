package com.gram.client.presentation.screens.order.components

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.presentation.components.CustomDialog
import com.gram.client.presentation.components.CustomSwitch
import com.gram.client.presentation.components.voyager.ActiveAllowancesContent
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.utils.getAddressText
import kotlinx.coroutines.launch

class CustomInfoOfActiveOrder : Screen {
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
        val bottomNavigator = LocalBottomSheetNavigator.current


        var symbol by remember { mutableStateOf(65) }

        val selectedOrder by orderExecutionViewModel.selectedOrder
        val coroutineScope = rememberCoroutineScope()
        val isDialogOpen = remember { mutableStateOf(false) }

        val context = LocalContext.current

        LaunchedEffect(key1 = true) {
            orderExecutionViewModel.getAllowancesByTariffId(selectedOrder.tariff_id){
                orderExecutionViewModel.clearSelectedAllowance()
            }
        }
        Scaffold(topBar = {

        }) {
            Box(){
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
                            selectedOrder.let { order ->
                                CustomInfoTitle(title = "Маршрут")
                                order.from_address?.let {
                                    CustomAddressText(
                                        title = getAddressText(it),
                                        point = R.drawable.ic_from_address_marker
                                    )
                                }
                                order.to_addresses?.let { toAddress ->
                                    for (i in toAddress.indices) {
                                        Divider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 50.dp)
                                        )
                                        CustomAddressText(
                                            title = getAddressText(toAddress[i]),
                                            point = if (i == toAddress.size - 1) R.drawable.ic_to_address_marker else R.drawable.ic_to_address_second_marker
                                        )
                                    }
                                }
                                order.created_at?.let {
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
                                CustomInfoTitle(title = "Надбавки")

                            }
                        }
                        item {
                            ActiveAllowancesContent(modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
                if(orderExecutionViewModel.stateCalculate.value.response!=null || orderExecutionViewModel.stateCalculate.value.isLoading){
                    Row(modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(54.dp)
                        .offset(y = -25.dp)
                        .background(Color(0xFF343434), shape = RoundedCornerShape(15.dp))
                        .align(alignment = Alignment.BottomCenter),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
                            if(orderExecutionViewModel.stateCalculate.value.isLoading){
                                CircularProgressIndicator(color = Color.White)
                            } else {
                                Text(
                                    text = "${orderExecutionViewModel.stateCalculate.value.response?.result?.get(0)?.amount} c" ?: "",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight(500),
                                        color = Color(0xFFFFFFFF),

                                        textAlign = TextAlign.Center,
                                    ),
                                    modifier = Modifier.clickable {
                                        orderExecutionViewModel.getPrice()
                                    }
                                )
                            }
                        }
                        Box(modifier = Modifier
                            .border(width = 0.5.dp, color = Color(0xFFFFFFFF))

                            .padding(0.5.dp)
                            .width(0.dp)
                            .height(24.dp))
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
                            if(orderExecutionViewModel.stateEditOrder.value.isLoading){
                                CircularProgressIndicator(color = Color.White)
                            } else {
                                Text(
                                    text = "Готово",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight(500),
                                        color = Color(0xFFFFFFFF),
                                        textAlign = TextAlign.Center,
                                    ),
                                    modifier = Modifier.clickable {
                                        orderExecutionViewModel.editOrder(){
                                            orderExecutionViewModel.clearCalculate()
                                            Toast.makeText(context, "Заказ успешно изменен", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        CustomDialog(
            text = "Вы хотите изменить данный заказ?",
            okBtnClick = {
                coroutineScope.launch {
                    isDialogOpen.value = false

                }
            },
            cancelBtnClick = {
                coroutineScope.launch {
                    isDialogOpen.value = false
                }
            },
            isDialogOpen = isDialogOpen.value
        )
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
        number: List<Int>
    ) {
        val mainViewModel: MainViewModel = hiltViewModel()
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()

        var selectedButton by remember { mutableStateOf(0) }
        val switchON = remember { mutableStateOf(false) } // Initially the switch is ON

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp, end = 25.dp, bottom = 25.dp)
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
            if (switchON.value) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    items(number){
                        Row(Modifier.padding(start = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .wrapContentWidth()
                                    .wrapContentHeight()
                                    .background(
                                        if (selectedButton == it) Color.Black else Color.White,
                                        shape = RoundedCornerShape(20)
                                    )
                                    .clickable {
                                        selectedButton = it
                                        mainViewModel.stateAllowances.value.response?.forEach { it.price + number.lastIndex }
                                        orderExecutionViewModel.editOrder {

                                        }


                                    }
                                    .border(
                                        width = 1.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(20)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if(title== "С детьми") " $it " else "$it c",
                                    color = if (selectedButton == it) Color.White else Color.Black,
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
