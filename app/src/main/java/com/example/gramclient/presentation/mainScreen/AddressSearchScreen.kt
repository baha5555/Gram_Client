package com.example.gramclient.presentation.mainScreen

import android.content.SharedPreferences
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gramclient.Constants
import com.example.gramclient.PreferencesName
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.presentation.components.*
import com.example.gramclient.presentation.mainScreen.addressComponents.AddressList
import com.example.gramclient.presentation.mainScreen.states.SearchAddressResponseState
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.PrimaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
 fun AddressSearchScreen(
    navController: NavHostController,
    preferences: SharedPreferences,
    mainViewModel: MainViewModel
) {
    var isSearchState = remember{ mutableStateOf(false) }

    var WHICH_ADDRESS = remember{ mutableStateOf("") }

    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val coroutineScope= rememberCoroutineScope()
    val context = LocalContext.current

    var initialApiCalled by rememberSaveable { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }



    if (!initialApiCalled) {
        LaunchedEffect(Unit) {
            mainViewModel.getActualLocation(context, preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString())
            initialApiCalled = true
        }
    }

    val fromAddress=mainViewModel.from_address.observeAsState()
    val toAddress=mainViewModel.to_address.observeAsState()


    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl ) {
        ModalDrawer(
            drawerState = drawerState,
            gesturesEnabled = !drawerState.isClosed,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        SideBarMenu(navController, preferences)
                    }
                } },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr ) {
                    BottomSheetScaffold(
                        modifier = Modifier.fillMaxSize(),
                        floatingActionButton = {
                            FloatingButton(scope = coroutineScope, drawerState = drawerState, bottomSheetState=bottomSheetState)
                        },
                        drawerGesturesEnabled = false,
                        sheetBackgroundColor = Color.White,
                        scaffoldState = bottomSheetState,
                        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        sheetGesturesEnabled = !bottomSheetState.bottomSheetState.isCollapsed,
                        sheetContent = {
                            AddressSearchBottomSheet(
                                navController=navController, isSearchState=isSearchState,
                                preferences=preferences, mainViewModel=mainViewModel,
                                bottomSheetState = bottomSheetState,
                                focusRequester=focusRequester,
                                coroutineScope = coroutineScope,
                                WHICH_ADDRESS=WHICH_ADDRESS,
                                toAddress=toAddress
                            )
                        },
                        sheetPeekHeight = 280.dp,
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        )
                        {
                            CustomMainMap(navController=navController, mainViewModel = mainViewModel)
                            FromAddressField(fromAddress) {
                                coroutineScope.launch {
                                    bottomSheetState.bottomSheetState.expand()
                                    isSearchState.value=true
                                    WHICH_ADDRESS.value=Constants.FROM_ADDRESS
                                }
                            }
                        }
                    }
                }
            }
        )
    }

 }

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FloatingButton(
    scope: CoroutineScope,
    drawerState: DrawerState,
    bottomSheetState: BottomSheetScaffoldState
){
    FloatingActionButton(
        modifier = Modifier
            .size(50.dp)
            .offset(y = if (bottomSheetState.bottomSheetState.isCollapsed) (-35).dp else (-65).dp),
        backgroundColor = PrimaryColor,
        onClick = {
            scope.launch {
                drawerState.open()
            }
        }
    ) {
        Icon(Icons.Filled.Menu,
            contentDescription = "Menu", tint = Color.White,
            modifier = Modifier.size(25.dp)
        )
    }
}

