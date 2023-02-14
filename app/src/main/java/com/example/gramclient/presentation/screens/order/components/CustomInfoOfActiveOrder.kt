package com.example.gramclient.presentation.screens.order.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gramclient.domain.mainScreen.Address

@Composable
fun CustomInfoOfActiveOrder(
    performerName:String,
    performerPhone:String,
    transportModel:String,
    transportNumber: String,
    transportColor: String,
    toAddress: List<Address>,
    fromAddress: String,
    orderTime:String,
    tariffName:String,
    tariffPrice:String,
    paymentMethod:String
) {
    var symbol by remember { mutableStateOf(65) }
    Column(modifier=Modifier.fillMaxHeight(0.95f)) {
        Box(modifier =Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            Text(text = "Детали заказа", fontWeight = FontWeight.Bold)
        }
        LazyColumn() {
            item {
                CustomInfoTitle(title = "Маршрут")
                CustomAddressText(title = fromAddress, point = "${symbol.toChar()}")
                for (i in toAddress.indices) {
                    CustomAddressText(title = toAddress[i].address, point = "${(symbol + i + 1).toChar()}")
                }
                CustomInfoTitle(title = "Время")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp, horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = orderTime)
                }
                CustomInfoTitle(title = "Тариф")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp, horizontal = 10.dp)
                ) {
                    Text(text = tariffName, modifier = Modifier.padding(bottom = 15.dp))
                    Divider()
                    Text(text = tariffPrice+"сомони")
                }
                CustomInfoTitle(title = "Способ оплаты")
                Row (modifier= Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = paymentMethod)
                }
                CustomInfoTitle(title = "Водитель")
            }
        }
    }
}
@Composable
fun CustomAddressText(
    title:String,
    point:String
){
    Row (modifier= Modifier
        .fillMaxWidth()
        .padding(vertical = 15.dp, horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically){
        Text(text = point, color = Color(0xFB8B8B8B))
        Text(text = title, modifier = Modifier
            .padding(start = 10.dp)
            .fillMaxWidth(0.8f))
    }
}

@Composable
fun CustomInfoTitle(title:String){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFFB8B8B8))
        .padding(vertical = 15.dp, horizontal = 10.dp)){
        Text(text = title)
    }
}