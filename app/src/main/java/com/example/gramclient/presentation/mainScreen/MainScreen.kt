package com.example.gramclient.presentation.mainScreen

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gramclient.PreferencesName
import com.example.gramclient.R
import com.example.gramclient.domain.profile.main
import com.example.gramclient.presentation.components.*
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    preferences: SharedPreferences,
    mainViewModel: MainViewModel,
) {

    val mainBottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scaffoldState = rememberScaffoldState()

    val paymentMethods = listOf("Наличные", "Картой", "С бонусного счета")
    val paymentState = remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var initialApiCalled by rememberSaveable { mutableStateOf(false) }

    if (!initialApiCalled) {
        LaunchedEffect(Unit) {
            mainViewModel.getTariffs(
                preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString()
            )
            mainViewModel.getAllowancesByTariffId(
                preferences.getString(
                    PreferencesName.ACCESS_TOKEN,
                    ""
                ).toString(), 1
            )
//            mainViewModel.getActualLocation(
//                context,
//                preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString()
//            )
            mainViewModel.getPrice(preferences)
            initialApiCalled = true
        }
    }

    val stateTariffs by mainViewModel.stateTariffs
    val stateAllowances by mainViewModel.stateAllowances
    val stateAddressByPoint by mainViewModel.stateAddressPoint
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
                            .clickable { paymentState.value = item }
                            .padding(vertical = 15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Row {
                                Image(
                                    modifier = Modifier.size(20.dp),
                                    imageVector = ImageVector.vectorResource(if (idx == 0) R.drawable.wallet_icon else if (idx == 1) R.drawable.payment_card_icon else R.drawable.bonus_icon),
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

        var expanded by remember { mutableStateOf(false) }
        val favorite = "2001"
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Scaffold(scaffoldState = scaffoldState, bottomBar = {
                    BottomBar(
                        navController, mainBottomSheetState, bottomSheetState,
                        createOrder = {
                            mainViewModel.createOrder(preferences)
                        }
                    )
                }) {

                    BottomSheetScaffold(
                        sheetBackgroundColor = Color.Transparent,
                        scaffoldState = mainBottomSheetState,
                        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        sheetContent = {
                            MainBottomSheetContent(
                                scaffoldState = mainBottomSheetState,
                                mainViewModel = mainViewModel,
                                stateCalculate = stateCalculate,
                                stateTariffs = stateTariffs,
                                preferences = preferences,
                                stateAllowances = stateAllowances,
                                stateAddressByPoint = stateAddressByPoint,
                                navController = navController
                            )
                        },
                        sheetPeekHeight = 355.dp,
                    ) {
                        CustomMainMap(navController = navController, mainViewModel = mainViewModel)
                    }
                }
            }
        }
    }
}







