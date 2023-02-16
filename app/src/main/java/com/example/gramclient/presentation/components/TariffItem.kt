@file:Suppress("UNUSED_EXPRESSION")

package com.example.gramclient.presentation.components

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
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.gramclient.ui.theme.PrimaryColor

@Composable
fun TariffItem(
    icon: Int,
    price: Int,
    name: String,
    isSelected: Boolean,
    onSelected: () -> Unit = {}
) {

    ConstraintLayout(
        modifier = Modifier
            .width(85.dp)
            .height(95.dp)
            .border(border= BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onSelected()
            }
            .background(if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.background)
            .padding(10.dp)


    ) {
        val (icn, prc, txt) = createRefs()


        Image(
            modifier = Modifier.offset(0.dp, if(name=="Эконом")(-10).dp else 0.dp)
                .constrainAs(icn) {
                    start.linkTo(parent.start)
                },
            painter = painterResource(icon),
            contentDescription = "icon"
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = name, fontSize = 13.sp,
            modifier = Modifier.constrainAs(txt) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(prc.top)
            }, color = if (isSelected) Color.White else Color.Black)
        Text(text = "$price c", fontSize = 13.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(prc) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }.padding(end = 20.dp), color = if (isSelected) Color.White else Color.Black)
    }
}