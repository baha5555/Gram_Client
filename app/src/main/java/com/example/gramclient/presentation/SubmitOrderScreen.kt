package com.example.gramclient.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.presentation.components.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubmitOrderScreen(navController: NavHostController){

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

        BottomSheetScaffold(
            sheetBackgroundColor= Color.White,
            scaffoldState = bottomSheetState,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetContent = {
                Column(
                    modifier = Modifier.fillMaxWidth().height(440.dp)
                        .padding(top = 15.dp, start = 30.dp, end = 17.dp, bottom = 20.dp)
                ){
                    Text(text = "Способ оплаты", fontSize = 22.sp, modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                }
            },
            sheetPeekHeight = 0.dp,
        ) {
            Scaffold(
                scaffoldState = scaffoldState,
                bottomBar = { BottomBar(navController, mainBottomSheetState, bottomSheetState) },
                drawerContent = { SideBarMenu(drawerState, navController)},
            ) {
                BottomSheetScaffold(
                    sheetBackgroundColor= Color.Transparent,
                    scaffoldState = mainBottomSheetState,
                    sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    sheetContent = {
                        MainBottomSheet(navController, mainBottomSheetState)
                    },
                    sheetPeekHeight = 440.dp,
                ) {
                    CustomMap()
                }
            }
        }

}






