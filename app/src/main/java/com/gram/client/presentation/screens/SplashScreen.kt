package com.gram.client.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.gram.client.R
import com.gram.client.app.preference.CustomPreference
import com.gram.client.presentation.screens.main.SearchAddressScreen
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.presentation.screens.order.SearchDriverScreen
import com.gram.client.presentation.screens.profile.ProfileViewModel
import kotlinx.coroutines.delay

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
        val profileViewModel: ProfileViewModel = hiltViewModel()

        val navigator = LocalNavigator.currentOrThrow
        val currentKey = navigator.lastItem.key
        val activeOrders = orderExecutionViewModel.stateActiveOrders.value

        val isAnimationEnd = remember{
            mutableStateOf(false)
        }

        val prefs = CustomPreference(LocalContext.current)
        LaunchedEffect(key1 = true) {
            delay(4600)
            if (prefs.getAccessToken() == "") {
                navigator.replace(SearchAddressScreen())
                //mainViewModel.getFastAddresses()
            }
            else {
                orderExecutionViewModel.getActiveOrders()
                profileViewModel.getProfileInfo()
                //mainViewModel.getFastAddresses()
            }
            isAnimationEnd.value = true

        }
        if (activeOrders.success) {
        if (currentKey == SplashScreen().key) {
            if (activeOrders.response!!.isEmpty()) { navigator.replace(SearchAddressScreen()) }
            else { navigator.replace(SearchDriverScreen()) }
        }
        }
        Splash(isAnimationEnd)
    }

    @Composable
    fun Splash(isAnimationEnd: MutableState<Boolean>) {
        Box(){
            if(!isAnimationEnd.value){
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
                //val progress by animateLottieCompositionAsState(composition)
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier.fillMaxHeight(),
                    contentScale = ContentScale.Crop
                    //progress = { progress },
                )
            }else{
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_end))
                //val progress by animateLottieCompositionAsState(composition)
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier.fillMaxHeight(),
                    contentScale = ContentScale.Crop
                    //progress = { progress },
                )
            }
        }
    }
}