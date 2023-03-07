package com.gram.client.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun CustomDialog(
    text: String,
    okBtnClick: () -> Unit,
    cancelBtnClick: () -> Unit,
    isDialogOpen:Boolean
){
    if(isDialogOpen){
        Dialog(onDismissRequest = { /*TODO*/ }) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.secondary)
                        .padding(10.dp)
                ) {
                    Text(
                        text = text,
                        fontSize = 18.sp,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colors.onBackground
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
                            onClick = { cancelBtnClick() }
                        ) {
                            Text(text = "Нет", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                            onClick = { okBtnClick() }
                        ) {
                            Text(text = "Да", fontSize = 18.sp)
                        }
                    }
                }
        }
    }
}
@Composable
fun CustomCancelDialog(
    text: String,
    okBtnClick: () -> Unit,
    isDialogOpen:Boolean,
){
    if(isDialogOpen){
        Dialog(onDismissRequest = { /*TODO*/ }) {

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background)
                    .padding(10.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Button(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                        onClick = { okBtnClick() }
                    ) {
                        Text(text = "OK", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}