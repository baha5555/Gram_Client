package com.example.gramclient.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.example.gramclient.utils.Constants
import com.example.gramclient.R
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.domain.mainScreen.TariffsResult
import com.example.gramclient.presentation.components.voyager.SearchAddressNavigator
import com.example.gramclient.presentation.screens.main.MainViewModel
import com.example.gramclient.presentation.screens.main.components.*
import com.example.gramclient.presentation.screens.main.states.AllowancesResponseState
import com.example.gramclient.presentation.screens.main.states.CalculateResponseState
import com.example.gramclient.presentation.screens.main.states.TariffsResponseState
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.utils.Constants.stateOfDopInfoForDriver
import currentFraction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainBottomSheetContent(
    heightFraction: Float = 0.92f,
    scaffoldState: BottomSheetScaffoldState,
    mainViewModel: MainViewModel,
    stateCalculate: CalculateResponseState,
    stateTariffs: TariffsResponseState,
    stateAllowances: AllowancesResponseState,
    isSearchState: MutableState<Boolean>,
    focusRequester: FocusRequester,
    dopPhone: () -> Unit
) {
    val tariffIcons = arrayOf(
        R.drawable.car_econom_pic,
        R.drawable.car_comfort_pic,
        R.drawable.car_business_pic,
        R.drawable.car_miniven_pic,
        R.drawable.courier_icon
    )

    val tariffListIcons = arrayOf(
        R.drawable.car_econom_icon,
        R.drawable.car_comfort_icon,
        R.drawable.car_business_icon,
        R.drawable.car_miniven_icon,
        R.drawable.courier_icon
    )


    val fromAddress by mainViewModel.fromAddress
    val toAddress by mainViewModel.toAddress
    val selected_tariff = mainViewModel.selectedTariff?.observeAsState()

    var WHICH_ADDRESS = remember { mutableStateOf("") }
    val isAddressList = remember { mutableStateOf(true) }

    val searchText = remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .fillMaxHeight(fraction = heightFraction)
    ) {
        if (!isSearchState.value) {
            if (searchText.value != "")
                searchText.value = ""
            SheetContent(
                currentFraction = scaffoldState.currentFraction,
                addressContent = {
                    AddressesContent(
                        currentFraction = scaffoldState.currentFraction,
                        address_from = fromAddress,
                        address_to = toAddress
                    )
                },
                tariffsContent = {
                    TariffsContent(
                        currentFraction = scaffoldState.currentFraction,
                        selected_tariff = selected_tariff,
                        stateCalculate = stateCalculate,
                        address_to = toAddress,
                        stateTariffs = stateTariffs,
                        tariffListIcons = tariffListIcons,
                        mainViewModel = mainViewModel,
                        tariffIcons = tariffIcons
                    )
                },
                optionsContent = {
                    OptionsContent(dopPhone)
                },
                allowancesContent = {
                    AllowancesContent(
                        stateAllowances = stateAllowances,
                        mainViewModel = mainViewModel,
                    )
                }
            )
        } else {
            LaunchedEffect(Unit) {
                delay(200)
                focusRequester.requestFocus()
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .padding(bottom = 80.dp, top = 15.dp, start = 15.dp, end = 15.dp)
            ) {
                SearchTextField(
                    searchText = searchText,
                    focusRequester = focusRequester,
                    isSearchState = isSearchState,
                    bottomSheetState = scaffoldState,
                    scope = coroutineScope
                )
                SearchResultContent(
                    searchText = searchText,
                    focusManager = focusManager,
                    isAddressList = isAddressList,
                    bottomSheetState = scaffoldState,
                    isSearchState = isSearchState,
                    scope = coroutineScope,
                    mainViewModel = mainViewModel,
                    WHICH_ADDRESS = WHICH_ADDRESS,
                )
            }
        }
    }
}


