package com.gram.client.presentation.screens.promocod

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.domain.promocod.GetPromocodResponseState
import com.gram.client.presentation.components.CustomButton
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.ui.theme.PrimaryColor

class PromocodScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val promocodViewModel: PromocodViewModel = hiltViewModel()
        val promocod = promocodViewModel.statepromocod.value.response?.promo_code

        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier.fillMaxHeight(0.1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TopAppBar(
                        content = {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                IconButton(modifier = Modifier, onClick = { navigator.pop() }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_blue),
                                        contentDescription = ""
                                    )
                                }
                                Text(
                                    text = "Промокоды",
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.Center),
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                        elevation = 0.dp
                    )
                }
            }, backgroundColor = Color.White

        ) {
            Column {
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
                        placeholder = { Text(text = "Ввести промокод") },
                        trailingIcon = {
                            if (search.value.isNotEmpty())
                                IconButton(
                                    onClick = {

                                    },
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.arrow_right),
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
                        textStyle = TextStyle(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    CustomButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Поделиться: ${promocod }",
                        textSize = 18,
                        textBold = false
                    ) {

                    }

                }
            }


        }
    }
}