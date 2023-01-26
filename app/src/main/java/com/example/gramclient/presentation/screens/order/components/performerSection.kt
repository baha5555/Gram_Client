package com.example.gramclient.presentation.screens.order.components

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramclient.R
import com.example.gramclient.domain.realtimeDatabase.Order.RealtimeDatabaseOrder
import com.example.gramclient.presentation.components.CustomCircleButton
import com.example.gramclient.presentation.components.CustomDialog
import com.example.gramclient.presentation.screens.order.OrderExecutionViewModel
import com.example.gramclient.ui.theme.FontSilver
import com.example.gramclient.utils.Constants
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


@SuppressLint("CoroutineCreationDuringComposition", "SimpleDateFormat")
@Composable
fun performerSection(
    performer: RealtimeDatabaseOrder,
    orderExecutionViewModel: OrderExecutionViewModel
){
    val context = LocalContext.current

    val connectClientWithDriverIsDialogOpen = remember{ mutableStateOf(false) }

    val DateformatParse = SimpleDateFormat("yyyy-MM-dd HH:mm")

    var fillingTimeDateParse: Long by remember{
        mutableStateOf(0)
    }

    var diff:Long by remember{
        mutableStateOf(0)
    }

    var fillingTimeMinutes:Long by remember{
        mutableStateOf(0)
    }
    val scope = rememberCoroutineScope()
    scope.launch {
        performer.filing_time?.let {

            fillingTimeDateParse = DateformatParse.parse(it).time

            diff = (System.currentTimeMillis()-fillingTimeDateParse)*-1

            fillingTimeMinutes = diff / (60 * 1000) % 60

            Log.e(Constants.TAG,"fillingTimeMinutes $fillingTimeMinutes")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                color = Color.White
            )
            .padding(20.dp)
    ){
        Text(
            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
            text = when(performer.status){
                "Водитель на месте"->"Водитель на месте,\n можете выходить"
                "Исполняется"->"За рулем ${performer.performer?.first_name?:"Водитель"}"
                "Водитель назначен"->{
                    if(fillingTimeMinutes>0)"Через $fillingTimeMinutes мин приедет ${performer.performer?.first_name}"
                    else "Скоро приедет ${performer.performer?.first_name}"
                }
                else -> {"Вы завершили поездку"}
            }, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
        ){
            Text(text = "${performer.performer?.transport?.color?:"Не указан"} ${performer.performer?.transport?.model?:"Не указан"}", fontSize = 16.sp, color = Color.Black)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                modifier = Modifier
                    .background(shape = RoundedCornerShape(3.dp), color = Color(0xFFF4B91D))
                    .padding(3.dp), textAlign = TextAlign.Center,
                text = performer.performer?.transport?.car_number?:"номер не указан", fontSize = 16.sp, color = Color.Black)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.width(80.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    "",
                    modifier = Modifier.size(55.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${performer.performer?.first_name}",
                    color = FontSilver,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Spacer(modifier = Modifier.width(20.dp))
            CustomCircleButton(text = "Связаться", icon = R.drawable.phone) {
                connectClientWithDriverIsDialogOpen.value = true
            }
        }
    }
    CustomDialog(
        text = "Позвонить водителю?",
        okBtnClick = {
            connectClientWithDriverIsDialogOpen.value = false
            orderExecutionViewModel.connectClientWithDriver(
                order_id = performer.id.toString()
            ) {
                Toast.makeText(context, "Ваш запрос принят.Ждите звонка.", Toast.LENGTH_SHORT).show()
            }
        },
        cancelBtnClick = { connectClientWithDriverIsDialogOpen.value = false },
        isDialogOpen = connectClientWithDriverIsDialogOpen.value
    )
}
