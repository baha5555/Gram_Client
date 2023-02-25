package com.example.gramclient.presentation.screens.order

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.gramclient.R
import com.example.gramclient.app.preference.CustomPreference
import com.example.gramclient.domain.firebase.order.RealtimeDatabaseOrder
import com.example.gramclient.presentation.components.*
import com.example.gramclient.presentation.screens.main.MainViewModel
import com.example.gramclient.presentation.screens.main.SearchAddressScreen
import com.example.gramclient.presentation.screens.main.components.FloatingButton
import com.example.gramclient.presentation.screens.map.CustomMainMap
import com.example.gramclient.ui.theme.PrimaryColor
import com.example.gramclient.utils.Constants.STATE_RAITING
import com.example.gramclient.utils.Constants.STATE_RAITING_ORDER_ID
import com.example.gramclient.utils.Values
import kotlinx.coroutines.coroutineScope
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
        val stateRealtimeDatabaseOrders by orderExecutionViewModel.stateRealtimeOrdersDatabase
        val stateRealtimeClientOrderIdDatabase by orderExecutionViewModel.stateRealtimeClientOrderIdDatabase
        val stateActiveOrder = remember{orderExecutionViewModel.stateActiveOrders}
        val stateClientOrderId = stateRealtimeClientOrderIdDatabase.response?.observeAsState()?.value
        scope.launch {
            if (stateClientOrderId == null) {
                delay(1000)
                orderExecutionViewModel.getActiveOrders() {
                    Log.e("RESPONSEEEE","${stateActiveOrder.value.response} \n ${stateActiveOrder.value.code} \n ${stateActiveOrder.value.success}")
                    if (stateActiveOrder.value.response?.isEmpty() == true && stateActiveOrder.value.code == 200) {
                        navigator.replaceAll(SearchAddressScreen())
                    }
                }
            }
        }
        CustomBackHandle(drawerState.isClosed)

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            BackHandler(enabled = drawerState.isOpen) {
                scope.launch { drawerState.close() }
            }
            ModalDrawer(drawerState = drawerState,
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
                                    Row(verticalAlignment = Alignment.CenterVertically,modifier=Modifier.fillMaxWidth(0.8f)) {
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
                                        modifier = Modifier
                                            .padding(end = 10.dp)
                                            .fillMaxWidth(0.35f),
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
                                Spacer(Modifier.requiredHeight(0.dp))
                            }
                        }) {
                            BottomSheetScaffold(
                                sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
                                scaffoldState = bottomSheetScaffoldState,
                                floatingActionButton = {
                                    FloatingButton(
                                        icon = Icons.Filled.Menu
                                    ){
                                        scope.launch {
                                            drawerState.open()
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
                                        stateRealtimeDatabaseOrders.response?.let { response ->
                                            response.observeAsState().value?.let { orders ->
                                                orderCount.value = orders.size
                                                stateRealtimeClientOrderIdDatabase.response?.let { responseClientOrderId ->
                                                    responseClientOrderId.observeAsState().value?.let { clientOrdersId ->
                                                        LazyColumn() {
                                                            items(orders) { order ->
                                                                clientOrdersId.active_orders?.let { active ->
                                                                    active.forEach { clientOrderId ->
                                                                        val isOpen = remember {
                                                                            mutableStateOf(
                                                                                false
                                                                            )
                                                                        }
                                                                        if (order.id == clientOrderId) {
                                                                            orderCard(
                                                                                orderExecutionViewModel,
                                                                                order,
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
                                                                            Spacer(Modifier.height(10.dp)
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            item {
                                                                Spacer(
                                                                    modifier = Modifier.height(120.dp)
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
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
                })
        }
    }

    @SuppressLint("SimpleDateFormat", "CoroutineCreationDuringComposition", "NewApi")
    @Composable
    fun orderCard(
        orderExecutionViewModel: OrderExecutionViewModel,
        order: RealtimeDatabaseOrder,
        sheetPeekHeightUpOnClick: () -> Unit,
        isOpen: MutableState<Boolean>,
    ) {
        val navigator = LocalNavigator.currentOrThrow
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
        scope.launch {
            if (STATE_RAITING_ORDER_ID.value != order.id) {
                STATE_RAITING.value = false
            }
            order.filing_time?.let {
                fillingTimeDateParse = dateFormatParse.parse(it).time
                diff = (System.currentTimeMillis() - fillingTimeDateParse) * -1
                fillingTimeMinutes = diff / (60 * 1000) % 60
//            Log.e(TAG,"fillingTimeMinutes $fillingTimeMinutes")
            }
        }
        val cancelOrderIsDialogOpen = remember { mutableStateOf(false) }
        val connectClientWithDriverIsDialogOpen = remember { mutableStateOf(false) }
        Column(modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { sheetPeekHeightUpOnClick() }
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.background, shape = RoundedCornerShape(20.dp))) {
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
                Column(modifier=Modifier.fillMaxWidth(0.75f)) {
                    Text(
                        text = if (order.performer == null) "Ищем ближайших водителей..."
                        else {
                            when (order.status) {
                                "Водитель на месте" -> "Водитель на месте,\n можете выходить"
                                "Исполняется" -> "За рулем ${order.performer?.first_name ?: "Водитель"}"
                                "Водитель назначен" -> {
                                    if (fillingTimeMinutes > 0 && order.filing_time!=null) "Через $fillingTimeMinutes мин приедет"
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
                        text = if (order.performer == null) "Среднее время поиска водителя: 1 мин" else "${order.performer.transport?.color ?: ""} ${order.performer.transport?.model ?: ""}",
                        fontSize = 14.sp
                    )
                }
                    if (order.performer != null)  {
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
            if (isOpen.value) {
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (order.performer == null) {
                        CustomCircleButton(
                            text = "Отменить\nзаказ", icon = Icons.Default.Close
                        ) {
                            cancelOrderIsDialogOpen.value = true
                        }
                    } else {
                        CustomCircleButton(
                            text = "Связаться", icon = ImageVector.vectorResource(id = R.drawable.phone)
                        ) {
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
        CustomDialog(text = "Вы уверены что хотите отменить заказ?",
            okBtnClick = {
                cancelOrderIsDialogOpen.value = false
                orderExecutionViewModel.cancelOrder(order.id) {
                    orderExecutionViewModel.stateCancelOrder.value.response.let {
                        if(it == null ) return@cancelOrder
                        if(it.result[0].count==0){
                            navigator.replaceAll(SearchAddressScreen())
                        }
                    }
                }
            },
            cancelBtnClick = { cancelOrderIsDialogOpen.value = false },
            isDialogOpen = cancelOrderIsDialogOpen.value
        )
        CustomDialog(text = "Позвонить водителю?",
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

    @Composable
    fun PulseLoading(
        durationMillis: Int = 1000,
        maxPulseSize: Float = 300f,
        minPulseSize: Float = 50f,
        pulseColor: Color = Color(234, 240, 246),
        centreColor: Color = Color(66, 133, 244)
    ) {
        val infiniteTransition = rememberInfiniteTransition()
        val size by infiniteTransition.animateFloat(
            initialValue = minPulseSize,
            targetValue = maxPulseSize,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
        val alpha by infiniteTransition.animateFloat(
            initialValue = 1f, targetValue = 0f, animationSpec = infiniteRepeatable(
                animation = tween(durationMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .size(size.dp)
                    .align(Alignment.Center)
                    .alpha(alpha),
                backgroundColor = pulseColor,
                elevation = 0.dp
            ) {}
            Card(
                modifier = Modifier
                    .size(minPulseSize.dp)
                    .align(Alignment.Center),
                shape = CircleShape,
                backgroundColor = centreColor
            ) {}
        }
    }
}