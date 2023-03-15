package com.gram.client.presentation.screens.promocod

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R

class ShareScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val promocodViewModel: PromocodViewModel = hiltViewModel()
        LaunchedEffect(key1 = true)
        {
            promocodViewModel.getPromocod()
        }
        val promocod = promocodViewModel.statepromocod.value.response
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

        Log.e(promocod.toString(), "${promocod?.promo_code}")
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier.fillMaxHeight(0.1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TopAppBar(
                        content = {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                IconButton(modifier = Modifier, onClick = { navigator.pop() }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_blue),
                                        contentDescription = ""
                                    )
                                }
                                Text(
                                    text = "Дари друзьям",
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.Center),
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                        elevation = 0.dp
                    )
                }
            }, backgroundColor = Color.White

        ) {
            Box (modifier = Modifier.fillMaxSize()){
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Делитесь промокодом и получай по 14 за каждого друга, который установит приложение и сделает три заказа: совершит поездки или оформит доставку. Друг получит 70 на первые заказы.\n" +
                                "\n" +
                                "Оплачивайте премиальными, а не реальными!\n", fontSize = 18.sp
                    )
                }

                Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.BottomCenter) {
                    Button(modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(bottom = 15.dp),onClick = {
                        val text =
                            "Воспользуйтесь моим щедрым подарком и получите 70 премиальных смн на поездки в такси.Просто скачай приложение «Gram» и введи промокод\"${promocod?.promo_code}\""
                        shareText(text = text, launcher = launcher)
                    }, shape = RoundedCornerShape(30)) {
                        Text("Поделиться: ${promocod?.promo_code}")
                    }
                }
            }

        }
    }
}