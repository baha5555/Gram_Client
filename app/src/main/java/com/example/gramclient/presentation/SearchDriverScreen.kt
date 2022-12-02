package com.example.gramclient.presentation

import android.content.SharedPreferences
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomDialog
import com.example.gramclient.presentation.components.CustomMainMap
import com.example.gramclient.presentation.components.CustomPulseLoader
import com.example.gramclient.presentation.mainScreen.MainViewModel
import com.example.gramclient.ui.theme.PrimaryColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchDriverScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel= hiltViewModel(),
    preferences: SharedPreferences
) {

    val stateCreateOrder by mainViewModel.stateCreateOrder
    val stateCancelOrder by mainViewModel.stateCancelOrder

    val isDialogOpen = remember {
        mutableStateOf(false)
    }
    stateCreateOrder.response?.let { orderState->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Идет поиск...", fontSize = 18.sp) },
                    backgroundColor = Color.White,
                    actions = {
                        Text("ОТМЕНИТЬ", fontSize = 18.sp, color = Color.Red, modifier = Modifier
                            .padding(end = 15.dp)
                            .clickable {
                                isDialogOpen.value = true
                            })
                    }
                )
            },
        ) {
            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = BottomSheetState(BottomSheetValue.Expanded)
            )
            BottomSheetScaffold(
                scaffoldState = bottomSheetScaffoldState,
                sheetContent = {
                    Text(
                        text = "Ищем ближайших водителей...",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(20.dp)
                    )
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = PrimaryColor
                    )
                    Text(
                        text = "Среднее время поиска водителя: 1 мин",
                        fontSize = 15.sp,
                        modifier = Modifier.padding(20.dp),
                        color = Color(0xFF7A7A7A)
                    )
                },
                sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),

                ) {
                CustomMainMap(mainViewModel = mainViewModel)
                CustomDialog(
                    text = "Вы дейтсвительно хотите отменить заказ?",
                    okBtnClick = {
                            mainViewModel.cancelOrder(preferences, orderState.result)
                            isDialogOpen.value = false
                            navController.popBackStack()
                    },
                    cancelBtnClick = { isDialogOpen.value = false },
                    isDialogOpen = isDialogOpen.value
                )
            }
        }
    }
    if(stateCreateOrder.isLoading || stateCreateOrder.error != ""){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CustomPulseLoader(isLoading = stateCreateOrder.isLoading, dotSize = 25.dp)
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = stateCreateOrder.error, textAlign = TextAlign.Center, color = Color(0xFFE91E63))
                if(!stateCreateOrder.isLoading){
                    Text(text = "Повторить запрос", color = PrimaryColor,
                        modifier = Modifier.clickable {
                            mainViewModel.createOrder(preferences)
                        },
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
