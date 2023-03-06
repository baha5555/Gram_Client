package com.gram.client.presentation.components.voyager

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.valentinilk.shimmer.shimmer

class MapPointScreen(val whichScreen: String? = null) : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val WHICH_SCREEN = remember{
            mutableStateOf(whichScreen ?: "")
        }
        val mainViewModel: MainViewModel = hiltViewModel()
        val statePoint = mainViewModel.stateAddressPoint.value
        val navigator = LocalNavigator.currentOrThrow
        BottomSheetScaffold(
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetContent = {
                SheetContent(whichScreen){
                    statePoint.response.let {
                        when(whichScreen) {
                            Constants.FROM_ADDRESS-> {
                                Log.e("which","$whichScreen")
                                if (it == null) {
                                    mainViewModel.updateFromAddress(
                                        Address(
                                            "Метка на карте",
                                            -1,
                                            map.mapCenter.latitude.toString(),
                                            map.mapCenter.longitude.toString()
                                        )
                                    )
                                } else {
                                    mainViewModel.updateFromAddress(
                                        Address(
                                            address = it.name,
                                            id = it.id,
                                            address_lat = it.lat,
                                            address_lng = it.lng
                                        )
                                    )
                                }
                            }
                            Constants.TO_ADDRESS-> {
                                Log.e("which","->$whichScreen")
                                if (it == null) {
//                                    mainViewModel.updateToAddress(
//                                        Address(
//                                            "Метка на карте",
//                                            -1,
//                                            map.mapCenter.latitude.toString(),
//                                            map.mapCenter.longitude.toString()
//                                        )
//                                    )
                                    mainViewModel.clearToAddress()
                                } else {
                                    mainViewModel.updateToAddress(
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
                            mainViewModel.getAddressFromMap(
                                mLocationOverlay.myLocation.longitude,
                                mLocationOverlay.myLocation.latitude,
                                Constants.TO_ADDRESS
                            )
                        }
                    }
                }
            }) {
            CustomMainMap(WHICH_ADDRESS = WHICH_SCREEN ,mainViewModel = mainViewModel)
        }
    }
}
@Composable
fun SheetContent(whichScreen: String?,stateViews:Boolean = false,onClick: () -> Unit) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val orderExecutionViewModel:OrderExecutionViewModel = hiltViewModel()
    val statePoint = if(!stateViews) mainViewModel.stateAddressPoint.value else orderExecutionViewModel.stateAddressPoint.value
    Column(
        modifier = Modifier
            .height(200.dp)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Text(
                text = if(whichScreen == Constants.FROM_ADDRESS)"Точка назначения" else "Точка отправления",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
            Divider(modifier = Modifier.padding(vertical = 10.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .size(20.dp),
                imageVector = ImageVector.vectorResource(if(whichScreen == Constants.FROM_ADDRESS) R.drawable.from_marker else R.drawable.ic_to_address_marker),
                contentDescription = "Logo"
            )
            if(statePoint.isLoading){
                Box(modifier = Modifier
                    .shimmer()
                    .padding(start = 10.dp)){
                    Box(modifier = Modifier
                        .size(150.dp, 20.dp)
                        .background(Color.Gray))
                }
            }else{
                Text(text = ""+ (statePoint.response?.name ?: "Метка на карте"), fontSize = 18.sp, modifier = Modifier.padding(start = 10.dp))
            }
        }
        Button(
            onClick = {
                onClick.invoke()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = "Готово", fontSize = 20.sp)
        }
    }
}