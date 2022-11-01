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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.ui.theme.BackgroundColor

@Composable
fun AuthorizationScreen(navController:NavHostController){

    val login = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(190.dp))
        Column(){
            Image(
                modifier= Modifier
                    .width(176.dp)
                    .height(50.07.dp),
                imageVector = ImageVector.vectorResource(R.drawable.logo_gram_black),
                contentDescription = "Logo"
            )
        }
        Spacer(modifier = Modifier.height(101.dp))
        Column(
        ){
            Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {
                        ""
                    },
                    placeholder = {
                        Text(text = "+992", fontSize = 18.sp)
                    },
                    modifier = Modifier
                        .padding(bottom = 40.dp)
                        .background(Color.White, shape = RoundedCornerShape(5.dp))
                        .fillMaxWidth(0.35f),
                    shape = RoundedCornerShape(5.dp),
                    enabled = false,
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Color.Black,
                        textColor = Color.Black,
                        disabledTextColor = Color.Black,
                        placeholderColor = Color.Black
                    ),
                    leadingIcon = {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.tj_flat),
                            contentDescription = "Проверено"
                        )
                    }
                )
                OutlinedTextField(
                    value = login.value,
                    onValueChange = {
                        login.value = it
                    },
                    placeholder = {
                        Text(text = "Телефон", fontSize = 18.sp)
                    },
                    textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(5.dp))
                        .fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(5.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF2264D1),
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Color(0xFF2264D1),
                        textColor = Color.Black,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }
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