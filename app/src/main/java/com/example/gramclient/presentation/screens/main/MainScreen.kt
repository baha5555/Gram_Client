package com.example.gramclient.presentation.screens.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.gramclient.R
import com.example.gramclient.app.preference.CustomPreference
import com.example.gramclient.presentation.components.*
import com.example.gramclient.presentation.screens.map.CustomMainMap
import com.example.gramclient.presentation.screens.order.OrderExecutionViewModel
import com.example.gramclient.presentation.screens.profile.ProfileViewModel
import kotlinx.coroutines.launch


class MainScreen : Screen{
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val mainViewModel: MainViewModel = hiltViewModel()
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
        val profileViewModel: ProfileViewModel = hiltViewModel()
        val mainBottomSheetState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
        )

        val bottomSheetState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
        )

        val scaffoldState = rememberScaffoldState()

        val paymentMethods = listOf("Наличные", "Картой", "С бонусного счета")
        val paymentState = remember { mutableStateOf("") }

        val coroutineScope = rememberCoroutineScope()

        var initialApiCalled by rememberSaveable { mutableStateOf(false) }
        var isSearchState = remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }

        val prefs = CustomPreference(LocalContext.current)
        if (!initialApiCalled) {
            LaunchedEffect(Unit) {
                if (prefs.getAccessToken() != "") {
                    profileViewModel.getProfileInfo()
                }
                mainViewModel.getTariffs()
                mainViewModel.getAllowancesByTariffId(1)
                mainViewModel.getPrice()
                initialApiCalled = true
            }
        }

        LaunchedEffect(mainBottomSheetState.bottomSheetState.currentValue) {
            if (mainBottomSheetState.bottomSheetState.isCollapsed) {
                isSearchState.value = false
                Log.e("singleTapConfirmedHelper", "isCollapsed")
            } else {
                Log.e("singleTapConfirmedHelper", "isExpanded")
            }
        }

        val stateTariffs by mainViewModel.stateTariffs

        val stateAllowances by mainViewModel.stateAllowances

        val stateCalculate by mainViewModel.stateCalculate

        BottomSheetScaffold(
            sheetBackgroundColor = Color.White,
            scaffoldState = bottomSheetState,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, start = 30.dp, end = 17.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Способ оплаты",
                            fontSize = 22.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        paymentMethods.forEachIndexed { idx, item ->
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    paymentState.value = item
                                }
                                .padding(vertical = 15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Row {
                                    Image(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = ImageVector.vectorResource(
                                            when (idx) {
                                                0 -> R.drawable.wallet_icon
                                                1 -> R.drawable.payment_card_icon
                                                else -> R.drawable.bonus_icon
                                            }
                                        ),
                                        contentDescription = "Logo"
                                    )
                                    Spacer(modifier = Modifier.width(19.dp))
                                    Text(text = item)
                                }
                                CustomCheckBox(size = 25.dp,
                                    isChecked = paymentState.value == item,
                                    onChecked = { paymentState.value = item })
                            }
                            Divider()
                        }
                    }
                    Spacer(modifier = Modifier.height(54.dp))
                    CustomButton(modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clip(RoundedCornerShape(12.dp)),
                        text = "Готово",
                        textSize = 20,
                        textBold = true,
                        onClick = {
                            coroutineScope.launch {
                                bottomSheetState.bottomSheetState.collapse()
                            }
                        })
                }
            },
            sheetPeekHeight = 0.dp,
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Scaffold(scaffoldState = scaffoldState, bottomBar = {
                        if (!isSearchState.value) {
                            BottomBar(mainBottomSheetState, bottomSheetState,
                                createOrder = {
                                    mainViewModel.createOrder(orderExecutionViewModel)
                                }
                            )
                        }
                    }) {
                        BottomSheetScaffold(
                            sheetBackgroundColor = Color.Transparent,
                            scaffoldState = mainBottomSheetState,
                            floatingActionButton = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 30.dp)
                                        .offset(y = if (bottomSheetState.bottomSheetState.isCollapsed) (-35).dp else (-65).dp),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    FloatingActionButton(
                                        modifier = Modifier
                                            .size(50.dp),
                                        backgroundColor = Color.White,
                                        onClick = {
                                            coroutineScope.launch {
                                                navigator.replaceAll(SearchAddressScreen())
                                            }
                                        }
                                    ) {
                                        Icon(
                                            Icons.Filled.ArrowBack,
                                            contentDescription = "Menu", tint = Color.Black,
                                            modifier = Modifier.size(25.dp)
                                        )
                                    }
                                }
                            },
                            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                            sheetContent = {
                                MainBottomSheetContent(
                                    scaffoldState = mainBottomSheetState,
                                    mainViewModel = mainViewModel,
                                    stateCalculate = stateCalculate,
                                    stateTariffs = stateTariffs,
                                    stateAllowances = stateAllowances,
                                    isSearchState = isSearchState,
                                    focusRequester = focusRequester
                                )
                            },
                            sheetPeekHeight = 325.dp,
                        ) {
                            CustomMainMap(mainViewModel = mainViewModel)
                        }
                    }
                }
            }
        }
    }
}







