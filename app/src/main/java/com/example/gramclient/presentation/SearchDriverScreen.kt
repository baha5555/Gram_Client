package com.example.gramclient.presentation

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomMainMap
import com.example.gramclient.presentation.mainScreen.MainViewModel
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.FontSilver
import com.example.gramclient.ui.theme.PrimaryColor
import java.nio.file.Files.size

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchDriverScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel(),
    preferences: SharedPreferences
) {
    val searchDriverState = remember {
        mutableStateOf(true)
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Expanded
        )
    )

    BottomSheetScaffold(sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color(0xFFEEEEEE))
            ) {
                val showContentState = remember {
                    mutableStateOf(false)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(25.dp))
                        .background(Color.White)
                        .clickable {
                            showContentState.value = !showContentState.value
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 15.dp,
                                vertical = if (searchDriverState.value) 15.dp else 5.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = if (searchDriverState.value) "Рядом с вами 3 машины" else "Через 6 мин приедет",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (searchDriverState.value) "Выбираем подходящие" else "cерый Opel Astra",
                                fontSize = 13.sp
                            )

                        }
                        if (searchDriverState.value) {
                            Text(text = "00:00", fontSize = 15.sp)
                        } else {
                            Box {
                                Text(
                                    text = "0220KK",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .offset(0.dp, 18.dp)
                                        .background(BackgroundColor)
                                )
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_car),
                                    contentDescription = "car_eco",
                                    modifier = Modifier.offset(0.dp, 16.dp)
                                )
                            }
                        }
                    }
                    AnimatedVisibility(visible = showContentState.value) {
                        Divider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            if (searchDriverState.value) {
                                RoundedButton(
                                    text = "Отменить\nзаказ",
                                    icon = Icons.Default.Close
                                ) {
                                    //Log.d("clicked", "click")
                                }
                            } else {
                                RoundedButton(text = "Позвонить", icon = Icons.Default.Phone) {
                                    //Log.d("clicked", "click")
                                }
                            }
                            RoundedButton(text = "Детали", icon = Icons.Default.Menu) {
                                searchDriverState.value = !searchDriverState.value
                            }
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                        .background(Color.White)
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
                                    text = "Аренда авто",
                                    fontSize = 12.sp,
                                    modifier = Modifier.align(
                                        Alignment.Center
                                    )
                                )
                            }
                        }
                    }
                    Spacer(Modifier.requiredHeight(20.dp))
                }
            }
        }, sheetPeekHeight = 83.dp
    ) {
        CustomMainMap(navController=navController, mainViewModel=mainViewModel)
    }
}

@Composable
fun RoundedButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FloatingActionButton(
            onClick = { onClick.invoke() },
        ) {
            Icon(imageVector = icon, "", modifier = Modifier.size(30.dp))
        }
        Spacer(modifier = Modifier.requiredHeight(3.dp))
        Text(text = text, fontSize = 12.sp, color = FontSilver, textAlign = TextAlign.Center)
    }
}
