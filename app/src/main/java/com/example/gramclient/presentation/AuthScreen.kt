package com.example.gramclient.presentation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.ui.theme.BackgroundColor

@Composable
fun AuthorizationScreen(navController: NavHostController) {
    var login = remember { mutableStateOf(TextFieldValue(text = "")) }
    var pass = remember { mutableStateOf(TextFieldValue(text = "")) }
    Column(
        Modifier
            .fillMaxHeight()
            .background(color = BackgroundColor),
        Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())

        ) {
            var visible by remember { mutableStateOf(false) }

            val animationTime = 1500
            val animationDelayTime = 5

            val arrowStartLocation = Offset(0F, 100F)
            val arrowEndLocation = Offset(0F, 0F)

            LaunchedEffect(Unit) {
                visible = true
            }
            val arrowLocation by animateOffsetAsState(
                targetValue = if (visible) arrowEndLocation else arrowStartLocation,
                animationSpec = tween(
                    animationTime,
                    animationDelayTime,
                    easing = LinearOutSlowInEasing
                )
            )
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.logo_gram_black),
                "",
                modifier = Modifier.offset(arrowLocation.x.dp, arrowLocation.y.dp)
            )

            Column(modifier = Modifier.padding(start = 40.dp, end = 40.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var numberText by remember { mutableStateOf("") }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
                }

                CustomButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .height(61.dp),
                    text = "Продолжить",
                    textSize = 18,
                    textBold = true,
                ) {
                    navController.navigate(RoutesName.IDENTIFICATION_SCREEN){
                        popUpTo(RoutesName.AUTH_SCREEN) {
                            inclusive = true
                        }
                    }
                }

            }

        }
        Column(modifier = Modifier
            .padding(10.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "Подтверждая номер телефона, я соглашаюсь с ", color= Color.Gray)
            Text(text = " правилами работы сервиса и политикой\n" +
                    "    обработки персональных данных.", color= Color.Blue, modifier = Modifier.clickable {  })
        }
    }


}