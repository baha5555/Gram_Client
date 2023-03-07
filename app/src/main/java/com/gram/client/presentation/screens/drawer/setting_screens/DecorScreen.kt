package com.example.gramclient.presentation.screens.drawer.setting_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.gram.client.R
import com.gram.client.presentation.components.CustomButton
import com.gram.client.presentation.components.CustomCheckBox
import com.gram.client.utils.Constants
import kotlinx.coroutines.launch

class DecorScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        var modalBottomSheetValue: ModalBottomSheetState

        val modalSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded })
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()

        ModalBottomSheetLayout(
            sheetState = modalSheetState,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, start = 30.dp, end = 17.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Способ оплаты",
                            fontSize = 22.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Divider()
                        Text(
                            text = "Способ оплаты",
                            fontSize = 22.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Divider()
                    }
                }
            },
            sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
        ) {
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
                    Text(
                        text = "Цвета",
                        Modifier.padding(start = 20.dp, top = 20.dp, bottom = 25.dp),
                        fontSize = 18.sp
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .background(Color.Black, shape = RoundedCornerShape(50))
                                .clickable { }
                        )

                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .background(Color.Blue, shape = RoundedCornerShape(50))
                                .clickable { }
                        )
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .background(Color.Yellow, shape = RoundedCornerShape(50))
                                .clickable { }
                        )
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .background(Color.Red, shape = RoundedCornerShape(50))
                                .clickable { }
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        Text(text = "Стандарт")
                        Text(text = "Синий")
                        Text(text = "Желтый")
                        Text(text = "Красний")
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Divider(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .clickable { },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.theme_icon),
                                contentDescription = "theme_icon",
                                Modifier.padding(start = 20.dp, end = 12.dp)
                            )
                            Text(text = "Тема")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Системная")
                            IconButton(onClick = {
                               /* coroutineScope.launch {
                                    if (modalBottomSheetValue.isVisible) {
                                        modalBottomSheetValue.animateTo(ModalBottomSheetValue.Hidden)
                                    } else {
                                        modalBottomSheetValue.animateTo(ModalBottomSheetValue.Expanded)
                                    }
                                }*/
                            }) {
                                Image(
                                    modifier = Modifier.size(25.dp),
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_forward_blue),
                                    contentDescription = "theme_icon",

                                    )
                            }
                        }
                    }
                }
            }
        }
    }
}
