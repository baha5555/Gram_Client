package com.gram.client.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.main.SearchAddressScreen
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.presentation.screens.profile.ProfileViewModel
import com.gram.client.utils.Routes
import com.gram.client.utils.Values

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
        val profileViewModel: ProfileViewModel = hiltViewModel()
        val mainViewModel: MainViewModel = hiltViewModel()

        val navigator = LocalNavigator.currentOrThrow
        val currentKey = navigator.lastItem.key
        val activeOrders = orderExecutionViewModel.stateActiveOrders.value

        val isAnimationEnd = remember {
            mutableStateOf(false)
        }

        val prefs = CustomPreference(LocalContext.current)
        LaunchedEffect(key1 = true) {
            if (prefs.getAccessToken() == "") {
                navigator.replace(SearchAddressScreen())
                //mainViewModel.getFastAddresses()
            } else {
                orderExecutionViewModel.getActiveOrders()
                profileViewModel.getProfileInfo()
                if (mainViewModel.stateCountriesKey.value.response == null) {
                    mainViewModel.getCountriesKey("tj")
                }
                //mainViewModel.getFastAddresses()
            }
            isAnimationEnd.value = true

        }
        if (activeOrders.success) {
            if (currentKey == SplashScreen().key) {
                if (activeOrders.response!!.isEmpty()) {
                    navigator.replace(SearchAddressScreen())
                } else {
                    Values.firstRoute.value = Routes.SEARCH_DRIVER_SHEET
                    navigator.replace(SearchAddressScreen())
                }
            }
        }
        Splash(isAnimationEnd)
    }

    @Composable
    fun Splash(isAnimationEnd: MutableState<Boolean>) {
        Box() {
            if (!isAnimationEnd.value) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
                //val progress by animateLottieCompositionAsState(composition)
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier.fillMaxHeight(),
                    contentScale = ContentScale.Crop
                    //progress = { progress },
                )
            } else {
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