package com.gram.client.presentation.components.voyager.reason

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gram.client.domain.firebase.order.RealtimeDatabaseOrder
import com.gram.client.presentation.components.CustomCheckBox
import com.gram.client.presentation.components.voyager.CommentSheet
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.utils.Comments
import com.gram.client.utils.Values

class Reason2Screen(
    val orderExecutionViewModel: OrderExecutionViewModel,
    val order: RealtimeDatabaseOrder
) : Screen {
    private var reasonsCheck: MutableState<String> = mutableStateOf("")

    @Composable
    override fun Content() {
        val stateReasonsResponse = orderExecutionViewModel.stateGetReasons.value.response
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
            stateReasonsResponse?.result?.forEach {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { reasonsCheck.value = it.name }
                        .padding(vertical = 10.dp)) {
                    Text("" + it.name, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    CustomCheckBox(
                        isChecked = reasonsCheck.value == it.name,
                        onChecked = { reasonsCheck.value = it.name })
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFFF7F7F7))
                    .clickable {
                        bottomNavigator.push(CommentSheet("Ваш коментарий...", Comments.CANCEL))
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = if(Values.CommentCancelReasons.value=="") "Ваш коментарий..." else Values.CommentCancelReasons.value, fontSize = 18.sp, color = if(Values.CommentCancelReasons.value=="") Color(0xFFBEBEB5) else Color.Black, modifier = Modifier.padding(15.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    reasonsCheck.value+="\n"+Values.CommentCancelReasons.value
                    bottomNavigator.hide()
                    orderExecutionViewModel.cancelOrder(order.id, reasonsCheck.value) {
                        orderExecutionViewModel.stateCancelOrder.value.response.let {}
                    }
                    Values.CommentCancelReasons.value=""
                    reasonsCheck.value=""
                },
                enabled = reasonsCheck.value != "" || Values.CommentCancelReasons.value!="",
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