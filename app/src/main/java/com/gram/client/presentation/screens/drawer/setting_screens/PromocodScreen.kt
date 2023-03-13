package com.gram.client.presentation.screens.drawer.setting_screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.presentation.components.CustomButton
import com.gram.client.ui.theme.PrimaryColor

class PromocodScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
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
                                    text = "Оформление",
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
                        .fillMaxHeight(0.7f)
                        .background(PrimaryColor),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    GiftAnimation()
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
                        textStyle = TextStyle(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    CustomButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Поделиться: ${search.value}",
                        textSize = 18,
                        textBold = false
                    ) {

                    }

                }
            }


        }
    }
}

@Composable
fun GiftAnimation() {
    val animatedValue = remember { Animatable(0f) }
    val infiniteTransition = rememberInfiniteTransition()
    val animationSpec = TweenSpec<Float>(durationMillis = 500)

    LaunchedEffect(true) {
        while (true) {
            animatedValue.animateTo(
                targetValue = 1f,
                animationSpec = animationSpec
            )
            animatedValue.animateTo(
                targetValue = 0f,
                animationSpec = animationSpec
            )
        }
    }

    Box {
        Image(
            painter = painterResource(id = R.drawable.img_promo),
            contentDescription = "Gift",
            modifier = Modifier
                .size(width = 450.dp, height = 350.dp)
                .offset(
                    x = animatedValue.value.dp,
                    y = animatedValue.value.dp
                )
        )
    }
}