package com.example.gramclient.presentation.screens.main

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import com.example.gramclient.utils.Constants
import com.example.gramclient.presentation.components.*
import com.example.gramclient.presentation.screens.main.components.AddressSearchBottomSheet
import com.example.gramclient.presentation.screens.main.components.FloatingButton
import com.example.gramclient.presentation.screens.main.components.FloatingButton1
import com.example.gramclient.presentation.screens.main.components.FromAddressField
import com.example.gramclient.presentation.screens.order.OrderExecutionViewModel
import com.example.gramclient.presentation.screens.order.orderCount
import com.example.gramclient.presentation.screens.profile.ProfileViewModel
import com.example.gramclient.presentation.screens.map.CustomMainMap
import kotlinx.coroutines.launch


class SearchAddressScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val mainViewModel: MainViewModel = hiltViewModel()
        var isSearchState = remember { mutableStateOf(false) }
        var sheetPeekHeight = remember { mutableStateOf(280) }

        var WHICH_ADDRESS = remember { mutableStateOf(Constants.TO_ADDRESS) }

        val bottomSheetState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
        )
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        var initialApiCalled by rememberSaveable { mutableStateOf(false) }

        val focusRequester = remember { FocusRequester() }

        CustomBackHandle(drawerState.isClosed)

        if (!initialApiCalled) {
            LaunchedEffect(Unit) {
                mainViewModel.getActualLocation(context)
                initialApiCalled = true
            }
        }
        LaunchedEffect(bottomSheetState.bottomSheetState.currentValue) {
            if (bottomSheetState.bottomSheetState.isCollapsed) {
                isSearchState.value = false
                Log.e("singleTapConfirmedHelper", "isCollapsed")
            } else {
                Log.e("singleTapConfirmedHelper", "isExpanded")
            }
        }
        val toAddress by mainViewModel.toAddress
        val fromAddress by mainViewModel.fromAddress
        val scope = rememberCoroutineScope()
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()

        val stateRealtimeDatabaseOrders by orderExecutionViewModel.stateRealtimeOrdersDatabase
        val stateRealtimeClientOrderIdDatabase by orderExecutionViewModel.stateRealtimeClientOrderIdDatabase
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            BackHandler(enabled = drawerState.isOpen) {
                scope.launch { drawerState.close() }
            }
            ModalDrawer(
                drawerState = drawerState,
                gesturesEnabled = !drawerState.isClosed,
                drawerContent = {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            SideBarMenu()
                        }
                    }
                },
                content = {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        BottomSheetScaffold(
                            modifier = Modifier.fillMaxSize(),
                            floatingActionButton = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 80.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {


                                    stateRealtimeDatabaseOrders.response?.let { response ->
                                        response.observeAsState().value?.let { orders ->
                                            orderCount.value = orders.size
                                            stateRealtimeClientOrderIdDatabase.response?.let { responseClientOrderId ->
                                                responseClientOrderId.observeAsState().value?.let { clientOrdersId ->
                                                    if (clientOrdersId.active_orders != null) {
                                                        FloatingButton1(
                                                            scope = coroutineScope,
                                                            drawerState = drawerState,
                                                            bottomSheetState = bottomSheetState
                                                        )
                                                    }

                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    FloatingButton(
                                        scope = coroutineScope,
                                        drawerState = drawerState,
                                        bottomSheetState = bottomSheetState
                                    )
                                }

                            },
                            drawerGesturesEnabled = false,
                            sheetBackgroundColor = Color.White,
                            scaffoldState = bottomSheetState,
                            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                            sheetGesturesEnabled = !bottomSheetState.bottomSheetState.isCollapsed,
                            sheetContent = {
                                AddressSearchBottomSheet(
                                    isSearchState = isSearchState,
                                    mainViewModel = mainViewModel,
                                    bottomSheetState = bottomSheetState,
                                    focusRequester = focusRequester,
                                    coroutineScope = coroutineScope,
                                    WHICH_ADDRESS = WHICH_ADDRESS,
                                    toAddress = toAddress
                                )
                            },
                            sheetPeekHeight = sheetPeekHeight.value.dp,
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.TopCenter
                            )
                            {
                                CustomMainMap(
                                    mainViewModel = mainViewModel,
                                    WHICH_ADDRESS = WHICH_ADDRESS
                                )
                                FromAddressField(fromAddress) {
                                    coroutineScope.launch {
                                        bottomSheetState.bottomSheetState.expand()
                                    }
                                    isSearchState.value = true
                                    WHICH_ADDRESS.value = Constants.FROM_ADDRESS
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}