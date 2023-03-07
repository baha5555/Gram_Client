package com.gram.client.presentation.screens.drawer.setting_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.gramclient.presentation.screens.drawer.setting_screens.DecorScreen
import com.gram.client.R
import com.gram.client.presentation.components.CustomSwitch

class SettingScreen : Screen {
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
                                    text = "Настройки",
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
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.White).padding(0.dp)
            ) {
                Column(Modifier.clickable {}) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.padding(vertical = 10.dp)) {
                            Text(text = "Регион", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = "Худжанд", fontSize = 15.sp, color = Color(0xFF565E66))
                        }
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_blue_2),
                            contentDescription = ""
                        )
                    }
                    Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 25.dp))
                }
                Column(Modifier.clickable {}) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.padding(vertical = 10.dp)) {
                            Text(
                                text = "Язык приложения",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(text = "Русский", fontSize = 15.sp, color = Color(0xFF565E66))
                        }
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_blue_2),
                            contentDescription = ""
                        )
                    }
                    Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 25.dp))

                }
                Column(Modifier.clickable {navigator.push(DecorScreen())}) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.padding(vertical = 10.dp)) {
                            Text(
                                text = "Оформление",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(text = "Светлое", fontSize = 15.sp, color = Color(0xFF565E66))
                        }
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_blue_2),
                            contentDescription = ""
                        )
                    }
                    Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 25.dp))
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 30.dp, top = 30.dp, bottom = 20.dp),
                    Arrangement.SpaceBetween,
                    Alignment.CenterVertically
                ) {
                    Text(text = "Геолокация", fontSize = 18.sp)
                    val switchON = remember {
                        mutableStateOf(false) // Initially the switch is ON
                    }
                    CustomSwitch(switchON) {}
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 30.dp, top = 20.dp, bottom = 20.dp),
                    Arrangement.SpaceBetween,
                    Alignment.CenterVertically
                ) {
                    Text(text = "Не звонить", fontSize = 18.sp)
                    val switchON = remember {
                        mutableStateOf(false) // Initially the switch is ON
                    }
                    CustomSwitch(switchON) {}
                }

            }
        }
    }
}



