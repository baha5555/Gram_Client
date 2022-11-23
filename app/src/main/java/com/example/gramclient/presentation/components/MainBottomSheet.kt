package com.example.gramclient.presentation.components

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.gramclient.domain.mainScreen.TariffsResult
import com.example.gramclient.presentation.LoadingIndicator
import com.example.gramclient.presentation.mainScreen.MainViewModel
import com.example.gramclient.presentation.mainScreen.states.AllowancesResponseState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainBottomSheet(
    navController: NavHostController,
    bottomSheetState: BottomSheetScaffoldState,
    tariffs: List<TariffsResult>,
    stateAllowances: AllowancesResponseState,
    mainViewModel: Lazy<MainViewModel>,
    preferences: SharedPreferences,
) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    val switchState = remember { mutableStateOf(true) }
    val tariffIcons = arrayOf(R.drawable.car_econom_pic, R.drawable.car_comfort_pic, R.drawable.car_business_pic, R.drawable.car_miniven_pic, R.drawable.courier_icon)
    val tariffListIcons = arrayOf(R.drawable.car_econom_icon, R.drawable.car_comfort_icon, R.drawable.car_business_icon, R.drawable.car_miniven_icon, R.drawable.courier_icon)

    var selectedTariff by remember {
        mutableStateOf(tariffs[0])
    }


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
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                if (animated) Offset(0f, 0f) else Offset(0f, 172f)
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
                                text = selectedTariff.name,
                                modifier = Modifier.padding(start = 10.dp),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 14.sp
                            )
                            Text(
                                text = "10 c",
                                modifier = Modifier.padding(end = 10.dp),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 14.sp
                            )
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
                                painter = painterResource(if(selectedTariff.id==1) tariffIcons[0] else if(selectedTariff.id==2) tariffIcons[1] else if(selectedTariff.id==4) tariffIcons[2] else if(selectedTariff.id==5) tariffIcons[3] else tariffIcons[4]),
                                contentDescription = "icon"
                            )

                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(items = tariffs, itemContent = { tariff ->
                                TariffItem(
                                        icon = if(tariff.id==1) tariffListIcons[0] else if(tariff.id==2) tariffListIcons[1] else if(tariff.id==4) tariffListIcons[2] else if(tariff.id==5) tariffListIcons[3] else  tariffListIcons[4],
                                        name =tariff.name,
                                        price = 10,
                                        isSelected=selectedTariff==tariff,
                                        onSelected = {
                                            selectedTariff = tariff
                                            mainViewModel.value.getAllowancesByTariffId(
                                                preferences.getString(
                                                PreferencesName.ACCESS_TOKEN, "").toString(), selectedTariff.id)
                                        })
                                    Spacer(modifier = Modifier.width(10.dp))
                            })
                        }
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
                        LoadingIndicator(isLoading = stateAllowances.isLoading)
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
                                        CustomSwitch(switchON = switchState)
                                    }
                                    Divider()
                                }
                                Spacer(modifier = Modifier.height(100.dp))
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFffffff), shape = RoundedCornerShape(20.dp))
                        .padding(vertical = 15.dp)
                ) {
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
                            imageVector = ImageVector.vectorResource(R.drawable.from_marker),
                            contentDescription = "Logo"
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        TextField(
                            placeholder = { Text("Откуда?") },
                            value = text,
                            onValueChange = {
                                text = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Gray,
                                unfocusedIndicatorColor = Color.Gray,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
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
                        TextField(
                            placeholder = { Text("Куда?") },
                            value = text,
                            onValueChange = {
                                text = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Gray,
                                unfocusedIndicatorColor = Color.Gray,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}
