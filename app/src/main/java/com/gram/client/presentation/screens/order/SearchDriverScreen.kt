package com.gram.client.presentation.screens.order

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.app.preference.CustomPreference
import com.gram.client.presentation.components.*
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.main.SearchAddressScreen
import com.gram.client.presentation.screens.main.components.FloatingButton2
import com.gram.client.presentation.screens.map.CustomMainMap
import com.gram.client.presentation.screens.map.currentRoute
import com.gram.client.presentation.screens.map.mLocationOverlay
import com.gram.client.presentation.screens.map.map
import com.gram.client.ui.theme.PrimaryColor
import kotlinx.coroutines.launch
import java.util.*

val orderCount = mutableStateOf(-1)

class SearchDriverScreen : Screen {
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val context = LocalContext.current
        val prefs = CustomPreference(context)
        val navigator = LocalNavigator.currentOrThrow
        val mainViewModel: MainViewModel = hiltViewModel()
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(
                initialValue = BottomSheetValue.Expanded
            )
        )
        val bottomSheetState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
        )
        val scope = rememberCoroutineScope()
        var sheetPeekHeight by remember {
            mutableStateOf(200)
        }
        LaunchedEffect(key1 = orderExecutionViewModel.stateActiveOrders.value.response?.size == 0) {
            orderExecutionViewModel.getActiveOrders() {
                if (orderExecutionViewModel.stateActiveOrders.value.response?.isEmpty() == true && orderExecutionViewModel.stateActiveOrders.value.code == 200) {
                    navigator.replaceAll(SearchAddressScreen())
                }
            }
        }
        CustomBackHandle(true)
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
                            sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
                            scaffoldState = bottomSheetScaffoldState,
                            floatingActionButton = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 25.dp, bottom = 50.dp),
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        FloatingButton2(
                                            ImageVector.vectorResource(id = R.drawable.btn_show_location),
                                            backgroundColor = Color.White,
                                            contentColor = PrimaryColor
                                        ) {
                                            map.controller.animateTo(mLocationOverlay.myLocation)
                                            if (mLocationOverlay.myLocation != null) {
                                                scope.launch {
                                                    if (currentRoute == SearchAddressScreen().key) {
                                                        mainViewModel.getAddressFromMap(
                                                            mLocationOverlay.myLocation.longitude,
                                                            mLocationOverlay.myLocation.latitude
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        FloatingButton2(
                                            Icons.Filled.Menu, backgroundColor = Color.White,
                                            contentColor = PrimaryColor
                                        ) {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    }

                                }
                            },
                            sheetContent = {
                                //SearchDriverSheetContent(orderExecutionViewModel)
                            },
                            sheetPeekHeight = sheetPeekHeight.dp
                        ) {
                            CustomMainMap(mainViewModel = mainViewModel)
                        }
                    }
                })
        }


    }


}