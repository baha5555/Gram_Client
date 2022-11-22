package com.example.gramclient.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.presentation.components.CustomDialog
import com.example.gramclient.presentation.components.CustomMap

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchDriverScreen(navController: NavHostController) {
    val isDialogOpen = remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Идет поиск...", fontSize = 18.sp) }, backgroundColor = Color.White,
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
                    color = Color(0xFF5D6AFB)
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
            CustomMap()
            CustomDialog(
                text = "Вы дейтсвительно хотите отменить поиск?",
                okBtnClick = { isDialogOpen.value = false },
                cancelBtnClick = { isDialogOpen.value = false },
                isDialogOpen = isDialogOpen.value
            )
        }
    }
}
