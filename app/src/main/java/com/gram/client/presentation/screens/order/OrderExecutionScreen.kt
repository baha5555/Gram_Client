package com.gram.client.presentation.screens.order

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.domain.firebase.order.RealtimeDatabaseOrder
import com.gram.client.presentation.components.*
import com.gram.client.presentation.components.voyager.reason.Reason1Screen
import com.gram.client.presentation.components.voyager.reason.Reason2Screen
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.main.SearchAddressScreen
import com.gram.client.presentation.screens.map.CustomMainMap
import com.gram.client.presentation.screens.order.components.*
import com.gram.client.ui.theme.PrimaryColor
import com.gram.client.utils.Constants.STATE_RAITING
import com.gram.client.utils.Constants.STATE_RAITING_ORDER_ID
import com.gram.client.utils.Values
import kotlinx.coroutines.delay
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

        val stateRealtimeDatabaseOrders =
            orderExecutionViewModel.stateRealtimeOrdersDatabase.value.response?.observeAsState()?.value

        val stateReasonsResponse = orderExecutionViewModel.stateGetReasons.value.response
        val reasonsCheck = remember { mutableStateOf("") }

        val isDialogOpen = remember { mutableStateOf(false) }

        val scope = rememberCoroutineScope()

        val ratingState = remember {
            mutableStateOf(0)
        }

        DisposableEffect(key1 = true ){
            onDispose {
                orderExecutionViewModel.clearAddresses()
            }
        }
        if (Values.DriverLocation.value.latitude != 0.0) {
            Log.i("asdasda", "" + Values.DriverLocation.value)
        }

        var selectRealtimeDatabaseOrder: RealtimeDatabaseOrder by remember {
            mutableStateOf(RealtimeDatabaseOrder())
        }
        val bottomNavigator = LocalBottomSheetNavigator.current
        // searchState dependencies ->
        var WHICH_ADDRESS = remember { mutableStateOf("") }
        val isAddressList = remember { mutableStateOf(true) }
        val searchText = remember { mutableStateOf("") }
        val focusManager = LocalFocusManager.current
        val isSearchState = remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }
        var orderId by remember {
            mutableStateOf(-1)
        }
        //searchState dependencies <-

        val selectedOrder by orderExecutionViewModel.selectedOrder

        LaunchedEffect(key1 = true) {
            Log.e("ActiveOrdersResponse", selectedOrder.toString())
        }

        var stateCancelOrderText by remember {
            mutableStateOf("Вы уверены, что хотите отменить данный заказ?")
        }
        scope.launch {
            Log.e("runRe", "get")
            stateRealtimeDatabaseOrders.let { orders ->
                orders?.forEach { order ->
                    if (order.id == selectedOrder.id) {
                        Log.e("Select Order", "$selectRealtimeDatabaseOrder")
                        selectRealtimeDatabaseOrder = order
                    }
                }
            }

            selectRealtimeDatabaseOrder.from_address?.let {
                if (it != mainViewModel.fromAddress.value) {
                    orderExecutionViewModel.updateFromAddress(it)
                }
                Log.e("From_address-1", "$it")
            }

            selectRealtimeDatabaseOrder.to_address?.let { to_Addresses ->
                orderExecutionViewModel.updateToAddress(clear = true)
                if (to_Addresses.toMutableStateList() != mainViewModel.toAddresses) {
                    //orderExecutionViewModel.updateToAddress(to_Addresses.toMutableStateList())
                    to_Addresses.toMutableStateList().forEach {
                        orderExecutionViewModel.updateToAddress(it)
                    }
                }
                Log.e("From_address-1", ""+to_Addresses.toMutableStateList().size)
            }
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
                    if (!isSearchState.value) {
                        scope.launch {
                            if (searchText.value != "")
                                searchText.value = ""
                        }
                        selectRealtimeDatabaseOrder.let { order ->
                            Log.i("orderAddresses", "" + order.from_address)
                            if (order.id != -1) orderExecutionViewModel.getDriverLocation(order.id)
                            if (isGet.value) {
                                order.from_address.let {
                                    if (it != null) {
                                        isGet.value = false
                                        //mainViewModel.updateFromAddress(it)
                                    }
                                }
                                order.to_address.let {
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
                                    bottomNavigator.show(Reason2Screen(orderExecutionViewModel, order))
                                }
                            })
                        }
                    } else {
                        searchSection(
                            searchText,
                            focusRequester,
                            isSearchState,
                            sheetState,
                            scope,
                            orderExecutionViewModel,
                            isAddressList,
                            focusManager,
                            mainViewModel
                        )
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
                                    stateReasonsResponse?.forEach {
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
                                            if (orderId != -1)
                                                orderExecutionViewModel.cancelOrder(orderId, reasonsCheck.value) {
                                                    orderExecutionViewModel.stateCancelOrder.value.response.let {
                                                        if(it == null ) return@cancelOrder
                                                        if(it.result[0].count==0){
                                                            navigator.replaceAll(SearchAddressScreen())
                                                        }
                                                        else{
                                                            navigator.pop()
                                                        }
                                                    }
                                                }
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
                if (STATE_RAITING.value) {
                    var thumbUpClicked by remember {
                        mutableStateOf(false)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .offset(0.dp, 100.dp)
                            .padding(horizontal = 50.dp)
                            .clip(
                                RoundedCornerShape(30.dp)
                            )
                            .background(PrimaryColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                        ) {
                            if (!thumbUpClicked) {
                                Text(
                                    text = "Оцените поездку",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    modifier = Modifier.offset(10.dp, (-8).dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    repeat(5) {
                                        Box(modifier = Modifier.clickable(indication = null,
                                            interactionSource = remember { MutableInteractionSource() }) {
                                            ratingState.value = it + 1
                                            scope.launch {
                                                delay(3000)
                                                thumbUpClicked = true
                                                orderExecutionViewModel.sendRating2(
                                                    order_id = STATE_RAITING_ORDER_ID.value,
                                                    add_rating = ratingState.value * 10
                                                )
                                                Log.d("balll", "" + ratingState.value * 10)
                                            }

                                        }) {
                                            if (it < ratingState.value) Image(
                                                imageVector = ImageVector.vectorResource(
                                                    id = R.drawable.ic_default_star
                                                ),
                                                contentDescription = null,
                                            )
                                            else Image(
                                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_outlined_star),
                                                contentDescription = null,
                                            )
                                        }

                                    }
                                }
                            } else {
                                ratingState.value = 0
                                scope.launch {
                                    delay(3000)
                                    STATE_RAITING.value = false
                                    navigator.pop()
                                }
                                Text(
                                    text = "Спасибо за ваше участие \nв улучшении качества работы!",
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}