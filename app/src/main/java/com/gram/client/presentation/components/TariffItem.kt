@file:Suppress("UNUSED_EXPRESSION")

package com.gram.client.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gram.client.presentation.screens.main.states.CalculateResponseState
import com.gram.client.presentation.screens.main.states.TariffsResponseState
import com.valentinilk.shimmer.shimmer

@Composable
fun TariffItem(
    icon: Int,
    price: Int,
    name: String,
    isSelected: Boolean,
    onSelected: () -> Unit = {},
    stateCalculate: CalculateResponseState
) {

    Column(
        modifier = Modifier
            .width(85.dp)
            .height(95.dp)
            .border(border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onSelected()
            }
            .background(if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.secondary)
            .padding(10.dp)


    ) {

        Image(
            modifier = Modifier
                .offset(0.dp, if (name == "Эконом") (-10).dp else 0.dp)
                .weight(1f),
            painter = painterResource(icon),
            contentDescription = "icon"
        )
        Text(text = name, fontSize = 13.sp, color = if (isSelected) Color.White else Color.Black, maxLines = 1)
        if(stateCalculate.isLoading){
            AnimatedVisibility(visible = true) {
                Box(modifier = Modifier.shimmer()){
                    Box(modifier = Modifier
                        .size(40.dp, 15.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.Gray))
                }
            }
        }
        else{
            AnimatedVisibility(visible = true) {
                Box(modifier = Modifier.height(15.dp)){
                    Text(text = "$price c", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Color.Black)
                }
            }
        }

    }
}