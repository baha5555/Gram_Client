package com.example.gramclient.presentation

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.gramclient.R
import com.example.gramclient.domain.firebase.order.RealtimeDatabaseOrder
import com.example.gramclient.presentation.components.CustomButton
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DriverInSiteScreen(
    selectRealtimeDatabaseOrder:RealtimeDatabaseOrder,
    isDialog:MutableState<Boolean>
) {
    val context = LocalContext.current
    if(isDialog.value) {
        val mp3:MediaPlayer  = MediaPlayer.create(context,R.raw.uvedoplegne)
        mp3.start()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 5)
        val formattedTime = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 15.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.ava
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.height(45.dp))
                Log.e("DRIVER","INSITE")
                Text(
                    text = "Приехал ${selectRealtimeDatabaseOrder.performer?.first_name} на ${selectRealtimeDatabaseOrder.performer?.transport?.model?:"Model"}\n цвет ${selectRealtimeDatabaseOrder.performer?.transport?.color?:"Color"}, госномер ${selectRealtimeDatabaseOrder.performer?.transport?.car_number}\n Платное ожидание начнется в $formattedTime \n Пожалуйста выходите",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 25.dp), contentAlignment = Alignment.BottomCenter) {
                CustomButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .height(61.dp),
                    text = "OK",
                    textSize = 18,
                    textBold = true,
                ) {
                    isDialog.value = false
                }
        }
    }
}