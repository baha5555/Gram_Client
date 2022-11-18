package com.example.gramclient.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.presentation.components.CustomTopBar
import com.example.gramclient.ui.theme.PrimaryColor

@Composable
fun PromoCodeScreen(navController: NavHostController) {
    Scaffold(topBar = { CustomTopBar(title = "Промокоды", navController = navController) }) {
        Column{
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .background(PrimaryColor),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.img_promo),
                    contentDescription = "Зимний лес",
                )
                Text(
                    "Приглашай друзей и\nполучай премиальные!",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Получай по 15 премиальных смн. за\nкаждого друга который установит\nприложение и совершит три\nпоездки.",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(20.dp)
            ) {
                Text(text = "Поделись своим кодом:", fontSize = 18.sp)
                val search = remember {
                    mutableStateOf("Y045KG")
                }
                TextField(
                    enabled = false,
                    value = search.value,
                    onValueChange = {
                        search.value = it
                    },
                    trailingIcon = {
                        if (search.value.isNotEmpty())
                            IconButton(
                                onClick = {

                                },
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_share_blue),
                                    contentDescription = "",
                                    tint = PrimaryColor,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        cursorColor = Color(0xFF005EFF),
                        focusedIndicatorColor = Color(0xFF005EFF),
                        leadingIconColor = Color(0xFF005EFF),
                        disabledTextColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.height(30.dp))
                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Поделиться",
                    textSize = 18,
                    textBold = false
                ) {

                }

            }
        }
    }
}