package com.example.gramclient.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.*
import com.example.gramclient.ui.theme.PrimaryColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderExecution(navController: NavHostController) {
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope= rememberCoroutineScope()
    var text by remember { mutableStateOf("") }
    val showMyLocation = remember{ mutableStateOf(false) }

    val isDialogOpen= remember{ mutableStateOf(false) }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetBackgroundColor= Color.Transparent,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
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
                       
                       Text(text = "5 мин и приедет", modifier = Modifier.fillMaxWidth(),
                           textAlign = TextAlign.Center,
                       fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                            ) 
                        {
                            Text(text = "серебристый Opel Astra")
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(
                                Modifier
                                    .background(
                                        Color(0xFFE2EAF2),
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .padding(vertical = 2.dp, horizontal = 12.dp)
                            ) {
                                Text(text = "4405", fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(19.dp))
                        Row(Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        )
                        {
                            Column(horizontalAlignment = Alignment.CenterHorizontally){
                                Card(modifier = Modifier
                                    .size(55.dp),
                                    elevation = 5.dp,
                                    shape = RoundedCornerShape(50.dp)
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        painter = painterResource(R.drawable.ava),
                                        contentDescription = "icon",
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "Акбар", color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.width(15.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally){
                                Card(modifier = Modifier
                                    .size(55.dp),
                                    elevation = 5.dp,
                                    shape = RoundedCornerShape(50.dp)
                                ) {
                                    IconButton(onClick = {
                                        /*...*/
                                    }) {
                                        Icon(
                                            modifier = Modifier
                                                .size(24.dp),
                                            painter = painterResource(R.drawable.phone_icon),
                                            contentDescription = "icon",
                                            tint = PrimaryColor
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "Позвонить", color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.width(15.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally){
                                Card(modifier = Modifier
                                    .size(55.dp),
                                    elevation = 5.dp,
                                    shape = RoundedCornerShape(50.dp)
                                ) {
                                    IconButton(onClick = {
                                        /*...*/
                                    }) {
                                        Icon(
                                            modifier = Modifier
                                                .size(24.dp),
                                            painter = painterResource(R.drawable.chat_icon),
                                            contentDescription = "icon",
                                            tint = PrimaryColor
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "Написать", color = Color.Gray)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFffffff), shape = RoundedCornerShape(20.dp))
                            .padding(top = 15.dp, bottom = 15.dp, start = 25.dp, end = 5.dp),
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Image(
                                modifier = Modifier
                                    .size(28.dp),
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Image(
                                modifier = Modifier
                                    .size(28.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.plus_icon),
                                contentDescription = "Logo"
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            TextField(
                                placeholder = { Text("Добавить остановку") },
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Image(
                                modifier = Modifier
                                    .size(28.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.to_marker),
                                contentDescription = "Logo"
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            TextField(
                                placeholder = { Text("Куда") },
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                modifier = Modifier
                                    .size(25.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.wallet_icon),
                                contentDescription = "Logo",
                                tint = Color(0xFF343B71)
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(modifier= Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                                    Column {
                                        Text(text = "Наличные")
                                        Text(text = "Способ оплаты", fontSize = 12.sp, color = Color.Gray)
                                    }
                                    Icon(
                                        modifier = Modifier
                                            .size(25.dp),
                                        imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                                        contentDescription = "Logo",
                                        tint = Color.Gray
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(color = Color(0x72111111))
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                modifier = Modifier
                                    .size(25.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.location_icon),
                                contentDescription = "Logo",
                                tint = Color(0xFF343B71)
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(end = 15.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                                    Text(text = "Показать водителю, где я")
                                    CustomSwitch(switchON = showMyLocation)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(color = Color(0x72111111))
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                modifier = Modifier
                                    .size(25.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.warning_icon),
                                contentDescription = "Logo",
                                tint = Color(0xFF343B71)
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(modifier= Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                                    Column {
                                        Text(text = "Стоимость поездки")
                                        Text(text = "11 сомон", fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFFffffff),
                                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                            )
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Card(modifier = Modifier
                                .size(55.dp),
                                elevation = 5.dp,
                                shape = RoundedCornerShape(50.dp)
                            ) {
                                IconButton(onClick = {
                                    isDialogOpen.value=true
                                }) {
                                    Image(
                                        modifier = Modifier
                                            .size(24.dp),
                                        painter = painterResource(R.drawable.cancel_icon),
                                        contentDescription = "icon",
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Отменить поездку", color = Color.Gray)
                        }
                    }
                }
            }
        },
        sheetPeekHeight = 205.dp,
    ) {
        CustomMap()
        CustomDialog(
            text = "Водитель уже найден! Вы уверены, что все равно хотите отменить поездку?",
            okBtnClick = {
                coroutineScope.launch {
                    isDialogOpen.value = false
                    sheetState.bottomSheetState.collapse()
                }
                         },
            cancelBtnClick = { isDialogOpen.value=false },
            isDialogOpen = isDialogOpen.value
        )
    }
}