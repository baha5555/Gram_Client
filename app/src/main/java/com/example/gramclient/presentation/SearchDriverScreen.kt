package com.example.gramclient.presentation

import android.content.SharedPreferences
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Atm
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomMainMap
import com.example.gramclient.presentation.mainScreen.MainViewModel
import com.example.gramclient.ui.theme.PrimaryColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchDriverScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel(),
    preferences: SharedPreferences
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Expanded
        )
    )
    BottomSheetScaffold(sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.40f)
                    .background(Color(0xFFEEEEEE))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.50f)
                        .clip(shape = RoundedCornerShape(25.dp))
                        .background(Color.White)
                        .align(Alignment.TopCenter)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.45f)
                        .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                        .background(Color.White)
                        .align(Alignment.BottomCenter)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                            .height(50.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(PrimaryColor)
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_car),
                            contentDescription = "car_eco",
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        Image(
                            imageVector = Icons.Default.Atm,
                            contentDescription = "car_eco",
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Divider(
                                color = Color.White,
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight(0.5f)
                                    .offset((-10).dp, 0.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "car_eco",
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "Заказать ещё одну машину",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .align(Alignment.Center)
                        )
                    }

                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        repeat(5) {
                            Box(
                                modifier = Modifier
                                    .size(150.dp, 30.dp)
                                    .padding(start = 15.dp)
                                    .border(
                                        width = 1.dp,
                                        color = PrimaryColor,
                                        shape = RoundedCornerShape(35.dp)
                                    )
                                    .clip(RoundedCornerShape(35.dp))
                            ) {
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_box),
                                    contentDescription = "ic_box"
                                )
                                Text(
                                    text = "Доставка", fontSize = 12.sp, modifier = Modifier.align(
                                        Alignment.Center
                                    )
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(150.dp, 30.dp)
                                    .padding(start = 15.dp)
                                    .border(
                                        width = 1.dp,
                                        color = PrimaryColor,
                                        shape = RoundedCornerShape(35.dp)
                                    )
                                    .clip(RoundedCornerShape(35.dp))
                            ) {
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_car_rent),
                                    contentDescription = "ic_box"
                                )
                                Text(
                                    text = "Аренда авто", fontSize = 12.sp, modifier = Modifier.align(
                                        Alignment.Center
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }) {
        CustomMainMap(navController = navController, mainViewModel = mainViewModel)
    }
}
