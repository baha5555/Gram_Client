package com.example.gramclient.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramclient.R
import com.example.gramclient.ui.theme.BackgroundColor

@Composable
fun CustomOrderExecutionFieldOfWarning(
    mainText:String,
    secondaryText:String,
    icon:Int
) {
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp,start=15.dp,end=15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(23.dp),
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = "Logo",
                tint = Color(0xFF343B71)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                        tint = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        Divider(color = BackgroundColor)
    }
}