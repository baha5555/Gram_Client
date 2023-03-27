package com.gram.client.presentation.screens.promocod

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import javax.annotation.meta.When

class SendPromoCodeScreen() : Screen {
    @Composable
    override fun Content() {
        val bottomNavigator = LocalBottomSheetNavigator.current
        val promocodeViewModel: PromocodViewModel = hiltViewModel()
        val promocode = promocodeViewModel.stateSendPromocod.value.response
        val context = LocalContext.current
        val sendPromocod = remember {
            mutableStateOf("")
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Text(
                "Промокод",
                fontSize = 20.sp,
                modifier = Modifier.align(CenterHorizontally).padding(20.dp),
                fontWeight = FontWeight.Medium
            )
            Divider(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF97ADB6)))
            TextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                value = sendPromocod.value,
                onValueChange = { sendPromocod.value = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = "Напишите сообщение",
                        fontSize = 20.sp,
                        color = Color.Gray,
                    )
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    promocodeViewModel.sendPromoCode(sendPromocod.value)
                    bottomNavigator.hide()
                    if (sendPromocod.value.length < 7 || sendPromocod.value.length > 7) {
                        Toast.makeText(
                            context,
                            "Промокод состоит из 7 символов",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Toast.makeText(
                        context,
                        promocode?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.e(
                        "${promocode?.message}",
                        "${promocode?.message}"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth().padding(8.dp)
                    .height(57.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = ButtonDefaults.elevation(0.dp)
            ) {
                Text(text = "Добавить промокод", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}