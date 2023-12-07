package com.gram.client.presentation.screens.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.presentation.components.*
import com.gram.client.presentation.components.voyager.MapPointScreen
import com.gram.client.presentation.components.voyager.SearchAddresses
import com.gram.client.presentation.screens.drawer.myaddresses_screen.MyAddressViewModel
import com.gram.client.presentation.screens.main.components.*
import com.gram.client.presentation.screens.map.CustomMainMap
import com.gram.client.presentation.screens.map.currentRoute
import com.gram.client.presentation.screens.map.mLocationOverlay
import com.gram.client.presentation.screens.map.map
import com.gram.client.presentation.screens.order.SearchDriverScreen
import com.gram.client.utils.Constants
import com.gram.client.utils.Values
import kotlinx.coroutines.launch


class SearchAddressScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val mainViewModel: MainViewModel = hiltViewModel()
        val isSearchState = remember { mutableStateOf(false) }
        val sheetPeekHeight = remember { mutableStateOf(350) }


        val bottomSheetState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
        )
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val bottomNavigator = LocalBottomSheetNavigator.current

        val focusRequester = remember { FocusRequester() }

        CustomBackHandle(drawerState.isClosed)

        val myAddressViewModel: MyAddressViewModel = hiltViewModel()

        LaunchedEffect(true) {
            Values.WhichAddress.value = Constants.FROM_ADDRESS
            mainViewModel.getActualLocation(context)
            if(myAddressViewModel.stateGetAllMyAddresses.value.response==null){
                myAddressViewModel.getAllMyAddresses()
            }
        }
        LaunchedEffect(bottomSheetState.bottomSheetState.currentValue) {
            if (bottomSheetState.bottomSheetState.isCollapsed) {
                isSearchState.value = false
            } else {
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
                                            Icons.Filled.ArrowBack,
                                            backgroundColor = MaterialTheme.colors.background,
                                            contentColor = MaterialTheme.colors.onBackground
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
                                        ImageVector.vectorResource(id = R.drawable.btn_show_location)
                                    ){
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
                                    FloatingButton(
                                        Icons.Filled.Menu
                                    ){
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }
                                }
                            },
                            drawerGesturesEnabled = false,
                            sheetBackgroundColor = Color(0xffEEEEEE),
                            scaffoldState = bottomSheetState,
                            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                            sheetGesturesEnabled = true,
                            backgroundColor = Color(0xFF702D2D),
                            sheetContent = {
                                val searchText = remember { mutableStateOf("") }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .focusRequester(focusRequester)
                                        .fillMaxHeight(fraction = 0.98f)
                                        .background(Color(0xffEEEEEE)),
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(197.dp)
                                            .padding(2.dp)
                                            .background(
                                                color = Color(0xFFFFFFFF),
                                                shape = RoundedCornerShape(size = 30.dp)
                                            ), horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(0.95f)
                                                .padding(9.dp)
                                                .background(
                                                    color = Color(0xFFFFFFFF),
                                                    shape = RoundedCornerShape(size = 30.dp)
                                                )
                                        ) {
                                        if (!isSearchState.value) {
                                            if (searchText.value != "") searchText.value = ""
                                            ToAddressField(
                                                toAddress = toAddress
                                            )
                                            Spacer(modifier = Modifier.height(15.dp))
                                            FastAddresses(mainViewModel)
                                        }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = Color(0xFFFFFFFF),
                                                shape = RoundedCornerShape(size = 30.dp)
                                            )
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(20.dp)
                                                .background(
                                                    color = Color(0xFFFFFFFF),
                                                    shape = RoundedCornerShape(size = 30.dp)
                                                )
                                        ) {
                                            Text(
                                                text = "Добро пожаловать в Gram",
                                                style = TextStyle(
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight(600),
                                                    color = Color(0xFF343434),
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            StoriesList()
                                        }


                                    }
//            Spacer(modifier = Modifier.height(15.dp))
//            Services()
                                }
                            },
                            sheetPeekHeight = sheetPeekHeight.value.dp-40.dp,
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.TopCenter
                            )
                            {
                                CustomMainMap(
                                    mainViewModel = mainViewModel
                                )
                                FromAddressField(fromAddress) {
                                    bottomNavigator.show(
                                        SearchAddresses{
                                            navigator.push(MapPointScreen())
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}