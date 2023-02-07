package com.example.gramclient.presentation.screens.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.ui.theme.BackgroundColor

@Composable
fun CustomDopInfoForDriver(
    onClick:()->Unit,
    textFieldValue:String,
    onValueChange:(String)->Unit,
    placeholder:String,
    title:String,
    stateTextField:Boolean = false
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.9f)) {
        Box(modifier = Modifier.fillMaxWidth().background(BackgroundColor).padding(vertical = 20.dp), contentAlignment = Alignment.Center) {
            Text(
                text = title,
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 10.dp)
            )
        }
        TextField(
            modifier=Modifier.fillMaxWidth(0.95f).padding(start = 10.dp, top = 10.dp),
            value = textFieldValue, onValueChange = {onValueChange(it)},
            placeholder = { Text(text = placeholder) },
            singleLine = stateTextField,
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                cursorColor = Color.Black,
                leadingIconColor = Color.Black,
                trailingIconColor = Color.Black,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = if(stateTextField) Color.Black else Color.Transparent,
                unfocusedIndicatorColor = if(stateTextField) Color.Black else Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = if(stateTextField) KeyboardType.Number else KeyboardType.Text),
            )
        if(textFieldValue!="") {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 10.dp),
                    text = "Сохранить", textSize = 16, onClick = onClick,
                    textBold = false
                )
            }
        }
    }
}