@Composable
fun FromAddressField(fromAddress: State<Address?>, onClick: ()-> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier
                .width(177.dp)
                .height(50.dp)
                .clip(shape = RoundedCornerShape(percent = 50))
                .clickable {
                    onClick()
                }
                .background(
                    Color.Black,
                    shape = RoundedCornerShape(percent = 50)
                )
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(text = "Ваш адрес", color=Color.White, fontSize = 11.sp)
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    modifier = Modifier,
                    painter = painterResource(R.drawable.arrow_right),
                    contentDescription = "icon",
                    tint = Color.White
                )
            }
            Text(text = fromAddress.value?.name ?: "",
                color=Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddressSearchBottomSheet(
    heightFraction: Float = 0.98f,
    navController: NavHostController,
    isSearchState: MutableState<Boolean>,
    preferences: SharedPreferences,
    mainViewModel: MainViewModel,
    bottomSheetState: BottomSheetScaffoldState,
    focusRequester: FocusRequester,
    coroutineScope: CoroutineScope,
    WHICH_ADDRESS: MutableState<String>,
    toAddress: State<MutableList<Address>?>
){
    val searchText=remember{ mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val isAddressList= remember { mutableStateOf(true) }
    val stateSearchAddress by mainViewModel.stateSearchAddress

    Column(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .fillMaxHeight(fraction = heightFraction)
                .background(Color.White)
                .padding(15.dp)
        ) {
            if(!isSearchState.value){
                ToAddressField(navController, WHICH_ADDRESS=WHICH_ADDRESS, toAddress=toAddress,
                    isSearchState=isSearchState, bottomSheetState=bottomSheetState, scope=coroutineScope
                )
                Spacer(modifier = Modifier.height(15.dp))
                FastAddresses()
                Spacer(modifier = Modifier.height(15.dp))
                Services()
            }else{
                LaunchedEffect(Unit) {
                    delay(200)
                    focusRequester.requestFocus()
                }
                SearchTextField(searchText = searchText, preferences = preferences, navController = navController, focusRequester = focusRequester)
                SearchResultContent(
                    searchText = searchText,
                    focusManager = focusManager,
                    navController = navController,
                    isAddressList = isAddressList,
                    stateSearchAddress = stateSearchAddress,
                    bottomSheetState=bottomSheetState,
                    isSearchState=isSearchState,
                    scope = coroutineScope,
                    mainViewModel = mainViewModel,
                    WHICH_ADDRESS=WHICH_ADDRESS
                )
            }
        }
}

@Composable
fun FastAddresses(){
    LazyRow(modifier = Modifier.fillMaxWidth()){
        repeat(5){
            item { 
                FastAddressCard(title = "Панчшанбе Базар")
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
fun FastAddressCard(
    title:String
){
    Column(
        modifier = Modifier
            .width(115.dp)
//            .height(115.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { }
            .background(color = Color(0xFFCFF5FF), shape = RoundedCornerShape(20.dp))
            .padding(top = 15.dp, start = 15.dp, end = 15.dp)
    ){
        Text(text = title, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = "15 мин", color = Color.Black, fontSize = 12.sp)
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_car),
            contentDescription = "car_eco",
        )
    }
}

@Composable
fun Services(){
    LazyRow(modifier = Modifier.fillMaxWidth()){
        repeat(5){
            item {
                ServicesCard(serviceName = "Доставка")
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
    }
}

@Composable
fun ServicesCard(
    serviceName:String
){
    Row(
        modifier = Modifier
            .width(147.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { }
            .border(border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(20.dp)),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start
    ){
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_box),
            contentDescription = "ic_box"
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = serviceName, color = Color.Black, fontSize = 14.sp)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToAddressField(
    navController: NavHostController,
    WHICH_ADDRESS: MutableState<String>,
    isSearchState: MutableState<Boolean>,
    bottomSheetState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    toAddress: State<MutableList<Address>?>
) {
    toAddress.value?.forEach { address ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable {
                    scope.launch {
                        bottomSheetState.bottomSheetState.expand()
                        isSearchState.value = true
                        WHICH_ADDRESS.value = Constants.TO_ADDRESS
                    }
                }
                .background(PrimaryColor)
                .padding(horizontal = 5.dp)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_car),
                contentDescription = "car_eco",
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
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
                            navController.navigate(RoutesName.MAIN_SCREEN)
                        },
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "car_eco",
                    tint = Color.White
                )
            }
            Text(
                text = address.name,
                textAlign = TextAlign.Start,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.Center)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchResultContent(
    searchText: MutableState<String>,
    focusManager: FocusManager,
    navController: NavHostController,
    isAddressList: MutableState<Boolean>,
    stateSearchAddress: SearchAddressResponseState,
    bottomSheetState: BottomSheetScaffoldState,
    isSearchState: MutableState<Boolean>,
    scope: CoroutineScope,
    mainViewModel: MainViewModel,
    WHICH_ADDRESS: MutableState<String>
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
    )
    {
        AddressList(
            navController = navController,
            isVisible = isAddressList,
            address = searchText,
            focusManager = focusManager,
        ){address ->
            scope.launch {
                bottomSheetState.bottomSheetState.collapse()
            }
            isSearchState.value=false
            when(WHICH_ADDRESS.value){
                Constants.FROM_ADDRESS -> { mainViewModel.updateFromAddress(address) }
                Constants.TO_ADDRESS -> {
                    mainViewModel.updateToAddress(0, address)
                    navController.navigate(RoutesName.MAIN_SCREEN)
                }
            }
        }
    }
}

@Composable
fun SearchTextField(
    searchText: MutableState<String>,
    mainViewModel: MainViewModel= hiltViewModel(),
    preferences: SharedPreferences,
    navController: NavHostController,
    focusRequester: FocusRequester
) {

    TextField(
        value = searchText.value,
        onValueChange = { value ->
            searchText.value = value
            mainViewModel.searchAddress(preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString(), value)
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
                        .padding(15.dp)
                        .size(24.dp)
                )
            }
        },
        trailingIcon = {
            if (searchText.value != "") {
                IconButton(
                    onClick = {
                        searchText.value =
                            ""
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        placeholder= {Text(text = "Введите адрес для поиска")},
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
}
