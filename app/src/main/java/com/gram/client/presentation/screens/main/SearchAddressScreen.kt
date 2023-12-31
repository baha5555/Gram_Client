package com.gram.client.presentation.screens.main

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.app.preference.CustomPreference
import com.gram.client.domain.mainScreen.Address
import com.gram.client.presentation.components.*
import com.gram.client.presentation.components.voyager.MapPointSheetContent
import com.gram.client.presentation.components.voyager.SearchAddresses
import com.gram.client.presentation.screens.drawer.myaddresses_screen.MyAddressViewModel
import com.gram.client.presentation.screens.main.components.*
import com.gram.client.presentation.screens.map.CustomMainMap
import com.gram.client.presentation.screens.map.mLocationOverlay
import com.gram.client.presentation.screens.map.map
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.presentation.screens.order.SearchDriverScreen
import com.gram.client.presentation.screens.profile.ProfileViewModel
import com.gram.client.utils.Constants
import com.gram.client.utils.Routes
import com.gram.client.utils.Values
import kotlinx.coroutines.launch


class SearchAddressScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val mainViewModel: MainViewModel = hiltViewModel()
        val statePoint = mainViewModel.stateAddressPoint.value
        val isSearchState = remember { mutableStateOf(false) }
        val sheetPeekHeight = remember { mutableStateOf(310) }
        when (Values.currentRoute.value) {
            Routes.CREATE_ORDER_SHEET -> sheetPeekHeight.value = 345
            Routes.SEARCH_ADDRESS_SHEET -> sheetPeekHeight.value = 310
            Routes.MAP_POINT_SHEET -> sheetPeekHeight.value = 200
        }

        val navController = rememberNavController()
        // Get the active screen
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        Values.currentRoute.value = navBackStackEntry?.destination?.route.toString()

        var bottomSheetState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
        )
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val bottomNavigator = LocalBottomSheetNavigator.current

        val focusRequester = remember { FocusRequester() }

        CustomBackHandle(drawerState.isClosed)

        val myAddressViewModel: MyAddressViewModel = hiltViewModel()
        val profileViewModel: ProfileViewModel = hiltViewModel()
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
        val prefs = CustomPreference(LocalContext.current)

        LaunchedEffect(true) {
            Values.WhichAddress.value = Constants.FROM_ADDRESS
            mainViewModel.getActualLocation(context)
            if (myAddressViewModel.stateGetAllMyAddresses.value.response == null) {
                myAddressViewModel.getAllMyAddresses()
            }

            if (prefs.getAccessToken() != "") {
                profileViewModel.getProfileInfo()
            }
        }
        LaunchedEffect(bottomSheetState.bottomSheetState.currentValue) {
            if (bottomSheetState.bottomSheetState.isCollapsed) {
                isSearchState.value = false
            } else {
            }
        }
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
                        Scaffold(bottomBar = {

                            AnimatedVisibility(Values.currentRoute.value == Routes.CREATE_ORDER_SHEET) {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    BottomBar(mainBottomSheetState = bottomSheetState) {
                                        mainViewModel.createOrder {
                                            navigator.push(SearchDriverScreen())
                                            orderExecutionViewModel.getActiveOrders { }
                                        }
                                    }
                                }
                            }
                        }) {
                            BottomSheetScaffold(
                                modifier = Modifier.fillMaxSize(),
                                floatingActionButton = {
                                    AnimatedVisibility(Values.currentRoute.value == Routes.CREATE_ORDER_SHEET || Values.currentRoute.value == Routes.MAP_POINT_SHEET) {
                                        Column(
                                            modifier = Modifier
                                                .offset(y = (-100).dp, x = 25.dp),
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            FloatingActionButton(
                                                modifier = Modifier.size(50.dp),
                                                backgroundColor = MaterialTheme.colors.background,
                                                onClick = {
                                                    navController.popBackStack()
                                                }
                                            ) {
                                                Icon(
                                                    Icons.Filled.ArrowBack,
                                                    contentDescription = "Menu",
                                                    modifier = Modifier.size(25.dp)
                                                )
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
                                        ) {
                                            map.controller.animateTo(mLocationOverlay.myLocation)
                                            if (mLocationOverlay.myLocation != null) {
                                                scope.launch {
                                                    if (Values.currentRoute.value == SearchAddressScreen().key) {
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
                                        ) {
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
                                    NavHost(
                                        navController = navController,
                                        startDestination = Routes.SEARCH_ADDRESS_SHEET
                                    ) {
                                        composable(Routes.SEARCH_ADDRESS_SHEET) {
                                            if(Values.WhichAddress.value == Constants.TO_ADDRESS && Routes.SEARCH_ADDRESS_SHEET == Values.currentRoute.value) Values.WhichAddress.value = Constants.FROM_ADDRESS
                                            SearchAddressSheetContent(
                                                focusRequester,
                                                isSearchState,
                                                navController,
                                                mainViewModel
                                            )
                                        }
                                        composable(Routes.CREATE_ORDER_SHEET) {
                                            MainBottomSheetContent(
                                                scaffoldState = bottomSheetState,
                                                mainViewModel = mainViewModel
                                            ) {
                                                if (Constants.stateOfDopInfoForDriver.value != "PLAN_TRIP" && Constants.stateOfDopInfoForDriver.value != "") {
                                                    val inputMethodManager =
                                                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                                    inputMethodManager.toggleSoftInput(
                                                        InputMethodManager.SHOW_FORCED,
                                                        0
                                                    )
                                                }
                                                coroutineScope.launch {
                                                    //modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                                }
                                            }
                                        }
                                        composable(Routes.MAP_POINT_SHEET) {
                                            MapPointSheetContent(mainViewModel) {
                                                statePoint.response.let {
                                                    when (Values.WhichAddress.value) {
                                                        Constants.FROM_ADDRESS -> {
                                                            if (it == null) {
                                                                mainViewModel.updateFromAddress(
                                                                    Address(
                                                                        "Метка на карте",
                                                                        -1,
                                                                        map.mapCenter.latitude.toString(),
                                                                        map.mapCenter.longitude.toString()
                                                                    )
                                                                )
                                                            } else {
                                                                mainViewModel.updateFromAddress(
                                                                    it
                                                                )
                                                            }
                                                        }

                                                        Constants.TO_ADDRESS -> {
                                                            if (it == null) {
                                                                mainViewModel.clearToAddress()
                                                            } else {
                                                                mainViewModel.updateToAddress(
                                                                    it
                                                                )
                                                            }
                                                        }
                                                    }
                                                    navController.popBackStack()
                                                }
                                            }
                                        }
                                    }
                                },
                                sheetPeekHeight = sheetPeekHeight.value.dp,
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.TopCenter
                                )
                                {
                                    CustomMainMap(mainViewModel = mainViewModel)
                                    if (Values.currentRoute.value == Routes.SEARCH_ADDRESS_SHEET) {
                                        FromAddressField(fromAddress) {
                                            bottomNavigator.show(
                                                SearchAddresses({
                                                    navController.navigate(Routes.CREATE_ORDER_SHEET)
                                                }) {
                                                    navController.navigate(Routes.MAP_POINT_SHEET)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    @Composable
    private fun SearchAddressSheetContent(
        focusRequester: FocusRequester,
        isSearchState: MutableState<Boolean>,
        navController: NavHostController,
        mainViewModel: MainViewModel
    ) {
        val toAddress = mainViewModel.toAddresses
        val searchText = remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .fillMaxHeight(fraction = 0.80f)
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
                            toAddress = toAddress,
                            navController = navController,
                            mainViewModel = mainViewModel
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        FastAddresses(mainViewModel, navController)
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
        }
    }
}