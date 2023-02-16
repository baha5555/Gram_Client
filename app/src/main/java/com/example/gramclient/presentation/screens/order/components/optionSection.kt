package com.example.gramclient.presentation.screens.order.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun optionSection(modalBottomSheetState: ModalBottomSheetState){

    val switch= remember{ mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.background)
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
                    Text(text = "Наличные", maxLines = 1, overflow = TextOverflow.Ellipsis, color= Color.Black, fontSize = 18.sp)
            }
            Image(
                modifier = Modifier.size(18.dp),
                imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                contentDescription = "icon"
            )
        }
            Divider(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 55.dp, end = 15.dp))
        Row(
            modifier = Modifier
                .clickable {
                    scope.launch {
                        modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                    }
                }
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = Icons.Default.Info,
                    contentDescription = "Logo"
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Детали заказа", maxLines = 1, overflow = TextOverflow.Ellipsis, color= Color.Black, fontSize = 18.sp)
            }
            Image(
                modifier = Modifier.size(18.dp),
                imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                contentDescription = "icon"
            )
        }
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 55.dp, end = 15.dp))
/*        Row(
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
        }*/
    }
}