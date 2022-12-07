package com.example.gramclient.presentation.mainScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramclient.R
import com.example.gramclient.presentation.components.*
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.PrimaryColor
import currentFraction

@OptIn(ExperimentalMaterialApi::class)
@Composable
 fun AddressSearchScreen (){

    val mainBottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        drawerContent = {
                        Column() {
                            Text(text = "Drawer")
                        }
        },
        sheetBackgroundColor = Color.White,
        scaffoldState = mainBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetGesturesEnabled = false,
        sheetContent = {
            AddressSearchBottomSheet()
        },
        sheetPeekHeight = 280.dp,
    ) {
        CustomMainMap()
    }
 }

@Composable
fun TopBar(){
    Column() {
        Text(text = "TopBar")
    }
}
@Composable
fun AddressSearchBottomSheet(heightFraction: Float = 0.95f){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = heightFraction)
                .background(Color.White)
                .padding(15.dp)
        ) {
            ToAddressField()
            Spacer(modifier = Modifier.height(15.dp))
            FastAddresses()
            Spacer(modifier = Modifier.height(15.dp))
            Services()
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
            .height(115.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { }
            .background(color = Color(0xFFCFF5FF), shape = RoundedCornerShape(20.dp))
            .padding(15.dp)
    ){
        Text(text = title, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = "15 мин", color = Color.Black, fontSize = 12.sp)
        Image(
            modifier = Modifier,
            painter = painterResource(R.drawable.car_econom_icon),
            contentDescription = "icon"
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
    Column(
        modifier = Modifier
            .width(147.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { }
            .border(border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(20.dp)),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = serviceName, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ToAddressField(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(percent = 50))
            .clickable { }
            .background(PrimaryColor, shape = RoundedCornerShape(percent = 50))
            .padding(vertical = 10.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start
        ){
            Image(
                modifier = Modifier,
                alignment = Alignment.Center,
                imageVector = ImageVector.vectorResource(id = R.drawable.car_econom_icon),
                contentDescription = "icon"
            )
            Text(text = "Куда едем?", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start
        ){
            Spacer(modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
                .background(Color.White))
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                modifier = Modifier,
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "icon",
                tint = Color.White
            )
        }
    }
}
