package com.gram.client.presentation.components.voyager.reason

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.domain.firebase.order.RealtimeDatabaseOrder
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.presentation.screens.order.SearchDriverScreen

class Reason1Screen(
    val orderExecutionViewModel: OrderExecutionViewModel,
    val order: RealtimeDatabaseOrder
) : Screen {
    @Composable
    override fun Content() {
        val bottomNavigator = LocalBottomSheetNavigator.current
        val navigator = LocalNavigator.currentOrThrow
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(
                "Водитель уже едет",
                fontSize = 28.sp,
                modifier = Modifier.padding(vertical = 10.dp),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Водитель изменил свой маршрут и уже проехал часть пути к вам. Вы уверены, \n" +
                        "что хотите отменить поездку?", fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(35.dp))
            Button(
                onClick = {
                    bottomNavigator.replace(Reason2Screen(orderExecutionViewModel, order) {navigator.replace(SearchDriverScreen())})
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFF0F1EC),
                    contentColor = Color.Black
                ),
                elevation = ButtonDefaults.elevation(0.dp)
            ) {
                Text(text = "Отменить поездку", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    bottomNavigator.hide()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = ButtonDefaults.elevation(0.dp)
            ) {
                Text(text = "Дождусь водителя", fontSize = 16.sp)
            }
        }
    }
}