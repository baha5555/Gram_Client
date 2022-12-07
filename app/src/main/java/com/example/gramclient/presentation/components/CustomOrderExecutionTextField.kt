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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.gramclient.R
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.PrimaryColor

@Composable
fun CustomOrderExecutionTextField(
    value:String,
    onValueChange:(String)->Unit,
    text:String,
    icon:Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth().padding(start=15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(15.dp),
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = "Logo"
        )
        Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(end = 15.dp,),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        placeholder = { Text(text) },
                        value = value,
                        onValueChange = {
                            onValueChange(it)
                        },
                        modifier = Modifier.fillMaxWidth(0.9f),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                    Icon(
                        modifier = Modifier
                            .size(14.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                        contentDescription = "Logo",
                        tint = PrimaryColor
                    )
                }
            }
            Divider(color = BackgroundColor)
        }
    }
}