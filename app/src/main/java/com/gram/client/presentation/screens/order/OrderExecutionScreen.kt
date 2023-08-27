package com.gram.client.presentation.screens.order

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.presentation.components.*
import com.gram.client.presentation.components.voyager.RatingScreen
import com.gram.client.presentation.components.voyager.reason.Reason1Screen
import com.gram.client.presentation.components.voyager.reason.Reason2Screen
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.main.SearchAddressScreen
import com.gram.client.presentation.screens.map.CustomMainMap
import com.gram.client.presentation.screens.order.components.*
import com.gram.client.utils.Constants.STATE_RATING
import com.gram.client.utils.Constants.STATE_RAITING_ORDER_ID
import com.gram.client.utils.Values
import kotlinx.coroutines.launch


class OrderExecutionScreen : Screen {
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val isGet = remember {
            mutableStateOf(true)
        }
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
        val mainViewModel: MainViewModel = hiltViewModel()
        val navigator = LocalNavigator.currentOrThrow
        val sheetState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
        )

        val coroutineScope = rememberCoroutineScope()



        val stateReasonsResponse = orderExecutionViewModel.stateGetReasons.value.response
        val reasonsCheck = remember { mutableStateOf("") }

        val isDialogOpen = remember { mutableStateOf(false) }

        val scope = rememberCoroutineScope()

        val ratingState = remember {
            mutableStateOf(0)
        }

        DisposableEffect(key1 = true ){
            orderExecutionViewModel.getRatingReasons()
            onDispose {
                orderExecutionViewModel.clearAddresses()
            }
        }
        if (Values.DriverLocation.value.latitude != 0.0) {
            Log.i("asdasda", "" + Values.DriverLocation.value)
        }

        val bottomNavigator = LocalBottomSheetNavigator.current
        val searchText = remember { mutableStateOf("") }
        var orderId by remember {
            mutableStateOf(-1)
        }

        val selectedOrder by orderExecutionViewModel.selectedOrder

        LaunchedEffect(key1 = true) {
            Log.e("ActiveOrdersResponse", selectedOrder.toString())
        }

        var stateCancelOrderText by remember {
            mutableStateOf("Вы уверены, что хотите отменить данный заказ?")
        }
            BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetBackgroundColor = MaterialTheme.colors.background,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(unbounded = true)
                        .background(MaterialTheme.colors.secondary)
                ) {

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
                                    //mainViewModel.updateFromAddress(it)
                                }
                            }
                            order.to_addresses.let {
                                it?.forEach { it2 ->
                                    //mainViewModel.updateToAddress(it2)
                                }
                            }
                        }

                        if (order.performer != null) {
                            performerSection(performer = order, orderExecutionViewModel)
                            stateCancelOrderText = "Водитель уже найден! Вы уверены, что все равно хотите отменить поездку?"
                        }
                        orderSection(order, scope)
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
                                bottomNavigator.show(Reason1Screen(orderExecutionViewModel, order))
                            }else{
                                bottomNavigator.show(Reason2Screen(orderExecutionViewModel, order){navigator.replace(SearchDriverScreen())})
                            }
                        })
                    }
                }
            },
            sheetPeekHeight = 210.dp,
        ) {
            Box {
                CustomMainMap(
                    mainViewModel = mainViewModel
                )
                if (stateCancelOrderText != "Вы не можете отменить активный заказ.\nЭто может сделать только оператор") {
                    if (isDialogOpen.value) {
                        Dialog(onDismissRequest = { isDialogOpen.value = false }) {
                            Column(modifier = Modifier.background(Color.White)) {
                                Text(text = "Подтверждение")
                                Column() {
                                    stateReasonsResponse?.result?.forEach {
                                        Row(verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { reasonsCheck.value = it.id.toString() }
                                                .padding(vertical = 5.dp, horizontal = 10.dp)) {
                                            CustomCheckBox(
                                                isChecked = reasonsCheck.value == it.id.toString(),
                                                onChecked = { reasonsCheck.value = it.id.toString() })
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text("" + it.name)
                                        }
                                    }
                                    Button(onClick = {
                                        coroutineScope.launch {
                                            isDialogOpen.value = false
                                            sheetState.bottomSheetState.collapse()

                                        }
                                    }, enabled = reasonsCheck.value != "") {
                                        Text("Отменить заказ")
                                    }
                                }
                            }
                        }
                    }
                } else {
                    CustomCancelDialog(
                        text = stateCancelOrderText,
                        okBtnClick = {
                            coroutineScope.launch {
                                isDialogOpen.value = false
                            }
                        },
                        isDialogOpen = isDialogOpen.value
                    )
                }
                if (STATE_RATING.value) {
                    bottomNavigator.show(RatingScreen(STATE_RAITING_ORDER_ID.value, orderExecutionViewModel.selectedOrder.value.price){
                        navigator.replaceAll(SearchAddressScreen())
                    })
                    STATE_RATING.value=false
                }
            }
        }
    }
}