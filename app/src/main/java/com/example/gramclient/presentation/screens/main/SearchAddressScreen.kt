package com.example.gramclient.presentation.screens.main

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.gramclient.app.preference.CustomPreference
import com.example.gramclient.presentation.components.*
import com.example.gramclient.presentation.screens.main.components.AddressSearchBottomSheet
import com.example.gramclient.presentation.screens.main.components.FloatingButton
import com.example.gramclient.presentation.screens.main.components.FromAddressField
import com.example.gramclient.presentation.screens.map.CustomMainMap
import com.example.gramclient.presentation.screens.map.currentRoute
import com.example.gramclient.presentation.screens.map.mLocationOverlay
import com.example.gramclient.presentation.screens.map.map
import com.example.gramclient.presentation.screens.order.OrderExecutionViewModel
import com.example.gramclient.presentation.screens.order.SearchDriverScreen
import com.example.gramclient.presentation.screens.order.orderCount
import com.example.gramclient.ui.theme.PrimaryColor
import com.example.gramclient.utils.Constants
import com.example.gramclient.utils.Values
import kotlinx.coroutines.launch


class SearchAddressScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val mainViewModel: MainViewModel = hiltViewModel()
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
        val stateRealtimeDatabaseOrders by orderExecutionViewModel.stateRealtimeOrdersDatabase
        val stateRealtimeClientOrderIdDatabase by orderExecutionViewModel.stateRealtimeClientOrderIdDatabase
        val isSearchState = remember { mutableStateOf(false) }
        var sheetPeekHeight = remember { mutableStateOf(280) }

        var WHICH_ADDRESS = remember { mutableStateOf(Constants.TO_ADDRESS) }

        val bottomSheetState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
        )
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val focusRequester = remember { FocusRequester() }

        CustomBackHandle(drawerState.isClosed)

        LaunchedEffect(true) {
            mainViewModel.getActualLocation(context)
        }
        LaunchedEffect(bottomSheetState.bottomSheetState.currentValue) {
            if (bottomSheetState.bottomSheetState.isCollapsed) {
                isSearchState.value = false
                Log.e("singleTapConfirmedHelper", "isCollapsed")
            } else {
                Log.e("singleTapConfirmedHelper", "isExpanded")
            }
        }
        val toAddress = mainViewModel.toAddresses
        val fromAddress by mainViewModel.fromAddress
        val scope = rememberCoroutineScope()
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
                                if(Values.ClientOrders.value!=null){
                                    Box(modifier = Modifier.offset(25.dp, (-55).dp)){
                                        FloatingButton(
                                            bottomSheetState = bottomSheetState,
                                            Icons.Filled.ArrowBack,
                                            backgroundColor = Color.White,
                                            contentColor = PrimaryColor
                                        ){
                                            navigator.replaceAll(SearchDriverScreen())
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 25.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    FloatingButton(
                                        bottomSheetState = bottomSheetState,
                                        Icons.Filled.MyLocation
                                    ){
                                        map.controller.animateTo(mLocationOverlay.myLocation)
                                        if (mLocationOverlay.myLocation != null) {
                                            scope.launch {
                                                if (currentRoute == SearchAddressScreen().key) {
                                                    mainViewModel.getAddressFromMap(
                                                        mLocationOverlay.myLocation.longitude,
                                                        mLocationOverlay.myLocation.latitude,
                                                        WHICH_ADDRESS
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    FloatingButton(
                                        bottomSheetState = bottomSheetState,
                                        Icons.Filled.Menu
                                    ){
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }
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
                            sheetPeekHeight = sheetPeekHeight.value.dp-65.dp,
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