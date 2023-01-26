package com.example.gramclient.presentation.screens.order.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomSwitch


@Composable
fun optionSection(){

    val switch= remember{ mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(bottom = 10.dp)
    ){
        Row(
            modifier = Modifier
                .clickable {
//                    scope.launch {
//                        bottomSheetState.bottomSheetState.expand()
//                        isSearchState.value = true
//                        WHICH_ADDRESS.value = Constants.FROM_ADDRESS
//                    }
                }
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.wallet_icon),
                    contentDescription = "Logo"
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column(){
                    Text(text = "Наличные", maxLines = 1, overflow = TextOverflow.Ellipsis, color= Color.Black, fontSize = 18.sp)
                    Text(text = "Изменить способ оплаты", maxLines = 1, overflow = TextOverflow.Ellipsis, color= Color.Gray, fontSize = 14.sp)
                }
            }
            Image(
                modifier = Modifier.size(18.dp),
                imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                contentDescription = "icon"
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 55.dp, end = 15.dp)) {
            Divider()
        }
        Row(
            modifier = Modifier
                .clickable {
//                        scope.launch {
//                            bottomSheetState.bottomSheetState.expand()
//                            isSearchState.value = true
//                            WHICH_ADDRESS.value = Constants.TO_ADDRESS
//                        }
                }
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row() {
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.location_icon),
                    contentDescription = "Logo"
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Показать водителю, где я", color = Color.Gray,
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
            Row(){
                CustomSwitch(switchON = switch) {

                }
                Spacer(modifier = Modifier.width(15.dp))
            }
        }
    }
}