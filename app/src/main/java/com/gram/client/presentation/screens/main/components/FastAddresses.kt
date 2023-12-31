package com.gram.client.presentation.screens.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gram.client.R
import com.gram.client.presentation.components.voyager.SearchAddresses
import com.gram.client.presentation.screens.drawer.myaddresses_screen.MyAddressViewModel
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.utils.Constants
import com.gram.client.utils.Routes
import com.gram.client.utils.Values
import com.gram.client.utils.getAddressText
import com.valentinilk.shimmer.shimmer

@Composable
fun FastAddresses(mainViewModel: MainViewModel, navController: NavHostController) {
    val myAddressViewModel: MyAddressViewModel = hiltViewModel()
    val stateMyAddresses = myAddressViewModel.stateGetAllMyAddresses.value
    LaunchedEffect(key1 = true) {
        if (mainViewModel.stateFastAddress.value.response == null) {
            mainViewModel.getFastAddresses()
        }
    }
    val bottomNavigator = LocalBottomSheetNavigator.current
    val stateFastAddresses = mainViewModel.stateFastAddress.value
    if (stateFastAddresses.isLoading) {
        Row(Modifier.shimmer()) {
            repeat(5) {
                Column(
                    modifier = Modifier
                        .size(115.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = Color(0xFF9C9C9C))
                        .padding(top = 15.dp, start = 15.dp, end = 15.dp)
                ) {
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    } else
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            stateMyAddresses.response?.result?.home.let {
                item {
                    if(it==null) return@item
                    FastAddressCard("Дом", it[0].address.city, icon = R.drawable.ic_home) {
                        mainViewModel.clearToAddress()
                        mainViewModel.addToAddress(it[0].address)
                        if (mainViewModel.fromAddress.value.name != "") {
                            navController.navigate(Routes.CREATE_ORDER_SHEET)
                        } else {
                            Values.WhichAddress.value = Constants.ADD_FROM_ADDRESS_FOR_NAVIGATE
                            bottomNavigator.show(
                                SearchAddresses({
                                    navController.navigate(Routes.CREATE_ORDER_SHEET)
                                }) {
                                    navController.navigate(Routes.MAP_POINT_SHEET)
                                }
                            )
                        }
                        //map.controller.animateTo(GeoPoint(it.address_lat.toDouble(), it.address_lng.toDouble()))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
            stateMyAddresses.response?.result?.work.let {
                item {
                    if(it==null) return@item
                    FastAddressCard("Работа", it[0].address.city, icon = R.drawable.ic_work) {
                        mainViewModel.clearToAddress()
                        mainViewModel.addToAddress(it[0].address)
                        if (mainViewModel.fromAddress.value.name != "") {
                            navController.navigate(Routes.CREATE_ORDER_SHEET)
                        } else {
                            Values.WhichAddress.value = Constants.ADD_FROM_ADDRESS_FOR_NAVIGATE
                            bottomNavigator.show(
                                SearchAddresses({
                                    navController.navigate(Routes.CREATE_ORDER_SHEET)
                                }) {
                                    navController.navigate(Routes.MAP_POINT_SHEET)
                                }
                            )
                        }
                        //map.controller.animateTo(GeoPoint(it.address_lat.toDouble(), it.address_lng.toDouble()))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
            stateMyAddresses.response?.result?.other.let {
                if(it==null) return@let
                it.forEach {
                    item {
                        if(it==null) return@item
                        FastAddressCard(getAddressText(it.address), it.address.city, icon = R.drawable.ic_favorites) {
                            mainViewModel.clearToAddress()
                            mainViewModel.addToAddress(it.address)
                            if (mainViewModel.fromAddress.value.name != "") {
                                navController.navigate(Routes.CREATE_ORDER_SHEET)
                            } else {
                                Values.WhichAddress.value = Constants.ADD_FROM_ADDRESS_FOR_NAVIGATE
                                bottomNavigator.show(
                                    SearchAddresses({
                                        navController.navigate(Routes.CREATE_ORDER_SHEET)
                                    }) {
                                        navController.navigate(Routes.MAP_POINT_SHEET)
                                    }
                                )
                            }
                            //map.controller.animateTo(GeoPoint(it.address_lat.toDouble(), it.address_lng.toDouble()))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }
            stateFastAddresses.response?.forEach {
                item {
                    FastAddressCard(getAddressText(it), it.city) {
                        mainViewModel.clearToAddress()
                        mainViewModel.addToAddress(it)
                        if (mainViewModel.fromAddress.value.name != "") {
                            navController.navigate(Routes.CREATE_ORDER_SHEET)
                        } else {
                            Values.WhichAddress.value = Constants.ADD_FROM_ADDRESS_FOR_NAVIGATE
                            bottomNavigator.show(
                                SearchAddresses({
                                    navController.navigate(Routes.CREATE_ORDER_SHEET)
                                }) {
                                    navController.navigate(Routes.MAP_POINT_SHEET)
                                }
                            )
                        }
                        //map.controller.animateTo(GeoPoint(it.address_lat.toDouble(), it.address_lng.toDouble()))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
}