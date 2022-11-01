package com.example.gramclient.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.presentation.components.CustomDialog

@Composable
fun SplashScreen(navController: NavController){
    var startAnimate by remember {
        mutableStateOf(false)
    }
    val alphaAnimation= animateFloatAsState(
        targetValue = if(startAnimate) 1f else 0f,
        animationSpec = tween(durationMillis = 3000)
    )
    LaunchedEffect(key1 = true){
        startAnimate=true
        delay(4000)
        navController.navigate(RoutesName.AUTH_SCREEN){
            popUpTo(RoutesName.SPLASH_SCREEN) {
                inclusive = true
            }
        }

    }
    Splash(alpha = alphaAnimation.value)

}

@Composable
fun Splash(alpha:Float){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.BottomCenter
    ){
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 440.dp)){
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, 
                horizontalArrangement = Arrangement.Center) {
                Image(
                    modifier= Modifier
                        .width(176.dp)
                        .height(50.07.dp),
                    alpha = alpha,
                    bitmap = ImageBitmap.imageResource(R.drawable.logo),
                    contentDescription = "Logo"
                )
                Image(
                    modifier= Modifier
                        .width(89.dp)
                        .height(87.dp),
                    alpha = alpha,
                    bitmap = ImageBitmap.imageResource(R.drawable.sphere),
                    contentDescription = "shere"
                )
            }
            Text(text = "БЫСТРО, ДЕШЕВО, БЕЗОПАСНО", modifier = Modifier
                .fillMaxWidth()
                .padding(end = 40.dp), fontSize = 15.sp, textAlign = TextAlign.Center,
            )
        }
        Image(
            modifier= Modifier
                .fillMaxWidth()
                .height(240.dp),
            alpha = alpha,
            bitmap = ImageBitmap.imageResource(R.drawable.city),
            contentDescription = "city"
        )
    }
}