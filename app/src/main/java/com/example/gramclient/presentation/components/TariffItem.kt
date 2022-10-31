package com.example.gramclient.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.gramclient.R
import kotlinx.coroutines.NonDisposableHandle.parent

@Composable
fun TariffItem(
    icon: Int,
    price: Int,
    name: String
) {

    ConstraintLayout(
        modifier = Modifier
            .size(90.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFE2EAF2))
            .padding(10.dp)

    ) {
        val (icn, prc, txt) = createRefs()


        Image(
            modifier = Modifier
                .width(70.dp)
                .height(50.dp)
                .constrainAs(icn) {
                    start.linkTo(parent.start)
                },
            bitmap = ImageBitmap.imageResource(icon),
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