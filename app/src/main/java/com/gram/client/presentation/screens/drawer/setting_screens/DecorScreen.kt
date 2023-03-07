package com.example.gramclient.presentation.screens.drawer.setting_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import com.example.gramclient.R

class DecorScreen : Screen {
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
                Text(text = "Цвета")
                Row(Modifier, horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .background(Color.Black, shape = RoundedCornerShape(0.5f))
                    )
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .background(Color.Blue, shape = RoundedCornerShape(0.5f))
                    )
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .background(Color.Yellow, shape = RoundedCornerShape(0.5f))
                    )
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .background(Color.Red, shape = RoundedCornerShape(0.5f))
                    )
                }
            }
        }
    }
}