package com.example.gramclient.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.gramclient.R
import com.example.gramclient.app.preference.CustomPreference
import com.example.gramclient.presentation.screens.authorization.AuthScreen
import com.example.gramclient.presentation.screens.order.SearchDriverScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomBar(
    mainBottomSheetState: BottomSheetScaffoldState,
    bottomSheetState: BottomSheetScaffoldState,
    createOrder: () -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope= rememberCoroutineScope()
    val isDialogOpen=remember{ mutableStateOf(false) }
    val prefs = CustomPreference(LocalContext.current)
    BottomAppBar(
        backgroundColor = Color(0xFFF7F7F7),
        contentColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = {
                coroutineScope.launch {
                    if(bottomSheetState.bottomSheetState.isCollapsed){
                        bottomSheetState.bottomSheetState.expand()
                    } else{
                        bottomSheetState.bottomSheetState.collapse()
                    }
                }
            }) {
                Image(
                    modifier = Modifier.size(30.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.cash_icon),
                    contentDescription = "icon"
                )
            }
            CustomButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black)
                    .width(260.dp)
                    .height(54.dp)
                    .padding(top = 0.dp),
                text = "Заказать",
                textSize = 18,
                textBold = true,
            onClick = {
                coroutineScope.launch {
                    if (prefs.getAccessToken() == "") {
                        navigator.replaceAll(AuthScreen())
                    } else {
                        isDialogOpen.value = true
                    }
                }
            })
            IconButton(onClick = {
                coroutineScope.launch {
                    if(mainBottomSheetState.bottomSheetState.isExpanded){
                        mainBottomSheetState.bottomSheetState.collapse()
                    } else{
                        mainBottomSheetState.bottomSheetState.expand()
                    }
                }
            }) {
                Image(
                    modifier = Modifier.size(if(mainBottomSheetState.bottomSheetState.isCollapsed) 30.dp else 20.dp),
                    imageVector = ImageVector.vectorResource(if(mainBottomSheetState.bottomSheetState.isCollapsed) R.drawable.options_icon else R.drawable.arrow_down),
                    contentDescription = "icon"
                )
            }
        }
        CustomDialog(
            text = "Оформить данный заказ?",
            okBtnClick = {
                coroutineScope.launch {
                    createOrder().let {
                        navigator.push(SearchDriverScreen())
                        isDialogOpen.value = false
                    }
                }
                         },
            cancelBtnClick = { coroutineScope.launch { isDialogOpen.value=false } },
            isDialogOpen = isDialogOpen.value
        )
    }
}