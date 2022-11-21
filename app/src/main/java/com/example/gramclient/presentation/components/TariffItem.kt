@file:Suppress("UNUSED_EXPRESSION")

package com.example.gramclient.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
            .size(90.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onSelected()
            }
            .background(if (isSelected) Color(0xFFE2EAF2) else Color(0xFFFFFFFF))
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
            })
        Text(text = "$price сомони", fontSize = 13.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(prc) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
    }
}