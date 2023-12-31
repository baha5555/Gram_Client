package com.gram.client.presentation.sheets

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.presentation.components.voyager.reason.Reason2Screen
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.presentation.screens.order.components.CustomInfoOfActiveOrder
import com.gram.client.presentation.screens.order.components.actionSection
import com.gram.client.presentation.screens.order.components.optionSection
import com.gram.client.presentation.screens.order.components.orderSection
import com.gram.client.presentation.screens.order.components.performerSection
import com.gram.client.utils.Routes
import com.gram.client.utils.Values
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DetailActiveOrderSheetContent(
    orderExecutionViewModel: OrderExecutionViewModel,
    navController: NavHostController,
) {
    val isGet = remember {
        mutableStateOf(true)
    }
    val navigator = LocalNavigator.currentOrThrow
    val scope = rememberCoroutineScope()


    DisposableEffect(key1 = true ){
        orderExecutionViewModel.clearToAddress()
        orderExecutionViewModel.getRatingReasons()
        orderExecutionViewModel.selectedOrder.value.let { order ->
            order.from_address.let {
                if (it != null) {
                    isGet.value = false
                    orderExecutionViewModel.updateFromAddress(it)
                }
            }
            order.to_addresses.let {
                it?.forEach { it2 ->
                    orderExecutionViewModel.updateToAddress(it2)
                }
            }
        }
        scope.launch {
            orderExecutionViewModel.showRoad()
        }
        onDispose {
            orderExecutionViewModel.clearAddresses()
        }
    }
    if (Values.DriverLocation.value.latitude != 0.0) {
        Log.i("asdasda", "" + Values.DriverLocation.value)
    }

    val bottomNavigator = LocalBottomSheetNavigator.current
    val searchText = remember { mutableStateOf("") }

    val selectedOrder by orderExecutionViewModel.selectedOrder

    LaunchedEffect(key1 = true) {
        Log.e("ActiveOrdersResponse", selectedOrder.toString())
    }

    var stateCancelOrderText = remember {
        mutableStateOf("Вы уверены, что хотите отменить данный заказ?")
    }
    var orderId by remember {
        mutableStateOf(-1)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(unbounded = true)
            .background(MaterialTheme.colors.secondary)
    ) {
        val bottomNavigator = LocalBottomSheetNavigator.current

        scope.launch {
            if (searchText.value != "")
                searchText.value = ""
        }
        orderExecutionViewModel.selectedOrder.value.let { order ->
            Log.i("orderAddresses", "" + order)
            if (order.id != -1) orderExecutionViewModel.getDriverLocation(order.id)
            if (isGet.value) {
                order.from_address.let {
                    if (it != null) {
                        isGet.value = false
                        orderExecutionViewModel.updateFromAddress(it)
                    }
                }
                order.to_addresses.let {
                    it?.forEach { it2 ->
                        orderExecutionViewModel.updateToAddress(it2)
                    }
                }
            }

            if (order.performer != null) {
                performerSection(performer = order, orderExecutionViewModel)
                stateCancelOrderText.value = "Водитель уже найден! Вы уверены, что все равно хотите отменить поездку?"
            }
            orderSection(order, scope, navController, orderExecutionViewModel)
            Spacer(modifier = Modifier.height(10.dp))
            optionSection(onClick = {
                navigator.push(CustomInfoOfActiveOrder())
            })
            val context = LocalContext.current
            actionSection(cancelOrderOnClick = {
                orderId = order.id
                if (order.status == "Исполняется" || order.status == "Водитель на месте") {
                    Toast.makeText(context, "Вы не можете отменить активный заказ.\nЭто может сделать только оператор", Toast.LENGTH_LONG).show()
                    return@actionSection
                }
                if (order.performer != null) {
                    bottomNavigator.show(Reason2Screen(orderExecutionViewModel, order){
                        //navigator.push(SearchDriverScreen())
                        navController.navigate(Routes.SEARCH_ADDRESS_SHEET){
                            popUpTo(Routes.DETAIL_ACTIVE_ORDER_SHEET){
                                inclusive = true
                            }
                            popUpTo(Routes.SEARCH_DRIVER_SHEET){
                                inclusive = true
                            }
                        }
                    })
                }else{
                    bottomNavigator.show(Reason2Screen(orderExecutionViewModel, order){
                        //navigator.push(SearchDriverScreen())
                        navController.popBackStack()
                    })
                }
            })
        }
    }
}