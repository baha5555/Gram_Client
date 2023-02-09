package com.example.gramclient.presentation.screens.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.presentation.screens.main.MainScreen
import com.example.gramclient.presentation.screens.main.MainViewModel
import com.example.gramclient.presentation.screens.map.map
import com.valentinilk.shimmer.shimmer
import org.osmdroid.util.GeoPoint

@Composable
fun FastAddresses(mainViewModel: MainViewModel) {
    LaunchedEffect(key1 = true) {
        mainViewModel.getFastAddresses()
    }
    val navigator = LocalNavigator.currentOrThrow
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
            stateFastAddresses.response?.forEach {
                val toAddress = Address(it.address, it.id, it.address_lat, it.address_lng, it.city)
                item {
                    FastAddressCard(title = "" + it.address){
                        mainViewModel.updateToAddress(toAddress)
                        navigator.push(MainScreen())
                        map.controller.animateTo(GeoPoint(it.address_lat.toDouble(), it.address_lng.toDouble()))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
}