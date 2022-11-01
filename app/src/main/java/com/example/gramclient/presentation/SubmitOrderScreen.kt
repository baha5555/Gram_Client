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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomMap
import com.example.gramclient.presentation.components.TariffItem


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubmitOrderScreen(navController: NavHostController){
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )
    val scope = rememberCoroutineScope()

        Scaffold(
            backgroundColor = Color(0xFFF8F6F6),
            bottomBar = { BottomBar(navController) },
        ) {
            BottomSheetScaffold(
                scaffoldState = bottomSheetState,
                sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                sheetContent = {
                    bottomSheetContent(navController)
                },
                sheetPeekHeight = 350.dp,
            ) {
                CustomMap(LocalContext.current)
            }
        }
}


@Composable
fun BottomBar(navController: NavHostController) {
    BottomAppBar(
        backgroundColor = Color(0xFFF7F7F7),
        contentColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    navController.navigate(RoutesName.MAIN_SCREEN)
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.Black)
                    .width(278.dp)
                    .height(54.dp)
                    .padding(top = 0.dp),
                enabled =  true ,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3F51B5), contentColor = Color.White),
                content = { Text(text = "Подтвердить", fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 28.sp) },
            )
        }
    }
}

@Composable
fun bottomSheetContent(navController: NavHostController) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .width(65.dp)
                    .height(7.dp),
                bitmap = ImageBitmap.imageResource(R.drawable.rectangle),
                contentDescription = "Logo"
            )
        }
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
                bitmap = ImageBitmap.imageResource(R.drawable.from_marker),
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
                bitmap = ImageBitmap.imageResource(R.drawable.to_marker),
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
        Spacer(modifier = Modifier.height(23.dp))
        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)){
            repeat(6){
                item{
                    TariffItem(icon = R.drawable.econom_car, name = "Эконом", price = 10)
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }
}