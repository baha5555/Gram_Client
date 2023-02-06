package com.example.gramclient.presentation.screens.order

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.gramclient.R
import com.example.gramclient.domain.firebase.order.RealtimeDatabaseOrder
import com.example.gramclient.presentation.components.*
import com.example.gramclient.presentation.screens.main.MainViewModel
import com.example.gramclient.presentation.screens.map.CustomMainMap
import com.example.gramclient.presentation.screens.order.components.*
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.PrimaryColor
import com.example.gramclient.utils.Constants.STATE_RAITING
import com.example.gramclient.utils.Constants.STATE_RAITING_ORDER_ID
import com.example.gramclient.utils.Values
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class OrderExecutionScreen : Screen{
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
        val mainViewModel: MainViewModel = hiltViewModel()
        val navigator = LocalNavigator.currentOrThrow
        val sheetState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
        )

        val coroutineScope = rememberCoroutineScope()

        val stateRealtimeDatabaseOrders = orderExecutionViewModel.stateRealtimeOrdersDatabase.value.response?.observeAsState()?.value

        val isDialogOpen = remember { mutableStateOf(false) }

        val scope = rememberCoroutineScope()

        val ratingState = remember {
            mutableStateOf(0)
        }

        LaunchedEffect(key1 = true){
            orderExecutionViewModel.readAllOrders()
            //orderExecutionViewModel.getDriverLocation(1480)
        }
        if(Values.DriverLocation.value.latitude!=0.0){
            Log.i("asdasda", ""+Values.DriverLocation.value)
        }

        var selectRealtimeDatabaseOrder:RealtimeDatabaseOrder by remember {
            mutableStateOf(RealtimeDatabaseOrder())
        }
        // searchState dependencies ->
        var WHICH_ADDRESS = remember{ mutableStateOf("") }
        val isAddressList= remember { mutableStateOf(true) }
        val searchText=remember{ mutableStateOf("") }
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
            stateRealtimeDatabaseOrders.let { orders ->
                orders?.forEach { order ->
                    if (order.id == selectedOrder.id) {
                        Log.e("Select Order", "$selectRealtimeDatabaseOrder")
                        selectRealtimeDatabaseOrder = order
                    }
                }
            }

            selectRealtimeDatabaseOrder.from_address?.let {
                mainViewModel.updateFromAddress(it)
                Log.e("From_address","$it")
            }

            selectRealtimeDatabaseOrder.to_address?.let { to_Addresses->
                to_Addresses.forEach { address->
                    mainViewModel.updateToAddress(address)
                    Log.e("To_address","$address")
                }
            }
        }
        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetBackgroundColor = Color(0xFFffffff),
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(unbounded = true)
                        .background(BackgroundColor)
                ) {
                    if(!isSearchState.value) {
                        scope.launch{
                            if(searchText.value != "")
                                searchText.value = ""
                        }
                        selectRealtimeDatabaseOrder.let { order ->
                            if(order.id!=-1) orderExecutionViewModel.getDriverLocation(order.id)

                            if (order.performer != null) {
                                performerSection(performer = order, orderExecutionViewModel)
                                stateCancelOrderText = "Водитель уже найден! Вы уверены, что все равно хотите отменить поездку?"
                            }
                            orderSection(order, scope, sheetState, isSearchState)
                            Spacer(modifier = Modifier.height(10.dp))
                            optionSection()
                            actionSection(cancelOrderOnClick = {
                                isDialogOpen.value = true
                                orderId = order.id
                                if(order.status == "Исполняется"){
                                    stateCancelOrderText = "Вы не можете отменить активный заказ.\nЭто может сделать только оператор"
                                }
                            })
                        }
                    }else{
                        searchSection(
                            searchText, focusRequester, isSearchState, sheetState, scope, orderExecutionViewModel, isAddressList, focusManager, mainViewModel
                        )
                    }
                }
            },
            sheetPeekHeight = 210.dp,
        ) {
            Box {
                CustomMainMap(
                    mainViewModel = mainViewModel)
                if(stateCancelOrderText!="Вы не можете отменить активный заказ.\nЭто может сделать только оператор") {
                    CustomDialog(
                        text = stateCancelOrderText,
                        okBtnClick = {
                            coroutineScope.launch {
                                isDialogOpen.value = false
                                sheetState.bottomSheetState.collapse()
                                if (orderId != -1)
                                    orderExecutionViewModel.cancelOrder(orderId) {
                                        navigator.pop()
                                    }
                            }
                        },
                        cancelBtnClick = { isDialogOpen.value = false },
                        isDialogOpen = isDialogOpen.value
                    )
                }
                else {
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