package com.example.gramclient.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramclient.R
import com.example.gramclient.ui.theme.PrimaryColor

@Composable
fun CustomOrderExecutionField(
    mainText:String,
    secondaryText:String,
    icon:Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp,start=15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(28.dp),
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = "Logo"
        )
        Spacer(modifier = Modifier.width(15.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(end=15.dp,),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = mainText)
                    Text(
                        text = secondaryText,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Icon(
                    modifier = Modifier
                        .size(14.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                    contentDescription = "Logo",
                    tint = PrimaryColor
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Color(0xFFEEEEEE))
        }

    }
}