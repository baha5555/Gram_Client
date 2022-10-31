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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import com.example.gramclient.R
import com.example.gramclient.RoutesName

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
        Button(
            onClick = {
                login.value=login.value.trim()
                password.value=password.value.trim()
                navController.navigate(RoutesName.IDENTIFICATION_SCREEN)
            },
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(Color.Black)
                .width(303.dp)
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3F51B5), contentColor = Color.White),
            content = { Text(text = "Продолжить", fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 28.sp) },
        )
        Spacer(modifier = Modifier.height(80.dp))
        Column(modifier = Modifier.fillMaxSize().padding( vertical = 43.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "Подтверждая номер телефона, я соглашаюсь с ", color= Color.Gray)
            Text(text = " правилами работы сервиса и политикой\n" +
                    "    обработки персональных данных.", color= Color.Blue, modifier = Modifier.clickable {  })
        }
    }
}