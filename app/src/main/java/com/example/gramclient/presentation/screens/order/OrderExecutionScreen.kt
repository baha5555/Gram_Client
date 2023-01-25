package com.example.gramclient.presentation.screens.order

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.domain.realtimeDatabase.Order.RealtimeDatabaseOrder
import com.example.gramclient.presentation.components.*
import com.example.gramclient.presentation.screens.main.MainViewModel
import com.example.gramclient.presentation.screens.main.addressComponents.AddressList
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.FontSilver
import com.example.gramclient.ui.theme.PrimaryColor
import com.example.gramclient.utils.Constants
import com.example.gramclient.utils.Constants.STATE_RAITING
import com.example.gramclient.utils.Constants.STATE_RAITING_ORDER_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderExecution(
    navController: NavHostController,
    orderExecutionViewModel: OrderExecutionViewModel,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    val stateRealtimeDatabase = orderExecutionViewModel.stateRealtimeOrdersDatabase.value.response?.observeAsState()?.value
    val isDialogOpen = remember { mutableStateOf(false) }
    var showGrade by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val ratingState = remember {
        mutableStateOf(0)
    }
    LaunchedEffect(key1 = true){
        orderExecutionViewModel.readAllOrders()
    }
    var selectRealtimeDatabaseOrder:RealtimeDatabaseOrder by remember {
        mutableStateOf(RealtimeDatabaseOrder())
    }
    // searchState dependencies ->
    var WHICH_ADDRESS = remember{ mutableStateOf("") }
    val isAddressList= remember { mutableStateOf(true) }
    val searchText=remember{ mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val isSearchState = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var orderId by remember {
        mutableStateOf(-1)
    }
    //searchState dependencies <-

    val selectedOrder by orderExecutionViewModel.selectedOrder
    LaunchedEffect(key1 = true) {
        Log.e("ActiveOrdersResponse", selectedOrder.toString())

    }
    var stateCancelOrderText by remember {
        mutableStateOf("Водитель уже найден! Вы уверены, что все равно хотите отменить поездку?")
    }
    scope.launch {
        stateRealtimeDatabase.let { orders ->
            orders?.forEach { order ->
                if (order.id == selectedOrder.id) {
                    Log.e("Select Order", "$selectRealtimeDatabaseOrder")
                    selectRealtimeDatabaseOrder = order
                }
            }
        }
        selectRealtimeDatabaseOrder.from_address?.let {
            mainViewModel.updateFromAddress(it)
            Log.e("From_address","$it")
        }
        selectRealtimeDatabaseOrder.to_address?.let { to_Addresses->
            to_Addresses.forEach { address->
                mainViewModel.updateToAddress(0,address)
                Log.e("To_address","$address")
            }
        }
    }
    scope.launch {
        showGrade = when(selectedOrder.status){
            "Выполнен" -> true
            else -> { false }
        }
    }
    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetBackgroundColor = Color(0xFFffffff),
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(unbounded = true)
                    .background(BackgroundColor)
            ) {
                if(!isSearchState.value) {
                    scope.launch{
                        if(searchText.value != "")
                            searchText.value = ""
                    }
                    selectRealtimeDatabaseOrder.let { order ->
                            if (order.performer != null) {
                                    performerSection(performer = order, orderExecutionViewModel)
                            }
                        orderSection(order, scope, sheetState, isSearchState)
                        Spacer(modifier = Modifier.height(10.dp))
                        optionSection()
                        actionSection(cancelOrderOnClick = {
                            isDialogOpen.value = true
                            orderId = order.id
                            if(order.status == "Исполняется"){
                                stateCancelOrderText = "Вы не можете отменить активный заказ.\nЭто может сделать только оператор"
                            }
                        })
                    }
                }else{
                    searchSection(
                        searchText, focusRequester, isSearchState, sheetState, scope, orderExecutionViewModel,
                        navController, isAddressList, focusManager, mainViewModel
                    )
                }
            }
        },
        sheetPeekHeight = 210.dp,
    ) {
        Box {
            CustomMainMap(
                mainViewModel = mainViewModel,
                navController = navController
            )
            if(stateCancelOrderText=="Водитель уже найден! Вы уверены, что все равно хотите отменить поездку?") {
                CustomDialog(
                    text = stateCancelOrderText,
                    okBtnClick = {
                        coroutineScope.launch {
                            isDialogOpen.value = false
                            sheetState.bottomSheetState.collapse()
                            if (orderId != -1)
                                orderExecutionViewModel.cancelOrder(orderId, navController) {
                                    navController.popBackStack()
                                }
                        }
                    },
                    cancelBtnClick = { isDialogOpen.value = false },
                    isDialogOpen = isDialogOpen.value
                )
            }
            else {
                CustomCancelDialog(
                    text = stateCancelOrderText,
                    okBtnClick = {
                        coroutineScope.launch {
                            isDialogOpen.value = false
                        }
                    },
                    cancelBtnClick = {
                        coroutineScope.launch {
                            isDialogOpen.value = false
                        }
                    },
                    isDialogOpen = isDialogOpen.value
                )
            }
            if (STATE_RAITING.value) {
                var thumbUpClicked by remember {
                    mutableStateOf(false)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .offset(0.dp, 100.dp)
                        .padding(horizontal = 50.dp)
                        .clip(
                            RoundedCornerShape(30.dp)
                        )
                        .background(PrimaryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                    ) {
                        if (!thumbUpClicked) {
                            Text(
                                text = "Оцените поездку",
                                color = Color.White,
                                fontSize = 18.sp,
                                modifier = Modifier.offset(10.dp, (-8).dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                repeat(5) {
                                    Box(modifier = Modifier.clickable(indication = null,
                                        interactionSource = remember { MutableInteractionSource() }) {
                                        ratingState.value = it + 1
                                        scope.launch {
                                            delay(3000)
                                            thumbUpClicked = true
                                            orderExecutionViewModel.sendRating2(
                                                order_id = STATE_RAITING_ORDER_ID.value,
                                                add_rating = ratingState.value * 10
                                            )
                                            Log.d("balll", "" + ratingState.value * 10)
                                        }

                                    }) {
                                        if (it < ratingState.value) Image(
                                            imageVector = ImageVector.vectorResource(
                                                id = R.drawable.ic_default_star
                                            ),
                                            contentDescription = null,
                                        )
                                        else Image(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_outlined_star),
                                            contentDescription = null,
                                        )
                                    }

                                }
                            }
                        } else {
                            ratingState.value = 0
                            scope.launch {
                                delay(3000)
                                STATE_RAITING.value = false
                                navController.popBackStack()
                            }
                            Text(
                                text = "Спасибо за ваше участие \nв улучшении качества работы!",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun performerSection(
    performer: RealtimeDatabaseOrder,
    orderExecutionViewModel: OrderExecutionViewModel
    ){
    val context = LocalContext.current
    val connectClientWithDriverIsDialogOpen = remember{ mutableStateOf(false)}
    val DateformatParse = SimpleDateFormat("yyyy-MM-dd HH:mm")
    var fillingTimeDateParse: Long by remember{
        mutableStateOf(0)
    }
    var diff:Long by remember{
        mutableStateOf(0)
    }

    var fillingTimeMinutes:Long by remember{
        mutableStateOf(0)
    }
    val scope = rememberCoroutineScope()
    scope.launch {
        performer.filing_time?.let {
            fillingTimeDateParse = DateformatParse.parse(it).time
            diff = (System.currentTimeMillis()-fillingTimeDateParse)*-1
            fillingTimeMinutes = diff / (60 * 1000) % 60
            Log.e(Constants.TAG,"fillingTimeMinutes $fillingTimeMinutes")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                color = Color.White
            )
            .padding(20.dp)
    ){
        Text(
            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
            text = when(performer.status){
                "Водитель на месте"->"Водитель на месте,\n можете выходить"
                "Исполняется"->"За рулем ${performer.performer?.first_name?:"Водитель"}"
                "Водитель назначен"->{
                    if(fillingTimeMinutes>0)"Через $fillingTimeMinutes мин приедет ${performer.performer?.first_name}"
                    else "Скоро приедет ${performer.performer?.first_name}"
                }
                else -> {""}
            }, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
        ){
            Text(text = "${performer.performer?.transport?.color?:"Не указан"} ${performer.performer?.transport?.model?:"Не указан"}", fontSize = 16.sp, color = Color.Black)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                modifier = Modifier
                    .background(shape = RoundedCornerShape(3.dp), color = Color(0xFFF4B91D))
                    .padding(3.dp), textAlign = TextAlign.Center,
                text = performer.performer?.transport?.car_number?:"номер не указан", fontSize = 16.sp, color = Color.Black)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.width(80.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    "",
                    modifier = Modifier.size(55.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${performer.performer?.first_name}",
                    color = FontSilver,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
//            CustomCircleButton(text = "Сообщение", icon = Icons.Outlined.Message) { }
            Spacer(modifier = Modifier.width(20.dp))
            CustomCircleButton(text = "Связаться", icon = R.drawable.phone) {
                connectClientWithDriverIsDialogOpen.value = true
            }
        }
    }
    CustomDialog(
        text = "Позвонить водителю?",
        okBtnClick = {
            connectClientWithDriverIsDialogOpen.value = false
            orderExecutionViewModel.connectClientWithDriver(
                order_id = performer.id.toString()
            ) {
                Toast.makeText(context, "Ваш запрос принят.Ждите звонка.",Toast.LENGTH_SHORT).show()
            }
        },
        cancelBtnClick = { connectClientWithDriverIsDialogOpen.value = false },
        isDialogOpen = connectClientWithDriverIsDialogOpen.value
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun orderSection(
    order: RealtimeDatabaseOrder,
    scope: CoroutineScope,
    bottomSheetState: BottomSheetScaffoldState,
    isSearchState: MutableState<Boolean>
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(bottom = 10.dp)
    ){
        Row(
            modifier = Modifier
                .clickable {
//                    scope.launch {
//                        bottomSheetState.bottomSheetState.expand()
//                        isSearchState.value = true
//                        WHICH_ADDRESS.value = Constants.FROM_ADDRESS
//                    }
                }
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.from_marker),
                    contentDescription = "Logo"
                )
                Spacer(modifier = Modifier.width(20.dp))
//                Text(text = "Откуда?", maxLines = 1, overflow = TextOverflow.Ellipsis, color=Color.Gray)
                Text(text = order.from_address?.address ?: "Откуда?", maxLines = 1, overflow = TextOverflow.Ellipsis, color=Color.Black)
            }
            Image(
                modifier = Modifier.size(18.dp),
                imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                contentDescription = "icon"
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 55.dp, end = 15.dp)) {
            Divider()
        }
        Row(
            modifier = Modifier
                .clickable {
//                    scope.launch {
//                        bottomSheetState.bottomSheetState.expand()
//                        isSearchState.value = true
//                        WHICH_ADDRESS.value = Constants.FROM_ADDRESS
//                    }
                }
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.plus_icon),
                    contentDescription = "Logo"
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Добавить остановку", maxLines = 1, overflow = TextOverflow.Ellipsis, color=Color.Gray)
            }
            Image(
                modifier = Modifier.size(18.dp),
                imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                contentDescription = "icon"
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 55.dp, end = 15.dp)) {
            androidx.compose.material.Divider()
        }

        order.to_address?.forEach { address ->
            Row(
                modifier = Modifier
                    .clickable {
                        scope.launch {
                            bottomSheetState.bottomSheetState.expand()
                            isSearchState.value = true
                        }
                    }
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row() {
                    Image(
                        modifier = Modifier
                            .size(20.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.to_marker),
                        contentDescription = "Logo"
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = address.address,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black
                    )
                }
                Image(
                    modifier = Modifier.size(18.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                    contentDescription = "icon"
                )
            }
        }
    }
}
@Composable
fun optionSection(){

    val switch=remember{ mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(bottom = 10.dp)
    ){
        Row(
            modifier = Modifier
                .clickable {
//                    scope.launch {
//                        bottomSheetState.bottomSheetState.expand()
//                        isSearchState.value = true
//                        WHICH_ADDRESS.value = Constants.FROM_ADDRESS
//                    }
                }
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.wallet_icon),
                    contentDescription = "Logo"
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column(){
                    Text(text = "Наличные", maxLines = 1, overflow = TextOverflow.Ellipsis, color=Color.Black, fontSize = 18.sp)
                    Text(text = "Изменить способ оплаты", maxLines = 1, overflow = TextOverflow.Ellipsis, color=Color.Gray, fontSize = 14.sp)
                }
            }
            Image(
                modifier = Modifier.size(18.dp),
                imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                contentDescription = "icon"
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 55.dp, end = 15.dp)) {
            androidx.compose.material.Divider()
        }
        Row(
            modifier = Modifier
                .clickable {
//                        scope.launch {
//                            bottomSheetState.bottomSheetState.expand()
//                            isSearchState.value = true
//                            WHICH_ADDRESS.value = Constants.TO_ADDRESS
//                        }
                }
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row() {
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.location_icon),
                    contentDescription = "Logo"
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Показать водителю, где я", color = Color.Gray,
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
            Row(){
                CustomSwitch(switchON = switch) {

                }
                Spacer(modifier = Modifier.width(15.dp))
            }
        }
    }
}
@Composable
fun actionSection(cancelOrderOnClick:()->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .padding(20.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ){
        CustomCircleButton(text = "Отменить\nзаказ",
            icon = Icons.Default.Close,cancelOrderOnClick)
        Spacer(modifier = Modifier.width(20.dp))
        CustomCircleButton(text = "Отправить\nмаршрут",
            icon = R.drawable.share_icon) {
            //method
        }
        Spacer(modifier = Modifier.width(20.dp))
        CustomCircleButton(text = "Безопас-\nность",
            icon = R.drawable.safety_icon) {
            //method
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun searchSection(
    searchText: MutableState<String>,
    focusRequester: FocusRequester,
    isSearchState: MutableState<Boolean>,
    bottomSheetState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    orderExecutionViewModel: OrderExecutionViewModel,
    navController: NavHostController,
    isAddressList: MutableState<Boolean>,
    focusManager: FocusManager,
    mainViewModel: MainViewModel,
){
    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = searchText.value,
            onValueChange = { value ->
                searchText.value = value
                mainViewModel.searchAddress(value)
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
            leadingIcon = {
                IconButton(onClick = { /*navController.popBackStack()*/ }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                            .size(24.dp)
                    )
                }
            },
            trailingIcon = {
                if (searchText.value != "") {
                    IconButton(
                        modifier = Modifier.offset(x = (-40).dp),
                        onClick = {
                            searchText.value =
                                ""
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(vertical = 15.dp, horizontal = 40.dp)
                                .size(24.dp)
                        )
                    }
                }
            },
            placeholder = { Text(text = "Введите адрес для поиска") },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                cursorColor = Color.Black,
                leadingIconColor = Color.Black,
                trailingIconColor = Color.Black,
                backgroundColor = BackgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        Box(modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end = 20.dp)
            .clickable {
                scope.launch {
                    bottomSheetState.bottomSheetState.collapse()
                }
                isSearchState.value = false
            },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Spacer(
                    modifier =
                    Modifier
                        .width(1.dp)
                        .height(55.dp)
                        .padding(vertical = 10.dp)
                        .background(Color(0xFFE0DBDB))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Карта", fontSize = 14.sp, color = Color.Black, modifier = Modifier)
            }
        }
    }
    //Search response content
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
    )
    {
        AddressList(
            isVisible = isAddressList,
            address = searchText,
            focusManager = focusManager,
        ){address ->
            scope.launch {
                bottomSheetState.bottomSheetState.collapse()
            }
            isSearchState.value=false
                orderExecutionViewModel.editOrder(address.id)
            searchText.value = ""
        }
    }
}