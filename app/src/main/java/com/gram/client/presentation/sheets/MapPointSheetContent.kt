package com.gram.client.presentation.sheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gram.client.R
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.utils.Constants
import com.gram.client.utils.Values
import com.gram.client.utils.getAddressText
import com.valentinilk.shimmer.shimmer

@Composable
fun MapPointSheetContent(mainViewModel: MainViewModel, stateViews:Boolean = false, onClick: () -> Unit) {
    val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
    val statePoint = if(!stateViews) mainViewModel.stateAddressPoint.value else orderExecutionViewModel.stateAddressPoint.value
    Column(
        modifier = Modifier
            .height(200.dp)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Text(
                text = if(Values.WhichAddress.value == Constants.FROM_ADDRESS)"Точка назначения" else "Точка отправления",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
            Divider(modifier = Modifier.padding(vertical = 10.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .size(20.dp),
                imageVector = ImageVector.vectorResource(if(Values.WhichAddress.value == Constants.FROM_ADDRESS) R.drawable.from_marker else R.drawable.ic_to_address_marker),
                contentDescription = "Logo"
            )
            if(statePoint.isLoading){
                Box(modifier = Modifier
                    .shimmer()
                    .padding(start = 10.dp)){
                    Box(modifier = Modifier
                        .size(150.dp, 20.dp)
                        .background(Color.Gray))
                }
            }else{
                Text(text = statePoint.response?.let { getAddressText(it) } ?: "Метка на карте", fontSize = 18.sp, modifier = Modifier.padding(start = 10.dp))
            }
        }
        Button(
            onClick = {
                onClick.invoke()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = "Готово", fontSize = 20.sp)
        }
    }
}