@Composable
fun SheetContent(
    addressContent: @Composable() (ColumnScope.() -> Unit),
    tariffsContent: @Composable() (ColumnScope.() -> Unit),
    optionsContent: @Composable() (ColumnScope.() -> Unit),
    allowancesContent: @Composable() (ColumnScope.() -> Unit),
    currentFraction: Float
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                BackgroundColor,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(bottom = 80.dp)
    ) {
        Column {
            addressContent()
            Spacer(modifier = Modifier.height(0.dp + (currentFraction * 10).dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                tariffsContent()
                Spacer(modifier = Modifier.height(10.dp))
                optionsContent()
                Spacer(modifier = Modifier.height(10.dp))
                allowancesContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddressesContent(
    currentFraction: Float,
    address_from: Address,
    address_to: List<Address>
) {
    val bottomNavigator= LocalBottomSheetNavigator.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White,
                shape = RoundedCornerShape(0.dp + (currentFraction * 20).dp)
            )
            .padding(top = 15.dp, bottom = 0.dp + (currentFraction * 15).dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(
                modifier = Modifier
                    .width(45.dp)
                    .height(5.dp)
                    .graphicsLayer(translationY = -15f)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFCACAC2))
            )
        }
        Row(
            modifier = Modifier
                .clickable {
                    bottomNavigator.show(SearchAddressNavigator(Constants.FROM_ADDRESS))
                }
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(20.dp),
                imageVector = ImageVector.vectorResource(R.drawable.from_marker),
                contentDescription = "Logo"
            )
            Spacer(modifier = Modifier.width(20.dp))
            if (address_from.address == "") {
                Text(
                    text = "Откуда?",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
            } else {
                Text(
                    address_from.address,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 60.dp, end = 20.dp)
        ) {
            Divider()
        }
        address_to.forEach { address ->
            Row(
                modifier = Modifier
                    .clickable {
                        bottomNavigator.show(SearchAddressNavigator(Constants.TO_ADDRESS))
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier
                            .size(20.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.to_marker),
                        contentDescription = "Logo"
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    if (address.address == "") {
                        Text(
                            text = "Куда едем?", color = Color.Gray,
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Text(
                            address.address,
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                IconButton(onClick = { bottomNavigator.show(SearchAddressNavigator(Constants.TO_ADDRESS)) }) {
                    Icon(imageVector = Icons.Default.Add, "")
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 60.dp, end = 20.dp)
            ) {
                Divider()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TariffsContent(
    currentFraction: Float,
    selected_tariff: State<TariffsResult?>?,
    stateCalculate: CalculateResponseState,
    address_to: List<Address>,
    stateTariffs: TariffsResponseState,
    tariffListIcons: Array<Int>,
    mainViewModel: MainViewModel,
    tariffIcons: Array<Int>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White,
                shape = RoundedCornerShape(0.dp + (currentFraction * 20).dp)
            )
            .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .graphicsLayer(alpha = 0f + currentFraction)
                .height(0.dp + (currentFraction * 50).dp)
                .clip(RoundedCornerShape(20.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selected_tariff?.value!!.name,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            stateCalculate.response?.let {
                Text(
                    text = if (address_to[0].address == "") "от ${it.result.amount} c" else "${it.result.amount} c",
                    fontSize = 25.sp
                )
            }
            if (stateCalculate.response == null || stateCalculate.error != "") {
                CustomPulseLoader(isLoading = true)
            }
        }
        Column(horizontalAlignment = Alignment.Start) {
            Image(
                modifier = Modifier
                    .padding(
                        end =
                        when (selected_tariff?.value!!.id) {
                            1 -> 110.dp
                            2 -> 145.dp
                            4 -> 130.dp
                            5 -> 130.dp
                            else -> 310.dp
                        }, bottom = 30.dp
                    )
                    .fillMaxWidth(0f + currentFraction)
                    .graphicsLayer(alpha = 0f + currentFraction)
                    .height(0.dp + (currentFraction * 80).dp),
                painter = painterResource(
                    when (selected_tariff.value!!.id) {
                        1 -> tariffIcons[0]
                        2 -> tariffIcons[1]
                        3 -> tariffIcons[2]
                        4 -> tariffIcons[2]
                        5 -> tariffIcons[3]
                        else -> tariffIcons[4]
                    }
                ),
                contentDescription = "icon"
            )
        }

        stateTariffs.response?.let { tariffs ->
            if (tariffs.size != 0) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(items = tariffs, itemContent = { tariff ->
                        TariffItem(
                            icon = when (tariff.id) {
                                1 -> tariffListIcons[0]
                                2 -> tariffListIcons[1]
                                4 -> tariffListIcons[2]
                                5 -> tariffListIcons[3]
                                else -> tariffListIcons[4]
                            },
                            name = tariff.name,
                            price = tariff.min_price,
                            isSelected = selected_tariff?.value == tariff,
                            onSelected = {
                                mainViewModel.getAllowancesByTariffId(tariff.id)
                                mainViewModel.updateSelectedTariff(tariff)
                            })
                        Spacer(modifier = Modifier.width(10.dp))
                    })
                }
            }
        }
        if (stateTariffs.response == null || stateTariffs.error != "") {
            CustomRectangleShimmer(enabled = true)
        }
    }
}

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OptionsContent(dopPhone: () -> Unit, mainViewModel: MainViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 0.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable {
                    stateOfDopInfoForDriver.value = "COMMENT_TO_ORDER"
                    dopPhone()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Коментарий водителю", fontSize = 16.sp)
                if (mainViewModel.commentToOrder.value != "")
                    Text(
                        text = if (mainViewModel.commentToOrder.value.length < 35)
                            mainViewModel.commentToOrder.value else "${
                            mainViewModel.commentToOrder.value.take(
                                35
                            )
                        }...", fontSize = 12.sp, color = Color.Gray
                    )
            }
            Image(
                modifier = Modifier.size(18.dp),
                imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                contentDescription = "icon"
            )
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable {
                    if (mainViewModel.dopPhone.value != "")
                        mainViewModel.updateDopPhone("")
                    else {
                        stateOfDopInfoForDriver.value = "TO_ANOTHER_HUMAN"
                        dopPhone()
                    }
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Заказ другому человеку", fontSize = 16.sp)
                if (mainViewModel.dopPhone.value != "")
                    Text(text = mainViewModel.dopPhone.value, fontSize = 12.sp, color = Color.Gray)
            }
            if (mainViewModel.dopPhone.value != "")
                Icon(
                    modifier = Modifier
                        .size(18.dp),
                    imageVector = Icons.Default.Done,
                    contentDescription = "icon"
                )
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable {
                    stateOfDopInfoForDriver.value = "PLAN_TRIP"
                    dopPhone()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Запланировать поездку", fontSize = 16.sp)
                if (mainViewModel.planTrip.value != "")
                    Text(text = mainViewModel.planTrip.value, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(
                modifier = Modifier
                    .size(18.dp),
                imageVector = if (mainViewModel.planTrip.value != "") Icons.Default.Done else Icons.Default.LockClock,
                contentDescription = "icon"
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun AllowancesContent(
    stateAllowances: AllowancesResponseState,
    mainViewModel: MainViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {
        stateAllowances.response?.let { allowances ->
            if (allowances.size != 0) {
                allowances.forEach { allowance ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(0.7f),
                            text = allowance.name,
                            fontSize = 16.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = " (${allowance.price}c)",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(end = 18.dp)
                            )
                            CustomSwitch(switchON = allowance.isSelected) {
                                mainViewModel.includeAllowance(allowance)
                            }
                        }
                    }
                    Divider()
                }
            }
        }
        if (stateAllowances.response == null || stateAllowances.error != "") {
            CustomLinearShimmer(enabled = true)
        }
    }
}

