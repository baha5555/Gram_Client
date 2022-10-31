package com.example.gramclient.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomDialog(
    text: String,
    okBtnClick: () -> Unit,
    cancelBtnClick: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0x33000000))
            .padding(horizontal = 15.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp)
        ) {
            Text(
                text = text,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .width(166.dp)
                        .height(39.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE2EAF2), contentColor = Color.Black),
                    onClick = { cancelBtnClick() }
                ) {
                    Text(text = "Нет", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                CustomButton(
                    text = "Да",
                    textSize = 18,
                    textBold = false,
                    width = 166,
                    height = 39,
                    radius = 10
                ) {
                    okBtnClick()
                }
            }
        }
    }
}