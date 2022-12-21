package com.example.gramclient.presentation

import android.content.SharedPreferences
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gramclient.PreferencesName
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.domain.orderExecutionScreen.Order
import com.example.gramclient.presentation.components.CustomCircleButton
import com.example.gramclient.presentation.components.CustomDialog
import com.example.gramclient.presentation.components.CustomMainMap
import com.example.gramclient.presentation.mainScreen.MainViewModel
import com.example.gramclient.presentation.orderScreen.OrderExecutionViewModel
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.PrimaryColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchDriverScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel(),
    preferences: SharedPreferences,
    orderExecutionViewModel: OrderExecutionViewModel,
) {
    val isDialogOpen = remember { mutableStateOf(false) }


    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Expanded
        )
    )

    LaunchedEffect(key1 = true){
        orderExecutionViewModel.getActiveOrders(token = preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString(), navController)
    }

    val stateActiveOrders by orderExecutionViewModel.stateActiveOrders


    BottomSheetScaffold(sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color(0xFFEEEEEE))
            ) {
                stateActiveOrders.response?.let { orders->
                    orders.forEach { order->
                        orderCard(orderExecutionViewModel, preferences, order, navController, isDialogOpen)
                        Spacer(Modifier.height(10.dp))
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                        .background(Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                            .height(50.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(PrimaryColor)
                            .clickable {
                               navController.navigate(RoutesName.SEARCH_ADDRESS_SCREEN){
                                   popUpTo(RoutesName.SEARCH_DRIVER_SCREEN) {
                                       inclusive = true
                                   }
                               }
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_car),
                                contentDescription = "car_eco",
                            )
                            Text(
                                text = "Заказать ещё одну машину",
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                        Row(
                            modifier = Modifier.padding(end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Divider(
                                color = Color.White,
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight(0.5f)
                                    .offset((-10).dp, 0.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "car_eco",
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }

                    }

                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        repeat(5) {
                            Box(
                                modifier = Modifier
                                    .size(150.dp, 30.dp)
                                    .padding(start = 15.dp)
                                    .border(
                                        width = 1.dp,
                                        color = PrimaryColor,
                                        shape = RoundedCornerShape(35.dp)
                                    )
                                    .clip(RoundedCornerShape(35.dp))
                            ) {
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_box),
                                    contentDescription = "ic_box"
                                )
                                Text(
                                    text = "Доставка", fontSize = 12.sp, modifier = Modifier.align(
                                        Alignment.Center
                                    )
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(150.dp, 30.dp)
                                    .padding(start = 15.dp)
                                    .border(
                                        width = 1.dp,
                                        color = PrimaryColor,
                                        shape = RoundedCornerShape(35.dp)
                                    )
                                    .clip(RoundedCornerShape(35.dp))
                            ) {
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_car_rent),
                                    contentDescription = "ic_box"
                                )
                                Text(
                                    text = "Аренда авто",
                                    fontSize = 12.sp,
                                    modifier = Modifier.align(
                                        Alignment.Center
                                    )
                                )
                            }
                        }
                    }
                    Spacer(Modifier.requiredHeight(20.dp))
                }
            }
        }, sheetPeekHeight = 83.dp
    ) {
        CustomMainMap(
            mainViewModel = mainViewModel,
            navController = navController,
            preferences = preferences
        )
    }
}

@Composable
fun orderCard(
    orderExecutionViewModel: OrderExecutionViewModel,
    preferences: SharedPreferences,
    order: Order,
    navController: NavHostController,
    isDialogOpen: MutableState<Boolean>
){
    val isOpen = remember{ mutableStateOf(false)}

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { isOpen.value = !isOpen.value }
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(20.dp))
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top=20.dp, bottom = if(order.performer == null) 20.dp else 5.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(){
                Text(text =  if(order.performer == null) "Рядом с вами 3 машины" else "Через 6 мин приедет", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text =  if(order.performer == null) "Выбираем подходящие" else "${order.performer.transport.color} ${order.performer.transport.model}", fontSize = 14.sp, color = Color.Black)
            }
            if(order.performer == null) {
                Text(text = "00:00", fontSize = 14.sp, color = Color.Black)
            }else{
                Box {
                    Text(
                        text = order.performer.transport.car_number,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .offset(0.dp, 0.dp)
                            .background(BackgroundColor)
                    )
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_car),
                        contentDescription = "car_eco",
                        modifier = Modifier.offset(0.dp, 10.dp)
                    )
                }
            }
        }
        if(isOpen.value){
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ){
                if(order.performer == null) {
                    CustomCircleButton(
                        text = "Отменить\nзаказ",
                        icon = Icons.Default.Close
                    ) {
                        isDialogOpen.value = true
                    }
                }else {
                    CustomCircleButton(
                        text = "Позвонить",
                        icon = Icons.Default.Phone
                    ) {
                        //Log.d("clicked", "click")
                    }
                }
                Spacer(modifier = Modifier.width(50.dp))
                CustomCircleButton(text = "Детали", icon = Icons.Default.Menu) {
                    orderExecutionViewModel.updateSelectedOrder(order)
                    navController.navigate(RoutesName.ORDER_EXECUTION_SCREEN)
                }
            }
        }
    }
    CustomDialog(
        text = "Вы уверены что хотите отменить заказ?",
        okBtnClick = {
            isDialogOpen.value = false
            orderExecutionViewModel.cancelOrder(preferences = preferences, order.id, navController)
        },
        cancelBtnClick = { isDialogOpen.value = false },
        isDialogOpen = isDialogOpen.value
    )
}