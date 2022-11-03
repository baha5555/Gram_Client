package com.example.gramclient.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomMap
import com.example.gramclient.presentation.components.CustomTab
import com.example.gramclient.presentation.components.TariffItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubmitOrderScreen(navController: NavHostController){

    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = { BottomBar(navController, bottomSheetState, scaffoldState) },
            drawerContent = { DrawerContent(drawerState)},
        ) {
            BottomSheetScaffold(
                sheetBackgroundColor= Color.Transparent,
                scaffoldState = bottomSheetState,
                sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                sheetContent = {
                    bottomSheetContent(navController, bottomSheetState)
                },
                sheetPeekHeight = 440.dp,
            ) {
                CustomMap()
            }
        }
}
@Composable
fun DrawerContent(drawerState: DrawerState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2264D1))
    ){
        TopAppBar(
          modifier = Modifier.height(80.dp),
            backgroundColor = Color.White
        ) {
            Row(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 12.dp, start = 21.dp, end = 21.dp),
                verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween){
                Image(
                    modifier = Modifier
                        .width(91.dp)
                        .height(28.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.logo_gram_black),
                    contentDescription = "",
                )
                Text(text = "Худжанд", fontSize = 18.sp, color = Color.Black)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.White, shape = RoundedCornerShape(50.dp))
                    .padding(15.dp),
                content = {
                    Image(
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.camera_plus),
                        contentDescription = "",
                    )
                }
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Ваше имя...", fontSize = 22.sp, color = Color.White)
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Добавьте почту...", fontSize = 18.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp),
        ){
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.phone_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "Позвонить оператору", fontSize = 18.sp, color = Color.White)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.star_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "Мои адреса", fontSize = 18.sp, color = Color.White)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.clock_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "История заказов", fontSize = 18.sp, color = Color.White)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.promocode_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "Промокоды", fontSize = 18.sp, color = Color.White)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.message_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "Поддержка", fontSize = 18.sp, color = Color.White)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.parametres_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "Параметры", fontSize = 18.sp, color = Color.White)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.about_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "О приложении", fontSize = 18.sp, color = Color.White)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.logout_icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(text = "Выход", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomBar(
    navController: NavHostController,
    bottomSheetState: BottomSheetScaffoldState,
    scaffoldState:  ScaffoldState
) {
    val coroutineScope= rememberCoroutineScope()
    BottomAppBar(
        backgroundColor = Color(0xFFF7F7F7),
        contentColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = {
                coroutineScope.launch {
                    if(bottomSheetState.bottomSheetState.isExpanded){
                        bottomSheetState.bottomSheetState.collapse()
                    } else{
                        bottomSheetState.bottomSheetState.expand()
                    }
                }
            }) {
                Image(
                    modifier = Modifier.size(30.dp),
                    imageVector = ImageVector.vectorResource(if(bottomSheetState.bottomSheetState.isCollapsed) R.drawable.options_icon else R.drawable.arrow_down),
                    contentDescription = "icon"
                )
            }
            Button(
                onClick = {
                        navController.navigate(RoutesName.MAIN_SCREEN)
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black)
                    .width(260.dp)
                    .height(54.dp)
                    .padding(top = 0.dp),
                enabled =  true ,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2264D1), contentColor = Color.White),
                content = { Text(text = "Заказать", fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 28.sp) },
            )
            IconButton(onClick = {
                coroutineScope.launch {
                    if(scaffoldState.drawerState.isClosed){
                        scaffoldState.drawerState.open()
                    } else{
                        scaffoldState.drawerState.close()
                    }
                }
            }) {
                Image(
                    modifier = Modifier.size(30.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.cash_icon),
                    contentDescription = "icon"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun bottomSheetContent(
    navController: NavHostController,
    bottomSheetState: BottomSheetScaffoldState
) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    var isTaxiState by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
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
                .fillMaxWidth()
                .background(Color(0xFFE2EAF2), shape = RoundedCornerShape(20.dp))
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFffffff), shape = RoundedCornerShape(20.dp))
                    .padding(vertical = 15.dp)
            ){
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
                            text = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor =  Color.Gray,
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
                            text = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor =  Color.Gray,
                            unfocusedIndicatorColor = Color.Gray,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                }
                if(bottomSheetState.bottomSheetState.isCollapsed){
                    Spacer(modifier = Modifier.height(15.dp))
                    CustomTab(){
                        isTaxiState=!isTaxiState
                    }
                    Spacer(modifier = Modifier.height(23.dp))
                    LazyRow(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)){
                        if(isTaxiState){
                            repeat(6){
                                item{
                                    TariffItem(icon = R.drawable.econom_car, name = "Эконом", price = 10)
                                    Spacer(modifier = Modifier.width(10.dp))
                                }
                            }
                        }else{
                            repeat(6){
                                item{
                                    TariffItem(icon = R.drawable.courier_icon, name = "Курьер", price = 10)
                                    Spacer(modifier = Modifier.width(10.dp))
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(20.dp))
                    .padding(15.dp)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = "Эконом", modifier = Modifier.padding(start = 10.dp), fontSize = 24.sp, fontWeight = FontWeight.Bold, lineHeight = 14.sp)
                    Text(text = "10 c", modifier = Modifier.padding(end = 10.dp), fontSize = 28.sp, fontWeight = FontWeight.Normal, lineHeight = 14.sp)
                }
                Spacer(modifier = Modifier.height(15.dp))
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(89.dp),
                    painter = painterResource(R.drawable.econom_pic),
                    contentDescription = "icon"
                )
                if(bottomSheetState.bottomSheetState.isExpanded) {
                    Spacer(modifier = Modifier.height(15.dp))
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        repeat(6) {
                            item {
                                TariffItem(icon = R.drawable.econom_car, name = "Эконом", price = 10)
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 15.dp)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
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
                    horizontalArrangement = Arrangement.SpaceBetween){
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
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = "Заказ другому человеку", fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 15.dp)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = "Перевозка домашнего животного", fontSize = 16.sp)
                    Switch(
                        checked = true,
                        onCheckedChange = { }
                    )
                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = "Донести вещи, проводить", fontSize = 16.sp)
                    Switch(
                        checked = true,
                        onCheckedChange = { }
                    )
                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = "Поездка в тишине", fontSize = 16.sp)
                    Switch(
                        checked = true,
                        onCheckedChange = { }
                    )
                }
            }
        }
    }
}