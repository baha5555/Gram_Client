package com.example.gramclient.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomButton

@Composable
fun AuthorizationScreen(navController:NavHostController){

    val login = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xE1FFFFFF)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(190.dp))
        Column(){
            Image(
                modifier= Modifier
                    .width(176.dp)
                    .height(50.07.dp),
                bitmap = ImageBitmap.imageResource(com.example.gramclient.R.drawable.logo),
                contentDescription = "Logo"
            )
        }
        Spacer(modifier = Modifier.height(101.dp))
        Column(
        ){
            TextField(
                placeholder = { Text("Телефон") },
                textStyle = TextStyle(fontSize =  15.sp),
                modifier= Modifier
                    .width(303.dp)
                    .height(54.dp)
                    .clip(RoundedCornerShape(3.dp)),
                value = login.value, onValueChange = {login.value = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = Color.White, unfocusedIndicatorColor = Color.White,textColor = Color.Black, backgroundColor = Color(
                    0xFFECEBEB
                ), cursorColor = Color.Black)
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
        Spacer(modifier = Modifier.height(50.dp))
        CustomButton(
            text = "Продолжить",
            textSize = 18,
            textBold = true,
            width = 303,
            height = 54,
            radius = 12
        ) {
            navController.navigate(RoutesName.IDENTIFICATION_SCREEN)
        }
        Spacer(modifier = Modifier.height(80.dp))
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 43.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "Подтверждая номер телефона, я соглашаюсь с ", color= Color.Gray)
            Text(text = " правилами работы сервиса и политикой\n" +
                    "    обработки персональных данных.", color= Color.Blue, modifier = Modifier.clickable {  })
        }
    }
}