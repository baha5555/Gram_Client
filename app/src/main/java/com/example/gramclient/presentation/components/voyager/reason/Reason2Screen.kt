package com.example.gramclient.presentation.components.voyager.reason

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.example.gramclient.domain.firebase.order.RealtimeDatabaseOrder
import com.example.gramclient.presentation.components.CustomCheckBox
import com.example.gramclient.presentation.screens.order.OrderExecutionViewModel

class Reason2Screen(
    val orderExecutionViewModel: OrderExecutionViewModel,
    val order: RealtimeDatabaseOrder
) : Screen {
    @Composable
    override fun Content() {
        val stateReasonsResponse = orderExecutionViewModel.stateGetReasons.value.response
        val reasonsCheck = remember { mutableStateOf("") }
        val bottomNavigator = LocalBottomSheetNavigator.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(
                "Выберите причину:",
                fontSize = 28.sp,
                modifier = Modifier.padding(vertical = 10.dp),
                fontWeight = FontWeight.Medium
            )
            stateReasonsResponse?.forEach {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { reasonsCheck.value = it.id.toString() }
                        .padding(vertical = 10.dp)) {
                    Text("" + it.name, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    CustomCheckBox(
                        isChecked = reasonsCheck.value == it.id.toString(),
                        onChecked = { reasonsCheck.value = it.id.toString() })
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    bottomNavigator.hide()
                    orderExecutionViewModel.cancelOrder(order.id, reasonsCheck.value) {
                        orderExecutionViewModel.stateCancelOrder.value.response.let {}
                    }
                },
                enabled = reasonsCheck.value != "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = ButtonDefaults.elevation(0.dp)
            ) {
                Text(text = "Готово", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

        }
    }
}