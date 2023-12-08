package com.gram.client.presentation.components

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
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.app.preference.CustomPreference
import com.gram.client.presentation.screens.authorization.AuthScreen
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.order.SearchDriverScreen
import com.gram.client.utils.Constants.IDENTIFY_TO_SCREEN
import com.gram.client.utils.Constants.stateOfDopInfoForDriver
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomBar(
    mainBottomSheetState: BottomSheetScaffoldState,
    modalBottomSheetValue: ModalBottomSheetState,
    createOrder: () -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope= rememberCoroutineScope()
    val isDialogOpen=remember{ mutableStateOf(false) }
    val prefs = CustomPreference(LocalContext.current)
    val mainViewModel: MainViewModel = hiltViewModel()
    BottomAppBar(
        backgroundColor = MaterialTheme.colors.background,
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
                stateOfDopInfoForDriver.value = "PAYMENT_METHOD"
                coroutineScope.launch {
                    if (modalBottomSheetValue.isVisible) {
                        modalBottomSheetValue.animateTo(ModalBottomSheetValue.Hidden)
                    } else {
                        modalBottomSheetValue.animateTo(ModalBottomSheetValue.Expanded)
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
                    .background(MaterialTheme.colors.primary)
                    .fillMaxWidth(0.8f)
                    .height(54.dp)
                    .padding(top = 0.dp),
                text = "Заказать",
                textSize = 18,
                textBold = true,
                isLoading = mainViewModel.stateCreateOrder.value.isLoading,
            onClick = {
                coroutineScope.launch {
                    if (prefs.getAccessToken() == "") {
                        IDENTIFY_TO_SCREEN = "MAINSCREEN"
                        navigator.push(AuthScreen())
                    } else {
                        createOrder()
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
                Icon(
                    modifier = Modifier.size(if(mainBottomSheetState.bottomSheetState.isCollapsed) 30.dp else 20.dp),
                    imageVector = ImageVector.vectorResource(if(mainBottomSheetState.bottomSheetState.isCollapsed) R.drawable.options_icon else R.drawable.arrow_down),
                    contentDescription = "icon",
                    tint = MaterialTheme.colors.primary
                )
            }
        }
        CustomDialog(
            text = "Оформить данный заказ?",
            okBtnClick = {
                coroutineScope.launch {
                    isDialogOpen.value = false
                    createOrder()
                }
            },
            cancelBtnClick = { coroutineScope.launch { isDialogOpen.value=false } },
            isDialogOpen = isDialogOpen.value
        )
    }
}