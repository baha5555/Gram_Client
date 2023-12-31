package com.gram.client.presentation.sheets

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gram.client.R
import com.gram.client.domain.orderExecutionScreen.active.AllActiveOrdersResult
import com.gram.client.presentation.components.CustomCircleButton
import com.gram.client.presentation.components.CustomDialog
import com.gram.client.presentation.components.voyager.reason.Reason2Screen
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.utils.Constants
import com.gram.client.utils.Routes
import com.gram.client.utils.Values
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

val isSelected = mutableStateOf(0)

@Composable
fun SearchDriverSheetContent(
    orderExecutionViewModel: OrderExecutionViewModel,
    navController: NavHostController
) {
    val bottomNavigator = LocalBottomSheetNavigator.current
    val scope = rememberCoroutineScope()

    val isCreated = remember {
        mutableStateOf(false)
    }
    if(orderExecutionViewModel.stateActiveOrdersList.size>0){
        isCreated.value = true
    } else if(orderExecutionViewModel.stateActiveOrdersList.size==0 && isCreated.value){
        isCreated.value = false
        navController.navigate(Routes.SEARCH_ADDRESS_SHEET){
            popUpTo(Routes.SEARCH_DRIVER_SHEET){
                inclusive = true
            }
        }
    }
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colors.secondary)
    ) {
        LazyColumn() {

            orderExecutionViewModel.stateActiveOrdersList.forEachIndexed { inx, it ->

                item {
                    val isOpen = remember {
                        mutableStateOf(
                            isSelected.value == inx
                        )
                    }
                    OrderCard(
                        orderExecutionViewModel,
                        it,
                        sheetPeekHeightUpOnClick = {
                            isSelected.value = inx
                        },
                        isSelected = isSelected,
                        inx = inx,
                        navController = navController
                    )
                    Spacer(
                        Modifier.height(1.dp)
                    )
                }
            }
            item {
                Spacer(
                    modifier = Modifier.height(100.dp)
                )
            }
        }
    }
}

@SuppressLint("SimpleDateFormat", "CoroutineCreationDuringComposition", "NewApi")
@Composable
fun OrderCard(
    orderExecutionViewModel: OrderExecutionViewModel,
    order: AllActiveOrdersResult,
    sheetPeekHeightUpOnClick: () -> Unit,
    isSelected: MutableState<Int>,
    inx: Int,
    navController: NavHostController,
) {
   // val navigator = LocalNavigator.currentOrThrow
    val bottomNavigator = LocalBottomSheetNavigator.current

    val context = LocalContext.current

    val dateFormatParse = SimpleDateFormat("yyyy-MM-dd HH:mm")
    var fillingTimeDateParse: Long by remember {
        mutableStateOf(0)
    }
    var diff: Long by remember {
        mutableStateOf(0)
    }
    var diff1 = remember {
        mutableStateOf(0)
    }
    val handler = Handler(Looper.getMainLooper())

    var fillingTimeMinutes: Long by remember {
        mutableStateOf(0)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        orderExecutionViewModel.getReasons()
    }
    scope.launch {
        if (Constants.STATE_RAITING_ORDER_ID.value != order.id) {
            Constants.STATE_RATING.value = false
        }
        order.filing_time?.let {
            fillingTimeDateParse = dateFormatParse.parse(it).time
            diff = (System.currentTimeMillis() - fillingTimeDateParse) * -1
            fillingTimeMinutes = diff / (60 * 1000) % 60
        }
    }
    scope.launch {
        order.filing_time_to_int?.let {
            diff1.value = it
            if (it != null && it > 0) {
                diff1.value = diff1.value - 1
                delay(10000)
            }
        }
    }
    val connectClientWithDriverIsDialogOpen = remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .clip(RoundedCornerShape(20.dp))
        .clickable { sheetPeekHeightUpOnClick() }
        .fillMaxWidth()
        .background(
            color = MaterialTheme.colors.background,
            shape = RoundedCornerShape(20.dp)
        )) {
        if (order.performer == null || order.status == "Поступило") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp, start = 20.dp, end = 20.dp
                    )
            ) {
                LinearProgressIndicator(
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(percent = 50))
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = if (order.performer == null || order.status == "Поступило") 10.dp else 20.dp,
                    bottom = if (order.performer == null || order.status == "Поступило") 20.dp else 5.dp,
                    start = 20.dp,
                    end = 20.dp
                ),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.75f)) {
                Text(
                    text = if (order.performer == null || order.status == "Поступило") {
                        "Ищем ближайших водителей..."
                    } else {
                        when (order.status) {
                            "Водитель на месте" -> "Водитель на месте,\n можете выходить"
                            "Исполняется" -> "За рулем ${order.performer?.first_name ?: "Водитель"}"
                            "Водитель назначен" -> {
                                if (diff1.value > 0) {
                                    "Через ${diff1.value} мин приедет"
                                } else {
                                    "В ближайшее время \n приедет ${order.performer?.first_name}"
                                }
                            }

                            else -> {
                                ""
                            }
                        }
                    }, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    text = if (order.performer == null || order.status == "Поступило") "Среднее время поиска водителя ≈ 4 мин" else "${order.performer.transport?.color ?: ""} ${order.performer.transport?.model ?: ""}",
                    fontSize = 14.sp
                )
            }
            if (order.performer != null && order.status != "Поступило") {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = order.performer.transport?.car_number ?: "",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .offset(0.dp, 0.dp)
                            .background(MaterialTheme.colors.secondary)
                            .padding(2.dp)
                    )
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_car),
                        contentDescription = "car_eco",
                        modifier = Modifier.offset(0.dp, 10.dp)
                    )
                }
            }
        }
        AnimatedVisibility(visible = isSelected.value == inx) {
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {

                CustomCircleButton(
                    text = if (order.status == "Не оформлен" || order.status == "Поступило") "Отменить\nзаказ" else "Связаться",
                    icon = if (order.status == "Не оформлен" || order.status == "Поступило") Icons.Default.Close else ImageVector.vectorResource(
                        id = R.drawable.phone
                    )
                ) {
                    if (order.status == "Не оформлен" || order.status == "Поступило") {
                        bottomNavigator.show(Reason2Screen(orderExecutionViewModel, order) {})
                    } else {
                        connectClientWithDriverIsDialogOpen.value = true
                    }
                }

                Spacer(modifier = Modifier.width(50.dp))
                CustomCircleButton(text = "Детали", icon = Icons.Default.Menu) {
                    Values.ActiveOrdersInx.value = inx
                    orderExecutionViewModel.updateSelectedOrder(order)
                    //navigator.push(OrderExecutionScreen())
                    navController.navigate(Routes.DETAIL_ACTIVE_ORDER_SHEET)
                }
            }
        }
    }

    CustomDialog(
        text = "Позвонить водителю?",
        okBtnClick = {
            connectClientWithDriverIsDialogOpen.value = false
            orderExecutionViewModel.connectClientWithDriver(
                order_id = order.id.toString()
            ) {
                Toast.makeText(context, "Ваш запрос принят.Ждите звонка.", Toast.LENGTH_SHORT)
                    .show()
            }
        },
        cancelBtnClick = { connectClientWithDriverIsDialogOpen.value = false },
        isDialogOpen = connectClientWithDriverIsDialogOpen.value
    )
}
