package com.example.gramclient.presentation

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.gramclient.R
import com.example.gramclient.domain.realtimeDatabase.Order.RealtimeDatabaseOrder
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.presentation.screens.order.OrderExecutionViewModel
import com.example.gramclient.presentation.screens.profile.ProfileViewModel
import com.example.gramclient.utils.Constants.isDialogState
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DriverInSiteScreen(
    selectRealtimeDatabaseOrder:RealtimeDatabaseOrder,
    isDialog:MutableState<Boolean>
) {
    val context = LocalContext.current
    if(isDialog.value) {
        Box(modifier = Modifier
            .fillMaxSize()
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
                    text = "Приехал ${selectRealtimeDatabaseOrder.performer?.first_name} на ${selectRealtimeDatabaseOrder.performer?.transport?.model?:"Model"}\n цвет ${selectRealtimeDatabaseOrder.performer?.transport?.color?:"Color"},госномер ${selectRealtimeDatabaseOrder.performer?.transport?.car_number}\n Платное ожидание начнется в 20:10 \n Пожалуйста выходите",
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