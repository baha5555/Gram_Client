package com.gram.client.presentation.components.voyager

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gram.client.presentation.components.CustomBigButton
import com.gram.client.presentation.components.CustomLinearShimmer
import com.gram.client.presentation.components.CustomSwitch
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.main.states.AllowancesResponseState

class AddAllowancesSheet(
    var mainViewModel: MainViewModel,
    var stateAllowances: AllowancesResponseState
) : Screen {
    @Composable
    override fun Content() {
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
                stateAllowances = stateAllowances,
                mainViewModel = mainViewModel,
                modifier = Modifier.weight(1f)
            )
            CustomBigButton(text = "Готово") {
                mainViewModel.getPrice()
                bottomNavigator.hide()
            }
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun AllowancesContent(
        stateAllowances: AllowancesResponseState,
        mainViewModel: MainViewModel,
        modifier: Modifier
    ) {
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
                if (allowances.size != 0) {
                    allowances.forEach { allowance ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(0.7f),
                                text = allowance.name,
                                fontSize = 16.sp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
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
                                CustomSwitch(switchON = allowance.isSelected) {
                                    mainViewModel.includeAllowance(allowance)
                                }
                            }
                        }
                    }
                }
            }
        }
        if (stateAllowances.response == null || stateAllowances.error != "") {
            CustomLinearShimmer(enabled = true)
        }
    }
}