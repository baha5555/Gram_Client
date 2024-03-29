package com.gram.client.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CustomRequestError(onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .shadow(3.dp, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Не удалось отправить запрос.\n" +
                        "Проверьте соединение с интернетом.",
                fontSize = 16.sp,
                modifier = Modifier.weight(0.7f))
            Text(
                text = "Повторить", modifier = Modifier.clickable { onClick.invoke() },
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
            )
        }

}