package com.example.gramclient.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.presentation.components.CustomTopBar
import com.example.gramclient.ui.theme.BackgroundColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SettingScreen(navController: NavHostController) {
    Scaffold(topBar = { CustomTopBar("Параметры", navController) }) {
        Column(
            Modifier
                .fillMaxSize()
                .background(BackgroundColor)
        ) {
            Column(Modifier.clickable {
                navController.navigate(RoutesName.SETTING_LANGUAGE_SCREEN)
            }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
                        Text(text = "Регион", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = "Худжанд", fontSize = 18.sp)
                    }
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.btn_back_icon_2),
                        contentDescription = ""
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            color = Color.Gray
                        ),
                )
            }
            Column(Modifier.clickable { navController.navigate(RoutesName.SETTING_REGION_SCREEN) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
                        Text(text = "Язык приложения", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = "Русский", fontSize = 18.sp)
                    }
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.btn_back_icon_2),
                        contentDescription = ""
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            color = Color.Gray
                        )
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 30.dp, top = 30.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                Text(text = "Геолокация", fontSize = 22.sp)
                Switch2()
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 30.dp, top = 30.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                Text(text = "Не звонить", fontSize = 22.sp)
                Switch2()
            }

        }
    }
}

@Composable
fun Switch2(
    scale: Float = 1.7f,
    width: Dp = 26.dp,
    height: Dp = 15.dp,
    strokeWidth: Dp = 2.dp,
    checkedTrackColor: Color = Color(0xFF2264D1),
    uncheckedTrackColor: Color = Color(0xFF707070),
    gapBetweenThumbAndTrackEdge: Dp = 3.dp
) {

    val switchON = remember {
        mutableStateOf(false) // Initially the switch is ON
    }

    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge
    val animatePosition = animateFloatAsState(
        targetValue = if (switchON.value)
            with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
        else
            with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
    )

    Canvas(
        modifier = Modifier
            .size(width = width, height = height)
            .scale(scale = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        // This is called when the user taps on the canvas
                        switchON.value = !switchON.value
                    }
                )
            }
    ) {
        // Track
        drawRoundRect(
            color = if (switchON.value) checkedTrackColor else uncheckedTrackColor,
            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )

        // Thumb
        drawCircle(
            color = if (switchON.value) checkedTrackColor else uncheckedTrackColor,
            radius = thumbRadius.toPx(),
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )
    }
}

