package com.gram.client.presentation.components

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import com.gram.client.R
import com.gram.client.domain.mainScreen.Address
import com.gram.client.domain.mainScreen.TariffsResult
import com.gram.client.presentation.components.voyager.*
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.main.components.*
import com.gram.client.presentation.screens.main.states.AllowancesResponseState
import com.gram.client.presentation.screens.main.states.CalculateResponseState
import com.gram.client.presentation.screens.main.states.TariffsResponseState
import com.gram.client.utils.Comments
import com.gram.client.utils.Constants
import com.gram.client.utils.Values
import com.gram.client.utils.getAddressText
import currentFraction

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
    dopPhone: () -> Unit
) {
    val fromAddress by mainViewModel.fromAddress
    val toAddress = mainViewModel.toAddresses
    val selected_tariff = mainViewModel.selectedTariff?.observeAsState()
    val searchText = remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = heightFraction)
    ) {
        if (searchText.value != "")
            searchText.value = ""
        SheetContent(
            currentFraction = scaffoldState.currentFraction,
            addressContent = {
                AddressesContent(
                    currentFraction = scaffoldState.currentFraction,
                    address_from = fromAddress
                )
            },
            tariffsContent = {
                TariffsContent(
                    currentFraction = scaffoldState.currentFraction,
                    selected_tariff = selected_tariff,
                    stateCalculate = stateCalculate,
                    address_to = toAddress,
                    stateTariffs = stateTariffs,
                    mainViewModel = mainViewModel
                )
            },
            optionsContent = {
                OptionsContent(
                    dopPhone,
                    stateAllowances = stateAllowances,
                    mainViewModel = mainViewModel
                )

            },
            allowancesContent = {
//                AllowancesContent(
//                    stateAllowances = stateAllowances,
//                    mainViewModel = mainViewModel,
//                )
            }
        )
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
                MaterialTheme.colors.secondary,
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
    address_from: Address
) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val toAddresses = mainViewModel.toAddresses
    val navigator: Navigator = LocalNavigator.currentOrThrow
    val bottomNavigator = LocalBottomSheetNavigator.current
    val meeting = mainViewModel.stateMeetingInfo.value
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xffFFFFFF),
                shape = RoundedCornerShape(0.dp + (currentFraction * 20).dp)
            )
            .padding(top = 10.dp, bottom = 0.dp + (currentFraction * 15).dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(
                modifier = Modifier
                    .width(45.dp)
                    .height(5.dp)
                    .graphicsLayer(translationY = -1f)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFCACAC2))
            )
        }
        Row(
            modifier = Modifier
                .clickable {
                    bottomNavigator.show(SearchAddresses {
                        navigator.push(MapPointScreen())
                    })
                    Values.WhichAddress.value = Constants.FROM_ADDRESS
                }
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier,
                    imageVector = ImageVector.vectorResource(R.drawable.ic_serach_address_from),
                    contentDescription = "Logo"
                )
                Spacer(modifier = Modifier.width(20.dp))
                if (address_from.name == "") {
                    Text(
                        text = "Откуда?",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Gray
                    )
                } else {
                    Text(
                        if (meeting == "") getAddressText(address_from) else getAddressText(
                            address_from
                        ) + ", Место встречи " + meeting,
                        maxLines = 2, overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            if (mainViewModel.fromAddress.value.name != "") {
                Text("Место встречи", modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFF1A1A1A))
                    .clickable {
                        bottomNavigator.show(MeetSheet())
                    }
                    .padding(7.dp),
                    color = Color.White,
                    fontSize = 14.sp
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
        toAddresses.forEach {

        }
        Row(
            modifier = Modifier
                .clickable {
                    if (toAddresses.size <= 1) {
                        Values.WhichAddress.value = Constants.TO_ADDRESS
                        bottomNavigator.show(SearchAddresses {
                            navigator.push(MapPointScreen())
                        })
                    } else {
                        Values.WhichAddress.value = Constants.ADD_TO_ADDRESS
                        bottomNavigator.show(AddStopScreen {
                            navigator.push(MapPointScreen())
                        })
                        Toast
                            .makeText(
                                context,
                                "Можно изменить порядок остановки",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                }
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .fillMaxWidth(0.8f)
            ) {
                Image(
                    modifier = Modifier,
                    imageVector = if (MaterialTheme.colors.isLight) ImageVector.vectorResource(R.drawable.ic_serach_address_to)
                    else ImageVector.vectorResource(R.drawable.to_marker_dark),
                    contentDescription = "Logo"
                )
                Spacer(modifier = Modifier.width(20.dp))
                when (toAddresses.size) {
                    0 -> {
                        Text(
                            text = "Куда едем?", color = Color.Gray,
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                    }

                    1 -> {
                        Text(
                            getAddressText(toAddresses[0]),
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                    }

                    else -> {
                        Text(
                            "" + toAddresses.size + " - остановки",
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            if (toAddresses.size != 0) {
                Icon(imageVector = Icons.Default.Add, "", modifier = Modifier
                    .size(35.dp)
                    .clickable {
                        bottomNavigator.show(SearchAddressNavigator(
                            Constants.ADD_TO_ADDRESS,
                            function = {
                                navigator.push(MapPointScreen())
                                Values.WhichAddress.value = Constants.ADD_TO_ADDRESS
                            }
                        )
                        )
                    })
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TariffsContent(
    currentFraction: Float,
    selected_tariff: State<TariffsResult?>?,
    stateCalculate: CalculateResponseState,
    address_to: List<Address>,
    stateTariffs: TariffsResponseState,
    mainViewModel: MainViewModel,
) {
    val stateCalculate = mainViewModel.stateCalculate.value
    val countriesKey = mainViewModel.stateCountriesKey.value
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xffFFFFFF),
                shape = RoundedCornerShape(0.dp + (currentFraction * 20).dp)
            )
            .padding(horizontal = 20.dp, vertical = 20.dp)
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
                fontSize = 32.sp,
                color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold
            )
            stateCalculate.response?.let { it ->
                it.result.forEach {
                    if (selected_tariff.value == null) return
                    if (it.tariff_id == selected_tariff.value!!.id) {
                        Text(
                            text = "${it.amount} ${countriesKey.response?.currency_symbol?.monetary_unit}",
                            fontSize = 30.sp
                        )

                    }
                }
            }
            if (stateCalculate.response == null || stateCalculate.error != "") {
                CustomPulseLoader(isLoading = true)
            }
        }
        Column(horizontalAlignment = Alignment.Start) {
            if (selected_tariff != null) {
                Image(
                    modifier = Modifier
                        .padding(
                            end = 120.dp, bottom = 20.dp
                        )
                        .fillMaxWidth(0f + currentFraction)
                        .graphicsLayer(alpha = 0f + currentFraction)
                        .height(0.dp + (currentFraction * 120).dp),
                    painter = rememberAsyncImagePainter(selected_tariff.value!!.image),
                    contentDescription = "icon"
                )
            }
        }

        stateTariffs.response?.let { tariffs ->
            if (tariffs.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(items = tariffs, itemContent = { tariff ->
                        val price = remember {
                            mutableStateOf(0)
                        }
                        stateCalculate.response?.result?.forEach {
                            if (it.tariff_id == tariff.id) price.value = it.amount
                        }
                        countriesKey.response?.currency_symbol?.key?.let {
                            TariffItem(
                                icon = tariff.icon,
                                name = tariff.name,
                                price = if (price.value == 0) tariff.min_price else price.value,
                                stateCalculate = stateCalculate,
                                isSelected = selected_tariff?.value == tariff,
                                onSelected = {
                                    mainViewModel.getAllowancesByTariffId(tariff.id)
                                    mainViewModel.updateSelectedTariff(tariff)
                                },
                                key = it
                            )
                        }

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
@Composable
fun OptionsContent(
    dopPhone: () -> Unit,
    mainViewModel: MainViewModel,
    stateAllowances: AllowancesResponseState
) {
    val context = LocalContext.current
    val bottomNavigator = LocalBottomSheetNavigator.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xffFFFFFF),
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
    ) {
        val bottomNavigator = LocalBottomSheetNavigator.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    bottomNavigator.show(CommentSheet("Комментарий водителю", Comments.DRIVER))
                }
                .padding(15.dp)
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Коментарий водителю", fontSize = 16.sp)
                if (Values.CommentDriver.value != "")
                    Text(
                        text = if (Values.CommentDriver.value.length < 35)
                            Values.CommentDriver.value else "${
                            Values.CommentDriver.value.take(35)
                        }...", fontSize = 12.sp, color = Color.Gray
                    )
            }
            Image(
                modifier = Modifier.size(18.dp),
                imageVector = if (Values.CommentDriver.value == "") ImageVector.vectorResource(R.drawable.arrow_right) else Icons.Default.Done,
                contentDescription = "icon"
            )
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (Values.CommentToAnotherHuman.value == "992") {
                        bottomNavigator.show(
                            CommentSheet(
                                "Кто поедет на такси?",
                                Comments.TO_ANOTHER_HUMAN
                            )
                        )
                    } else {
                        Values.CommentToAnotherHuman.value = "992"
                    }

                }
                .padding(15.dp)
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Заказ другому человеку", fontSize = 16.sp)
                if (Values.CommentToAnotherHuman.value != "992")
                    Text(
                        text = Values.CommentToAnotherHuman.value,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
            }
            if (Values.CommentToAnotherHuman.value != "992")
                Icon(
                    modifier = Modifier
                        .size(18.dp),
                    imageVector = Icons.Default.Done,
                    contentDescription = "icon"
                )
            else Image(
                modifier = Modifier.size(18.dp),
                imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                contentDescription = "icon"
            )
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Toast
                        .makeText(context, "В стадии разработки!", Toast.LENGTH_SHORT)
                        .show()

                    /*stateOfDopInfoForDriver.value = "PLAN_TRIP"
                    dopPhone()*/
                }
                .padding(15.dp)
                .padding(horizontal = 5.dp),
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
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable {
//                Toast
//                    .makeText(context, "В стадии разработки!", Toast.LENGTH_SHORT)
//                    .show()
                bottomNavigator.show(AddAllowancesSheet() {})
            }
            .padding(15.dp)
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Добавить надбавки", fontSize = 16.sp)
        }
        Icon(
            modifier = Modifier
                .size(18.dp),
            imageVector = Icons.Default.Add,
            contentDescription = "icon"
        )
    }
}