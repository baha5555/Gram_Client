package com.example.gramclient.presentation.screens

import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import com.example.gramclient.R
import com.example.gramclient.app.preference.CustomPreference
import com.example.gramclient.presentation.components.currentRoute
import com.example.gramclient.utils.RoutesName
import com.example.gramclient.presentation.screens.order.OrderExecutionViewModel
import com.example.gramclient.ui.theme.BackgroundColor

@Composable
fun SplashScreen(
    navController: NavController,
    orderExecutionViewModel: OrderExecutionViewModel,
){
    val activeOrders = orderExecutionViewModel.stateActiveOrders.value
    val prefs = CustomPreference(LocalContext.current)
    LaunchedEffect(key1 = true) {
        if(prefs.getAccessToken() == "") navController.navigate(RoutesName.SEARCH_ADDRESS_SCREEN)
        else orderExecutionViewModel.getActiveOrders(navController)
    }
    if(activeOrders.success){
        currentRoute = navController.currentBackStackEntry?.destination?.route
        if(navController.currentBackStackEntry?.destination?.route == RoutesName.SPLASH_SCREEN) {
            Log.i("asdasda", ""+activeOrders.response)
            if (activeOrders.response!!.isEmpty()) {
                navController.navigate(RoutesName.SEARCH_ADDRESS_SCREEN) {
                    popUpTo(RoutesName.SPLASH_SCREEN) {
                        inclusive = true
                    }
                }
            } else {
                navController.navigate(RoutesName.SEARCH_DRIVER_SCREEN) {
                    popUpTo(RoutesName.SPLASH_SCREEN) {
                        inclusive = true
                    }
                }
            }
        }
    }
    Splash()

}

@Composable
fun Splash(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.logo_gram_black),
                ""
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