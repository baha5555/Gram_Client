package com.gram.client.presentation.components.voyager

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.domain.mainScreen.Address
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.main.components.FloatingButton
import com.gram.client.presentation.screens.map.CustomMainMap
import com.gram.client.presentation.screens.map.mLocationOverlay
import com.gram.client.presentation.screens.map.map
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.ui.theme.PrimaryColor
import com.gram.client.utils.Constants

class OrderExecutionMapPointScreen (val whichScreen: String? = null) : Screen {
    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val WHICH_SCREEN = remember {
            mutableStateOf(whichScreen ?: "")
        }
        val mainViewModel: MainViewModel = hiltViewModel()
        val orderExecutionViewModel:OrderExecutionViewModel = hiltViewModel()
        val statePoint = orderExecutionViewModel.stateAddressPoint
        val navigator = LocalNavigator.currentOrThrow
        BottomSheetScaffold(
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetContent = {
                SheetContent(whichScreen, stateViews = true) {
                    statePoint.value.response.let {
                        when (whichScreen) {
                            Constants.FROM_ADDRESS -> {
                                Log.e("which", "$whichScreen")
                                if (it == null) {
                                    orderExecutionViewModel.updateFromAddress(
                                        Address(
                                            "Метка на карте",
                                            -1,
                                            map.mapCenter.latitude.toString(),
                                            map.mapCenter.longitude.toString()
                                        )
                                    )
                                } else {
                                    orderExecutionViewModel.updateFromAddress(
                                        Address(
                                            address = it.name,
                                            id = it.id,
                                            address_lat = it.lat,
                                            address_lng = it.lng
                                        )
                                    )
                                }
                            }
                            Constants.TO_ADDRESS -> {
                                Log.e("which", "->$whichScreen")
                                if (it == null) {
                                    orderExecutionViewModel.updateToAddress(
                                        Address(
                                            "Метка на карте",
                                            -1,
                                            map.mapCenter.latitude.toString(),
                                            map.mapCenter.longitude.toString()
                                        )
                                    )
                                } else {
                                    orderExecutionViewModel.updateToAddress(
                                        Address(
                                            address = it.name,
                                            id = it.id,
                                            address_lat = it.lat,
                                            address_lng = it.lng
                                        )
                                    )
                                }
                            }
                        }
                        orderExecutionViewModel.editOrder()
                    }
                    navigator.pop()
                }
            },
            sheetPeekHeight = 200.dp,
            floatingActionButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FloatingButton(
                        Icons.Filled.ArrowBack,
                        backgroundColor = Color.White,
                        contentColor = PrimaryColor
                    ) {
                        navigator.pop()
                    }
                    FloatingButton(
                        ImageVector.vectorResource(id = R.drawable.btn_show_location)
                    ) {
                        map.controller.animateTo(mLocationOverlay.myLocation)
                        if (mLocationOverlay.myLocation != null) {
                            orderExecutionViewModel.getAddressFromMap(
                                mLocationOverlay.myLocation.longitude,
                                mLocationOverlay.myLocation.latitude,
                                WHICH_SCREEN.value
                            )
                        }
                    }
                }
            }) {
            CustomMainMap(WHICH_ADDRESS = WHICH_SCREEN, mainViewModel = mainViewModel)
        }
    }
}
