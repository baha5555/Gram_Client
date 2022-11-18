package com.example.gramclient.presentation

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.presentation.components.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun MainScreen(navController: NavHostController, preferences: SharedPreferences) {

    val mainBottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scaffoldState = rememberScaffoldState()

    val paymentMethods = listOf("Наличные", "Картой", "С бонусного счета")
    val paymentState = remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetBackgroundColor = Color.White,
        scaffoldState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
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
                    Spacer(modifier = Modifier.height(20.dp))
                    paymentMethods.forEachIndexed { idx, item ->
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .clickable { paymentState.value = item }
                            .padding(vertical = 15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Row {
                                Image(
                                    modifier = Modifier.size(20.dp),
                                    imageVector = ImageVector.vectorResource(if (idx == 0) R.drawable.wallet_icon else if (idx == 1) R.drawable.payment_card_icon else R.drawable.bonus_icon),
                                    contentDescription = "Logo"
                                )
                                Spacer(modifier = Modifier.width(19.dp))
                                Text(text = item)
                            }
                            CustomCheckBox(size = 25.dp,
                                isChecked = paymentState.value == item,
                                onChecked = { paymentState.value = item })
                        }
                        Divider()
                    }
                }
                Spacer(modifier = Modifier.height(54.dp))
                CustomButton(modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .clip(RoundedCornerShape(12.dp)),
                    text = "Готово",
                    textSize = 20,
                    textBold = true,
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.bottomSheetState.collapse()
                        }
                    })
            }
        },
        sheetPeekHeight = 0.dp,
    ) {
        val pagerState = rememberPagerState(2)
        val scope = rememberCoroutineScope()
        val pageNum = remember {
            mutableStateOf(1)
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                count = 2,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) { page ->
                pageNum.value = page

                if (page == 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        SideBarMenu(navController, preferences)
                        Image(imageVector = ImageVector.vectorResource(id = R.drawable.ic_drawer),
                            contentDescription = "",
                            modifier = Modifier
                                .offset(43.dp, 0.dp)
                                .width(100.dp)
                                .clickable {
                                    if (pageNum.value == 1) scope.launch {
                                        pagerState.animateScrollToPage(0)
                                        pageNum.value = 0

                                    } else scope.launch {
                                        pagerState.animateScrollToPage(1)
                                        pageNum.value = 1
                                    }
                                })
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Scaffold(scaffoldState = scaffoldState, bottomBar = {
                            BottomBar(
                                navController, mainBottomSheetState, bottomSheetState
                            )
                        }) {
                            BottomSheetScaffold(
                                sheetBackgroundColor = Color.Transparent,
                                scaffoldState = mainBottomSheetState,
                                sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                                sheetContent = {
                                    MainBottomSheet(navController, mainBottomSheetState)
                                },
                                sheetPeekHeight = 360.dp,
                            ) {
                                CustomMap()
                            }
                        }
                        Image(imageVector = ImageVector.vectorResource(id = R.drawable.ic_drawer_blue),
                            contentDescription = "",
                            modifier = Modifier
                                .offset(-(47).dp, 0.dp)
                                .width(100.dp)
                                .rotate(180f)
                                .clickable {
                                    if (pageNum.value == 1) scope.launch {
                                        pagerState.animateScrollToPage(0)
                                        pageNum.value = 0
                                    }
                                    else scope.launch {
                                        pagerState.animateScrollToPage(1)
                                        pageNum.value = 1
                                    }
                                })

                    }
                }
            }
        }
    }
}







