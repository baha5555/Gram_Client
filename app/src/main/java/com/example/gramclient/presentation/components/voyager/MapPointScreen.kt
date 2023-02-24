package com.example.gramclient.presentation.components.voyager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
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
import com.example.gramclient.R
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.presentation.screens.main.MainViewModel
import com.example.gramclient.presentation.screens.main.components.FloatingButton
import com.example.gramclient.presentation.screens.map.CustomMainMap
import com.example.gramclient.presentation.screens.map.mLocationOverlay
import com.example.gramclient.presentation.screens.map.map
import com.example.gramclient.ui.theme.PrimaryColor
import com.example.gramclient.utils.Values
import com.valentinilk.shimmer.shimmer

class MapPointScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val mainViewModel: MainViewModel = hiltViewModel()
        val statePoint = mainViewModel.stateAddressPoint.value
        val navigator = LocalNavigator.currentOrThrow
        BottomSheetScaffold(
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetContent = {
                SheetContent{
                    statePoint.response.let {
                        if(it==null){
                            mainViewModel.updateFromAddress(Address("Мекта на карте", -1, map.mapCenter.latitude.toString(), map.mapCenter.longitude.toString()))
                        }else{
                            mainViewModel.updateFromAddress(Address(address = it.name, id=it.id, address_lat = it.lat, address_lng = it.lng))
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
                    }
                }
            }) {
            CustomMainMap(mainViewModel = mainViewModel)
        }
    }

    @Composable
    fun SheetContent(onClick: () -> Unit) {
        val mainViewModel: MainViewModel = hiltViewModel()
        val statePoint = mainViewModel.stateAddressPoint.value
        Column(
            modifier = Modifier
                .height(200.dp)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Text(
                    text = "Точка отправления",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium
                )
                Divider(modifier = Modifier.padding(vertical = 10.dp))
            }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            modifier = Modifier
                                .size(20.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.from_marker),
                            contentDescription = "Logo"
                        )
                        if(statePoint.isLoading){
                            Box(modifier = Modifier.shimmer().padding(start = 10.dp)){
                                Box(modifier = Modifier.size(150.dp,20.dp).background(Color.Gray))
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

}