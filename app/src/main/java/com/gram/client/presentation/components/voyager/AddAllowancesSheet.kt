package com.gram.client.presentation.components.voyager

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gram.client.presentation.components.CustomBigButton
import com.gram.client.presentation.components.CustomSwitch
import com.gram.client.presentation.screens.main.MainViewModel
import com.valentinilk.shimmer.shimmer

class AddAllowancesSheet(val onClick:()->Unit) : Screen {
    @Composable
    override fun Content() {
        val mainViewModel: MainViewModel = hiltViewModel()

        val bottomNavigator = LocalBottomSheetNavigator.current
        Column(modifier = Modifier.fillMaxHeight(0.75f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color(0xFFEEEEEE)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Добавить надбавки", color = Color.Black, fontSize = 18.sp)
            }
            AllowancesContent(
                modifier = Modifier.weight(1f)
            )
            CustomBigButton(text = "Готово") {
                mainViewModel.getPrice()
                bottomNavigator.hide()
                onClick()
            }
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun AllowancesContent(
        modifier: Modifier
    ) {
        val mainViewModel: MainViewModel = hiltViewModel()
        val stateAllowances = mainViewModel.stateAllowances.value
        Log.e("надбавки","${mainViewModel.stateAllowances.value}")
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .background(
                    MaterialTheme.colors.background,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(horizontal = 20.dp, vertical = 15.dp)
        ) {
            stateAllowances.response?.let { allowances ->
                if (allowances.isNotEmpty()) {
                    allowances.forEach { allowance ->

                        val selectedPropertyInx = remember {
                            mutableStateOf(0)
                        }
                        val selectedPropertyPrice = remember {
                            mutableStateOf(0)
                        }
                        mainViewModel.selectedAllowances.value?.forEach {allowanceRequest ->
                            allowance.price_property?.forEachIndexed { inx, it ->
                                if(allowanceRequest.value == it){
                                    Log.d("PriceMy", ""+allowanceRequest.value
                                    )
                                    selectedPropertyInx.value = inx
                                    selectedPropertyPrice.value = it
                                }
                            }
                        }
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(0.6f),
                                    text = allowance.name,
                                    fontSize = 16.sp
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if(allowance.price_property == null){
                                        Text(
                                            text = " (${allowance.price}" + when (allowance.type) {
                                                "fix" -> "c"
                                                "percent" -> "%"
                                                "minute" -> "мин"
                                                else -> {
                                                    ""
                                                }
                                            } + ")",
                                            fontSize = 16.sp,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(end = 18.dp)
                                        )
                                    }
                                    CustomSwitch(switchON = allowance.isSelected) {
                                        if(allowance.price_property!=null){
                                            mainViewModel.includeAllowance(allowance, price = selectedPropertyPrice.value)
                                        } else {
                                            mainViewModel.includeAllowance(allowance)
                                        }
                                    }
                                }

                            }
                            if (allowance.price_property != null) {

                                LazyRow(contentPadding = PaddingValues(2.dp), modifier = Modifier.padding(start = 15.dp).alpha(alpha = if (allowance.isSelected.value) 1.0F else 0.5F)) {
                                    items( allowance.price_property.size) {
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    if (selectedPropertyInx.value == it) Color.Black else Color.White,
                                                    shape = RoundedCornerShape(20)
                                                )
                                                .border(
                                                    width = 1.dp,
                                                    color = Color.Black,
                                                    shape = RoundedCornerShape(20)

                                                )
                                                .height(30.dp)
                                                .clickable {
                                                    if(allowance.isSelected.value){
                                                        selectedPropertyInx.value = it
                                                        selectedPropertyPrice.value =  allowance.price_property[it]
                                                        mainViewModel.includeAllowance(allowance, price = selectedPropertyPrice.value)
                                                    }
                                                },
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Text(
                                                text = "" +  allowance.price_property[it] + "c",
                                                modifier = Modifier.padding(horizontal = 11.5.dp),
                                                color = if (selectedPropertyInx.value != it) Color.Black else Color.White
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(15.dp))
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        if (stateAllowances.isLoading) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(10.dp)
            ) {
                repeat(5) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shimmer(), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(15.dp)
                                .size(20.dp)
                                .clip(RoundedCornerShape(100))
                                .background(Color.Gray)
                        )
                        Column {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .height(18.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(100))
                                    .background(Color(0xFFAAAAAA))
                            )
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .height(12.dp)
                                    .fillMaxWidth(0.8f)
                                    .clip(RoundedCornerShape(100))
                                    .background(Color(0xFFAAAAAA))
                            )
                        }
                    }
                    Divider(Modifier.padding(vertical = 10.dp))
                }
            }
        }
    }
}