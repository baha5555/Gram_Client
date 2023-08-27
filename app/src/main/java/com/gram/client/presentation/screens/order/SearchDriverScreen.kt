package com.gram.client.presentation.screens.order

import android.annotation.SuppressLint

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.app.preference.CustomPreference
import com.gram.client.domain.orderExecutionScreen.active.AllActiveOrdersResult
import com.gram.client.presentation.components.*
import com.gram.client.presentation.components.voyager.MapPointScreen
import com.gram.client.presentation.components.voyager.SearchAddresses
import com.gram.client.presentation.screens.main.MainScreen
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.main.SearchAddressScreen
import com.gram.client.presentation.screens.map.CustomMainMap
import com.gram.client.ui.theme.PrimaryColor
import com.gram.client.utils.Constants
import com.gram.client.utils.Constants.STATE_RATING
import com.gram.client.utils.Constants.STATE_RAITING_ORDER_ID
import com.gram.client.utils.Values
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

val orderCount = mutableStateOf(-1)

class SearchDriverScreen : Screen {
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val context = LocalContext.current
        val prefs = CustomPreference(context)
        val navigator = LocalNavigator.currentOrThrow
        val bottomNavigator = LocalBottomSheetNavigator.current

        val mainViewModel: MainViewModel = hiltViewModel()
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(
                initialValue = BottomSheetValue.Expanded
            )
        )
        val scope = rememberCoroutineScope()
        var sheetPeekHeight by remember {
            mutableStateOf(200)
        }
        LaunchedEffect(key1 = orderExecutionViewModel.stateActiveOrders.value.response?.size == 0){
            orderExecutionViewModel.getActiveOrders() {
                if (orderExecutionViewModel.stateActiveOrders.value.response?.isEmpty() == true && orderExecutionViewModel.stateActiveOrders.value.code == 200) {
                    navigator.replaceAll(SearchAddressScreen())
                }
            }
        }

        CustomBackHandle(true)
        Scaffold(backgroundColor = MaterialTheme.colors.background, bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 25.dp, topEnd = 25.dp
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .height(50.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colors.primary)
                        .clickable { navigator.replace(SearchAddressScreen()) },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                Values.WhichAddress.value = Constants.TO_ADDRESS
                                bottomNavigator.show(SearchAddresses({
                                    navigator.push(MainScreen())
                                }) {
                                    navigator.push(MapPointScreen())
                                })
                            }
                            .background(PrimaryColor)
                            .padding(horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.car_kuda_edem),
                            contentDescription = "car_eco",
                            modifier = Modifier.offset(x = -25.dp)
                        )
                        Text(
                            text = "Заказать ещё одну машину",
                            textAlign = TextAlign.Start,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1, overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 12.dp)
                        )
                        Row(
                            modifier = Modifier
                                .padding(end = 10.dp),
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
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        navigator.push(MainScreen())
                                    },
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "car_eco",
                                tint = Color.White
                            )
                        }

                    }
                }
                Spacer(Modifier.requiredHeight(0.dp))
            }
        }) {
            BottomSheetScaffold(
                sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
                scaffoldState = bottomSheetScaffoldState,
                sheetContent = {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(MaterialTheme.colors.secondary)
                    ) {

                        LazyColumn() {
                            orderExecutionViewModel.stateActiveOrders.value.response?.forEach{
                                item {
                                    val isOpen = remember {
                                                mutableStateOf(
                                                    true
                                                )
                                            }
                                    orderCard(
                                        orderExecutionViewModel,
                                        it,
                                        sheetPeekHeightUpOnClick = {
                                            scope.launch {
                                                isOpen.value =
                                                    !isOpen.value
                                                sheetPeekHeight =
                                                    if (isOpen.value) 367
                                                    else 200

                                            }
                                        },
                                        isOpen = isOpen,
                                    )
                                    Spacer(
                                        Modifier.height(10.dp)
                                    )
//                                    clientOrdersId.active_orders?.let { active ->
//                                        active.forEach { clientOrderId ->
//                                            val isOpen = remember {
//                                                mutableStateOf(
//                                                    true
//                                                )
//                                            }
//                                            if (order.id == clientOrderId) {
//
//                                            }
//                                        }
//                                    }
                                }
                            }
                            item {
                                Spacer(
                                    modifier = Modifier.height(120.dp)
                                )
                            }
                        }
                    }

                },
                sheetPeekHeight = sheetPeekHeight.dp
            ) {
                CustomMainMap(
                    mainViewModel = mainViewModel
                )
            }
        }
    }

    @SuppressLint("SimpleDateFormat", "CoroutineCreationDuringComposition", "NewApi")
    @Composable
    fun orderCard(
        orderExecutionViewModel: OrderExecutionViewModel,
        order: AllActiveOrdersResult,
        sheetPeekHeightUpOnClick: () -> Unit,
        isOpen: MutableState<Boolean>,
    ) {
        val navigator = LocalNavigator.currentOrThrow
        val bottomNavigator = LocalBottomSheetNavigator.current

        val context = LocalContext.current

        val dateFormatParse = SimpleDateFormat("yyyy-MM-dd HH:mm")
        var fillingTimeDateParse: Long by remember {
            mutableStateOf(0)
        }
        var diff: Long by remember {
            mutableStateOf(0)
        }

        var fillingTimeMinutes: Long by remember {
            mutableStateOf(0)
        }
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = true) {
            orderExecutionViewModel.getReasons()
        }
        scope.launch {
            if (STATE_RAITING_ORDER_ID.value != order.id) {
                STATE_RATING.value = false
            }
            order.filing_time?.let {
                fillingTimeDateParse = dateFormatParse.parse(it).time
                diff = (System.currentTimeMillis() - fillingTimeDateParse) * -1
                fillingTimeMinutes = diff / (60 * 1000) % 60
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
            if (order.performer == null) {
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
                        top = if (order.performer == null) 10.dp else 20.dp,
                        bottom = if (order.performer == null) 20.dp else 5.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.75f)) {
                    Text(
                        text = if (order.performer == null) "Ищем ближайших водителей..."
                        else {
                            when (order.status) {
                                "Водитель на месте" -> "Водитель на месте,\n можете выходить"
                                "Исполняется" -> "За рулем ${order.performer?.first_name ?: "Водитель"}"
                                "Водитель назначен" -> {
                                    if (fillingTimeMinutes > 0 && order.filing_time != null) "Через $fillingTimeMinutes мин приедет"
                                    else "В ближайшее время \n приедет ${order.performer.first_name}"
                                }
                                else -> {
                                    ""
                                }
                            }
                        }, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onBackground
                    )
                    Text(
                        text = if (order.performer == null) "Среднее время поиска водителя ≈ 4 мин" else "${order.performer.transport?.color ?: ""} ${order.performer.transport?.model ?: ""}",
                        fontSize = 14.sp
                    )
                }
                if (order.performer != null) {
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
            AnimatedVisibility(visible = isOpen.value) {
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center
                ) {

                        CustomCircleButton(
                            text = "Связаться",
                            icon = ImageVector.vectorResource(id = R.drawable.phone)
                        ) {
                            if(order.performer == null){
                                Toast.makeText(context,"Водитель еще не найден!",Toast.LENGTH_SHORT).show()
                            }else{
                                connectClientWithDriverIsDialogOpen.value = true
                            }
                        }

                    Spacer(modifier = Modifier.width(50.dp))
                    CustomCircleButton(text = "Детали", icon = Icons.Default.Menu) {
                        orderExecutionViewModel.updateSelectedOrder(order)
                        navigator.push(OrderExecutionScreen())
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
}