package com.example.gramclient.presentation.components

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gramclient.PreferencesName
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.domain.mainScreen.TariffsResult
import com.example.gramclient.presentation.mainScreen.MainViewModel
import com.example.gramclient.presentation.mainScreen.states.AddressByPointResponseState
import com.example.gramclient.presentation.mainScreen.states.AllowancesResponseState
import com.example.gramclient.presentation.mainScreen.states.CalculateResponseState
import com.example.gramclient.presentation.mainScreen.states.TariffsResponseState
import com.example.gramclient.ui.theme.BackgroundColor
import currentFraction

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainBottomSheetContent(
    heightFraction: Float = 0.95f,
    scaffoldState: BottomSheetScaffoldState,
    mainViewModel: MainViewModel,
    stateCalculate: CalculateResponseState,
    stateTariffs: TariffsResponseState,
    preferences: SharedPreferences,
    stateAllowances: AllowancesResponseState,
    stateAddressByPoint: AddressByPointResponseState,
    navController: NavHostController
){
    val tariffIcons = arrayOf(R.drawable.car_econom_pic, R.drawable.car_comfort_pic, R.drawable.car_business_pic, R.drawable.car_miniven_pic, R.drawable.courier_icon)
    val tariffListIcons = arrayOf(R.drawable.car_econom_icon, R.drawable.car_comfort_icon, R.drawable.car_business_icon, R.drawable.car_miniven_icon, R.drawable.courier_icon)


    val address_from=mainViewModel.from_address.observeAsState()
    val address_to=mainViewModel.to_address.observeAsState()
    val selected_tariff=mainViewModel.selectedTariff?.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = heightFraction)
    ) {
        SheetContent (
            currentFraction=scaffoldState.currentFraction,
            addressContent = {
                AddressesContent(
                    currentFraction = scaffoldState.currentFraction,
                    stateAddressByPoint=stateAddressByPoint,
                    navController=navController,
                    address_from=address_from,
                    address_to=address_to
                )
                             },
            tariffsContent = {
                TariffsContent(
                    scaffoldState = scaffoldState,
                    currentFraction = scaffoldState.currentFraction,
                    selected_tariff=selected_tariff,
                    stateCalculate=stateCalculate,
                    address_to=address_to,
                    stateTariffs=stateTariffs,
                    tariffListIcons=tariffListIcons,
                    mainViewModel=mainViewModel,
                    preferences=preferences,
                    tariffIcons=tariffIcons
                )
            },
            optionsContent = {
                OptionsContent()
            },
            allowancesContent = {
                AllowancesContent(
                    stateAllowances=stateAllowances,
                    mainViewModel=mainViewModel,
                    preferences=preferences
                )
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
                BackgroundColor,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(bottom = 80.dp)
    ) {
        Column {
            addressContent()
            Spacer(modifier = Modifier.height(0.dp+(currentFraction*10).dp))
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

@Composable
fun AddressesContent(
    currentFraction: Float,
    stateAddressByPoint: AddressByPointResponseState,
    navController: NavHostController,
    address_from: State<Address?>,
    address_to: State<MutableList<Address>?>
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(
            Color.White,
            shape = RoundedCornerShape(0.dp + (currentFraction * 20).dp)
        )
        .padding(horizontal = 20.dp, vertical = 15.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Spacer(modifier = Modifier
                .width(45.dp)
                .height(5.dp)
                .graphicsLayer(translationY = -15f)
                .clip(RoundedCornerShape(50.dp))
                .background(Color(0xFFCACAC2))
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Loader(isLoading = stateAddressByPoint.isLoading)
            stateAddressByPoint.response?.let { address ->
                Image(
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.from_marker),
                    contentDescription = "Logo"
                )
            }
            if(stateAddressByPoint.error != ""){
                Image(
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.from_marker),
                    contentDescription = "Logo"
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            Column(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
            ){
                Text(
                    address_from.value?.name ?: "",
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable {
                    navController.navigate("${RoutesName.SEARCH_ADDRESS_SCREEN}/fromAddress")
                })
                Spacer(modifier = Modifier.height(20.dp))
                Divider()
            }
        }
        address_to.value?.forEach { address ->
            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(top = 10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.to_marker),
                    contentDescription = "Logo"
                )
                Spacer(modifier = Modifier.width(30.dp))
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                ){
                    Text(
                        address.name,
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable {
                        navController.navigate("${RoutesName.SEARCH_ADDRESS_SCREEN}/toAddress")
                    }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Divider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TariffsContent(
    currentFraction: Float,
    scaffoldState: BottomSheetScaffoldState,
    selected_tariff: State<TariffsResult?>?,
    stateCalculate: CalculateResponseState,
    address_to: State<MutableList<Address>?>,
    stateTariffs: TariffsResponseState,
    tariffListIcons: Array<Int>,
    mainViewModel: MainViewModel,
    preferences: SharedPreferences,
    tariffIcons: Array<Int>
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(
            Color.White,
            shape = RoundedCornerShape(0.dp + (currentFraction * 20).dp)
        )
        .padding(horizontal = 20.dp, vertical = 15.dp)
    ){
        Row(
            Modifier
                .fillMaxWidth()
                .graphicsLayer(alpha = 0f + currentFraction)
                .height(0.dp + (currentFraction * 50).dp)
                .clip(RoundedCornerShape(20.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = selected_tariff?.value!!.name, fontWeight = FontWeight.Bold, fontSize = 25.sp)
            stateCalculate.response?.let {
                Text(text = if(address_to.value!![0].name == "Куда?") "от ${it.result.amount} c" else "${it.result.amount} c", fontSize = 25.sp)
            }
            if (stateCalculate.response == null || stateCalculate.error != ""){
                CustomPulseLoader(isLoading = true)
            }
        }
        Column(horizontalAlignment = Alignment.Start) {
            Image(
                modifier = Modifier
                    .fillMaxWidth(0f + currentFraction)
                    .graphicsLayer(alpha = 0f + currentFraction)
                    .height(0.dp + (currentFraction * 80).dp),
                painter = painterResource(if(selected_tariff?.value!!.id==1) tariffIcons[0] else if(selected_tariff.value!!.id==2) tariffIcons[1] else if(selected_tariff.value!!.id==4) tariffIcons[2] else if(selected_tariff.value!!.id==5) tariffIcons[3] else tariffIcons[4]),
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
                            icon = if (tariff.id == 1) tariffListIcons[0] else if (tariff.id == 2) tariffListIcons[1] else if (tariff.id == 4) tariffListIcons[2] else if (tariff.id == 5) tariffListIcons[3] else tariffListIcons[4],
                            name = tariff.name,
                            price = tariff.min_price,
                            isSelected = selected_tariff?.value == tariff,
                            onSelected = {
                                mainViewModel.getAllowancesByTariffId(
                                    preferences.getString(
                                        PreferencesName.ACCESS_TOKEN, ""
                                    ).toString(), selected_tariff?.value?.id ?: 1
                                )
                                mainViewModel.updateSelectedTariff(tariff, preferences)
                            })
                        Spacer(modifier = Modifier.width(10.dp))
                    })
                }
            }
        }
        if (stateTariffs.response == null || stateTariffs.error != ""){
                CustomRectangleShimmer(enabled = true)
        }
    }
}

@Composable
fun OptionsContent() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(
            Color.White,
            shape = RoundedCornerShape(20.dp)
        )
        .padding(horizontal = 20.dp, vertical = 0.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Коментарий водителю", fontSize = 16.sp)
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
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Заказ другому человеку", fontSize = 16.sp)
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
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Запланировать поездку", fontSize = 16.sp)
            Image(
                modifier = Modifier.size(18.dp),
                imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
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
    preferences: SharedPreferences
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(
            Color.White,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        )
        .padding(horizontal = 20.dp, vertical = 15.dp)
    ){
        stateAllowances.response?.let { allowances ->
            if (allowances.size != 0) {
                allowances.forEach { allowance->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row() {
                            Text(text = allowance.name, fontSize = 16.sp)
                            Text(text = " (${allowance.price}c)", fontSize = 16.sp, color = Color.Gray)
                        }
                        CustomSwitch(switchON = allowance.isSelected) {
                            mainViewModel.includeAllowance(allowance, preferences)
                        }
                    }
                    Divider()
                }
            }
        }
        if (stateAllowances.response == null || stateAllowances.error != ""){
            CustomLinearShimmer(enabled = true)
        }
    }
}

