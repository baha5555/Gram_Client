package com.example.gramclient.presentation.components

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainBottomSheet(
    navController: NavHostController,
    bottomSheetState: BottomSheetScaffoldState,
) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    var isTaxiState by remember { mutableStateOf(true) }
    val switchState = remember { mutableStateOf(true) }
    val tariffs = arrayOf("Эконом", "Комфорт", "Бизнес", "Минивэн")
    val tariffIcons = arrayOf(R.drawable.car_econom_pic, R.drawable.car_comfort_pic, R.drawable.car_business_pic, R.drawable.car_miniven_pic)
    val tariffListIcons = arrayOf(R.drawable.car_econom_icon, R.drawable.car_comfort_icon, R.drawable.car_business_icon, R.drawable.car_miniven_icon)

    var selectedTariff by remember {
        mutableStateOf(0)
    }


    Box(
        modifier = Modifier
            .fillMaxWidth().height(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(67.dp)
                .height(8.dp)
                .background(Color(0xFFA1ACB6), shape = RoundedCornerShape(50.dp))
                .padding(bottom = 7.dp),
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE2EAF2), shape = RoundedCornerShape(20.dp))
        ) {
            var isAnimated by remember { mutableStateOf(true) }
            val transition = updateTransition(targetState = isAnimated, label = "transition")

            val rocketOffset by transition.animateOffset(transitionSpec = {
                if (this.targetState) {
                    tween(1100) // launch duration

                } else {
                    tween(1100) // land duration
                }
            }, label = "rocket offset") { animated ->
                if (animated) Offset(0f, 0f) else Offset(0f, 172f)
            }
            isAnimated = bottomSheetState.bottomSheetState.isCollapsed


            Log.d("Bottom", "1=" + bottomSheetState.bottomSheetState.isCollapsed.toString())
            Box {
                Column(modifier = Modifier.offset(rocketOffset.x.dp, rocketOffset.y.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(20.dp))
                            .padding(15.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = tariffs[selectedTariff],
                                modifier = Modifier.padding(start = 10.dp),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 14.sp
                            )
                            Text(
                                text = "10 c",
                                modifier = Modifier.padding(end = 10.dp),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(89.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            if(!bottomSheetState.bottomSheetState.isCollapsed){
                                Image(
                                    modifier = Modifier.offset(0.dp, 10.dp).padding(horizontal = 30.dp),
                                    painter = painterResource(id = R.drawable.shape_car),
                                    contentDescription = null
                                )
                            }
                            Image(
                                modifier = Modifier.fillMaxWidth()
                                    .height(89.dp),
                                painter = painterResource(tariffIcons[selectedTariff]),
                                contentDescription = "icon"
                            )

                        }
//                if (bottomSheetState.bottomSheetState.isExpanded) {
                        Spacer(modifier = Modifier.height(15.dp))
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            repeat(4) {
                                item {
                                    TariffItem(
                                        icon = tariffListIcons[it],
                                        name = tariffs[it],
                                        price = 10,
                                        isSelected=selectedTariff==it,
                                        onSelected = { selectedTariff = it })
                                    Spacer(modifier = Modifier.width(10.dp))
                                }
                            }
//                    }
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(20.dp))
                            .padding(horizontal = 15.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Коментарий для водителя", fontSize = 16.sp)
                            Image(
                                modifier = Modifier.size(18.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                                contentDescription = "icon"
                            )
                        }
                        Divider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Запланировать поездку", fontSize = 16.sp)
                            Image(
                                modifier = Modifier.size(18.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                                contentDescription = "icon"
                            )
                        }
                        Divider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Заказ другому человеку", fontSize = 16.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(20.dp))
                            .padding(horizontal = 15.dp)
                    ) {
                        val allowances = arrayOf(
                            "Перевозка домашнего животного",
                            "Донести вещи, проводить",
                            "Поездка в тишине"
                        )
                        allowances.forEach {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = it, fontSize = 16.sp)
                                CustomSwitch(switchON = switchState)
                            }
                            Divider()
                        }
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFffffff), shape = RoundedCornerShape(20.dp))
                        .padding(vertical = 15.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .width(35.dp)
                                .height(35.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.from_marker),
                            contentDescription = "Logo"
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        TextField(
                            placeholder = { Text("Откуда?") },
                            value = text,
                            onValueChange = {
                                text = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Gray,
                                unfocusedIndicatorColor = Color.Gray,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .width(35.dp)
                                .height(35.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.to_marker),
                            contentDescription = "Logo"
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        TextField(
                            placeholder = { Text("Куда?") },
                            value = text,
                            onValueChange = {
                                text = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Gray,
                                unfocusedIndicatorColor = Color.Gray,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}
