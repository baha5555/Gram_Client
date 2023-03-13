package com.gram.client.presentation.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
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

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
        val profileViewModel: ProfileViewModel = hiltViewModel()

        val navigator = LocalNavigator.currentOrThrow
        val currentKey = navigator.lastItem.key
        val activeOrders = orderExecutionViewModel.stateActiveOrders.value

        val prefs = CustomPreference(LocalContext.current)
        LaunchedEffect(key1 = true) {
            if (prefs.getAccessToken() == "") {
                navigator.replace(SearchAddressScreen())
                //mainViewModel.getFastAddresses()
            }
            else {
                orderExecutionViewModel.getActiveOrders()
                profileViewModel.getProfileInfo()
                //mainViewModel.getFastAddresses()
            }
        }
        if (activeOrders.success) {
        if (currentKey == SplashScreen().key) {
            if (activeOrders.response!!.isEmpty()) { navigator.replace(SearchAddressScreen()) }
            else { navigator.replace(SearchDriverScreen()) }
        }
        }
        Splash()
    }

    @Composable
    fun Splash() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.secondary),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.logo_gram_black),
                    "",
                    tint = MaterialTheme.colors.onSecondary
                )

                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.day_and_night))
                //val progress by animateLottieCompositionAsState(composition)
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier.fillMaxSize(0.4f),
                    alignment = Alignment.BottomCenter
                    //progress = { progress },
                )

            }

            Text(
                text = "БЫСТРО, ДЕШЕВО, БЕЗОПАСНО",
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 30.dp)
            )
            var visible by remember { mutableStateOf(false) }

            val animationTime = 15000
            val animationDelayTime = 5

            val arrowStartLocation = Offset(0F, 100F)
            val arrowEndLocation = Offset(0F, 0F)

            LaunchedEffect(Unit) {
                visible = true
            }
            val arrowLocation by animateOffsetAsState(
                targetValue = if (visible) arrowEndLocation else arrowStartLocation,
                animationSpec = tween(animationTime, animationDelayTime, easing = LinearOutSlowInEasing)
            )
            Image(
                painter = BitmapPainter(ImageBitmap.imageResource(R.drawable.city)),
                contentDescription = "Зимний лес",
                modifier = Modifier
                    .fillMaxSize()
                    .offset(arrowLocation.x.dp, arrowLocation.y.dp),
                alignment = Alignment.BottomCenter
            )
        }
    }
}