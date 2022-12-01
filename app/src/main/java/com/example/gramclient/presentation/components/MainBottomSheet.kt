package com.example.gramclient.presentation.components

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.PreferencesName
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.presentation.mainScreen.MainViewModel
import com.example.gramclient.presentation.mainScreen.states.*
import com.example.gramclient.ui.theme.PrimaryColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainBottomSheet(
    navController: NavHostController,
    bottomSheetState: BottomSheetScaffoldState,
    stateTariffs: TariffsResponseState,
    stateAllowances: AllowancesResponseState,
    mainViewModel: Lazy<MainViewModel>,
    preferences: SharedPreferences,
    stateAddressByPoint: AddressByPointResponseState,
    stateSearchAddress: SearchAddressResponseState,
    stateCalculate: CalculateResponseState,
) {
    val context = LocalContext.current
    val switchState = remember { mutableStateOf(true) }
    val tariffIcons = arrayOf(R.drawable.car_econom_pic, R.drawable.car_comfort_pic, R.drawable.car_business_pic, R.drawable.car_miniven_pic, R.drawable.courier_icon)
    val tariffListIcons = arrayOf(R.drawable.car_econom_icon, R.drawable.car_comfort_icon, R.drawable.car_business_icon, R.drawable.car_miniven_icon, R.drawable.courier_icon)


    val address_from=mainViewModel.value.from_address.observeAsState()
    val address_to=mainViewModel.value.to_address.observeAsState()
    val selected_tariff=mainViewModel.value.selectedTariff?.observeAsState()


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(67.dp)
                .height(8.dp)
                .background(Color(0xFFA1ACB6), shape = RoundedCornerShape(50.dp))
                .padding(bottom = 7.dp),
        ) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE2EAF2), shape = RoundedCornerShape(20.dp))
        ) {
            var isAnimated by remember { mutableStateOf(true) }
            val transition = updateTransition(targetState = isAnimated, label = "transition")

            val rocketOffset by transition.animateOffset(transitionSpec = {
                if (this.targetState) {
                    tween(1100) // launch duration

                } else {
                    tween(1100) // land duration
                }
            }, label = "rocket offset") { animated ->
                if (animated) Offset(0f, -40f) else Offset(0f, 139f)
            }
            isAnimated = bottomSheetState.bottomSheetState.isCollapsed


            Log.d("Bottom", "1=" + bottomSheetState.bottomSheetState.isCollapsed.toString())
            Box {
                Column(modifier = Modifier.offset(rocketOffset.x.dp, rocketOffset.y.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(20.dp))
                            .padding(15.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = selected_tariff?.value!!.name,
                                modifier = Modifier.padding(start = 10.dp),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 14.sp
                            )
                            stateCalculate.response?.let {
                                Text(
                                    text = "${it.result.amount} c",
                                    modifier = Modifier.padding(end = 10.dp),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 14.sp
                                )
                            }
                            if (stateCalculate.response == null){
                                Text(
                                    text = "...",
                                    modifier = Modifier.padding(end = 10.dp),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 14.sp
                                )
                            }
                            if (stateCalculate.error != ""){
                                Text(
                                    text = "...",
                                    modifier = Modifier.padding(end = 10.dp),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(89.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            if(!bottomSheetState.bottomSheetState.isCollapsed){
                                Image(
                                    modifier = Modifier
                                        .offset(0.dp, 10.dp)
                                        .padding(horizontal = 30.dp),
                                    painter = painterResource(id = R.drawable.shape_car),
                                    contentDescription = null
                                )
                            }
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(89.dp),
                                painter = painterResource(if(selected_tariff?.value!!.id==1) tariffIcons[0] else if(selected_tariff.value!!.id==2) tariffIcons[1] else if(selected_tariff.value!!.id==4) tariffIcons[2] else if(selected_tariff.value!!.id==5) tariffIcons[3] else tariffIcons[4]),
                                contentDescription = "icon"
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))

                        CustomRectangleShimmer(stateTariffs.isLoading)

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
                                            price = 10,
                                            isSelected = selected_tariff?.value == tariff,
                                            onSelected = {
                                                mainViewModel.value.getAllowancesByTariffId(
                                                    preferences.getString(
                                                        PreferencesName.ACCESS_TOKEN, ""
                                                    ).toString(), selected_tariff?.value?.id ?: 1
                                                )
                                                mainViewModel.value.updateSelectedTariff(tariff, preferences)
                                            })
                                        Spacer(modifier = Modifier.width(10.dp))
                                    })
                                }
                            }
                        }
                        CustomRectangleShimmer(if(stateTariffs.error != "") true else false)
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(20.dp))
                            .padding(horizontal = 15.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Коментарий для водителя", fontSize = 16.sp)
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
                        Divider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Заказ другому человеку", fontSize = 16.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(20.dp))
                            .padding(horizontal = 15.dp)
                    ) {
                        CustomLinearShimmer(stateAllowances.isLoading)

                        stateAllowances.response?.let { allowances ->
                            if(allowances.size!=0){
                                allowances.forEach { allowance ->
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
                                        CustomSwitch(switchON = allowance.isSelected){
                                            mainViewModel.value.includeAllowance(allowance, preferences)
                                        }
                                    }
                                    Divider()
                                }
                                Spacer(modifier = Modifier.height(100.dp))
                            }
                        }
                        CustomLinearShimmer(if(stateTariffs.error != "") true else false)
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFffffff), shape = RoundedCornerShape(20.dp))
                        .padding(vertical = 15.dp)
                )
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Loader(isLoading = stateAddressByPoint.isLoading)
                        stateAddressByPoint.response?.let { address ->
                            Image(
                                modifier = Modifier
                                    .width(35.dp)
                                    .height(35.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.from_marker),
                                contentDescription = "Logo"
                            )
                            mainViewModel.value.updateFromAddress(Address(address.name, address.id, address.lat, address.lng))
                        }
                        if(stateAddressByPoint.error != ""){
                            Image(
                                modifier = Modifier
                                    .width(35.dp)
                                    .height(35.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.from_marker),
                                contentDescription = "Logo"
                            )
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        Column(modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .clickable{
                                navController.navigate("${RoutesName.SEARCH_ADDRESS_SCREEN}/fromAddress")
                            }
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(Color(0xFFE2EAF2), shape = RoundedCornerShape(50.dp))
                            .padding(10.dp)
                        ){
                            Text(address_from.value?.name ?: "")
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    address_to.value?.forEach{ address ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .width(35.dp)
                                    .height(35.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.to_marker),
                                contentDescription = "Logo"
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Column(modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .clickable{
                                    navController.navigate("${RoutesName.SEARCH_ADDRESS_SCREEN}/toAddress")
                                }
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(Color(0xFFE2EAF2), shape = RoundedCornerShape(50.dp))
                                .padding(10.dp)
                            ){
                                Text(address.name)
                            }
                        }
                    }
                }
            }
//            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}

@Composable
fun Loader(isLoading:Boolean){
    if(isLoading){
        CircularProgressIndicator(modifier = Modifier.size(35.dp), color = PrimaryColor)
    }
}
