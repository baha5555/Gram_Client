package com.gram.client.presentation.screens.order

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
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
import com.gram.client.presentation.components.voyager.reason.Reason2Screen
import com.gram.client.presentation.screens.main.MainScreen
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.main.SearchAddressScreen
import com.gram.client.presentation.screens.main.components.FloatingButton2
import com.gram.client.presentation.screens.map.CustomMainMap
import com.gram.client.presentation.screens.map.currentRoute
import com.gram.client.presentation.screens.map.mLocationOverlay
import com.gram.client.presentation.screens.map.map
import com.gram.client.ui.theme.PrimaryColor
import com.gram.client.utils.Constants
import com.gram.client.utils.Constants.STATE_RAITING_ORDER_ID
import com.gram.client.utils.Constants.STATE_RATING
import com.gram.client.utils.Values
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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
        val bottomSheetState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
        )
        val scope = rememberCoroutineScope()
        var sheetPeekHeight by remember {
            mutableStateOf(200)
        }
        LaunchedEffect(key1 = orderExecutionViewModel.stateActiveOrders.value.response?.size == 0) {
            orderExecutionViewModel.getActiveOrders() {
                if (orderExecutionViewModel.stateActiveOrders.value.response?.isEmpty() == true && orderExecutionViewModel.stateActiveOrders.value.code == 200) {
                    navigator.replaceAll(SearchAddressScreen())
                }
            }
        }
        CustomBackHandle(true)
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            BackHandler(enabled = drawerState.isOpen) {
                scope.launch { drawerState.close() }
            }
            ModalDrawer(
                drawerState = drawerState,
                gesturesEnabled = !drawerState.isClosed,
                drawerContent = {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            SideBarMenu()
                        }
                    }
                },
                content = {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        BottomSheetScaffold(
                            sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
                            scaffoldState = bottomSheetScaffoldState,
                            floatingActionButton = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 25.dp, bottom = 50.dp),
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        FloatingButton2(
                                            ImageVector.vectorResource(id = R.drawable.btn_show_location),
                                            backgroundColor = Color.White,
                                            contentColor = PrimaryColor
                                        ) {
                                            map.controller.animateTo(mLocationOverlay.myLocation)
                                            if (mLocationOverlay.myLocation != null) {
                                                scope.launch {
                                                    if (currentRoute == SearchAddressScreen().key) {
                                                        mainViewModel.getAddressFromMap(
                                                            mLocationOverlay.myLocation.longitude,
                                                            mLocationOverlay.myLocation.latitude
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        FloatingButton2(
                                            Icons.Filled.Menu, backgroundColor = Color.White,
                                            contentColor = PrimaryColor
                                        ) {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    }

                                }
                            },
                            sheetContent = {
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
                                                        orderExecutionViewModel.stateActiveOrdersList.size == 1
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
                                                    inx = inx
                                                )
                                                Spacer(
                                                    Modifier.height(1.dp)
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
                                                modifier = Modifier.height(40.dp)
                                            )
                                        }
                                    }
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
                                                        Values.WhichAddress.value =
                                                            Constants.TO_ADDRESS
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
                                }

                            },
                            sheetPeekHeight = sheetPeekHeight.dp
                        ) {
                            CustomMainMap(mainViewModel = mainViewModel)
                        }
                    }
                })
        }


    }

    @SuppressLint("SimpleDateFormat", "CoroutineCreationDuringComposition", "NewApi")
    @Composable
    fun orderCard(
        orderExecutionViewModel: OrderExecutionViewModel,
        order: AllActiveOrdersResult,
        sheetPeekHeightUpOnClick: () -> Unit,
        isOpen: MutableState<Boolean>,
        inx: Int,
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
            if (STATE_RAITING_ORDER_ID.value != order.id) {
                STATE_RATING.value = false
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
                        text = if (order.performer == null || order.status == "Поступило") {"Ищем ближайших водителей..."}
                        else {
                            when (order.status) {
                                "Водитель на месте" -> "Водитель на месте,\n можете выходить"
                                "Исполняется" -> "За рулем ${order.performer?.first_name ?: "Водитель"}"
                                "Водитель назначен" -> {
                                    if (diff1.value > 0) { "Через ${diff1.value} мин приедет" } else { "В ближайшее время \n приедет ${order.performer?.first_name}" }
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