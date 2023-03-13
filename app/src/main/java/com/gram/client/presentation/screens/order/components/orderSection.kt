package com.gram.client.presentation.screens.order.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.domain.firebase.order.RealtimeDatabaseOrder
import com.gram.client.presentation.components.voyager.AddStopScreenOrderExcecution
import com.gram.client.presentation.components.voyager.OrderExecutionMapPointScreen
import com.gram.client.presentation.components.voyager.SearchAddressOrderExecutionNavigator
import com.gram.client.utils.Constants
import com.gram.client.utils.Values
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun orderSection(
    order: RealtimeDatabaseOrder,
    scope: CoroutineScope,
) {
    val navigator = LocalNavigator.currentOrThrow
    val bottomNavigator = LocalBottomSheetNavigator.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    if(order.status=="Поступило" || order.status=="Не оформлен"){
                        bottomNavigator.show(SearchAddressOrderExecutionNavigator(){
                            navigator.push(OrderExecutionMapPointScreen())
                            Values.WhichAddress.value=Constants.FROM_ADDRESS
                        })
                    }
                }
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            ) {
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.from_marker),
                    contentDescription = "Logo"
                )
                var text by remember{ mutableStateOf("")}
                Spacer(modifier = Modifier.width(20.dp))
                order.from_address.let {
                    text = it?.address ?: "Откуда?"
                    if(order.meeting_info!=null){
                        text+=", подъезд ${order.meeting_info}"
                    }
                }
                Text(
                    text =  text /*, maxLines = 1*/,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Image(
                modifier = Modifier
                    .size(18.dp)
                    .fillMaxWidth(0.1f),
                imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                contentDescription = "icon"
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 55.dp, end = 15.dp)
        ) {
            Divider()
        }
        Row(
            modifier = Modifier
                .clickable {
                    scope.launch {
                        bottomNavigator.show(SearchAddressOrderExecutionNavigator(){
                            navigator.push(OrderExecutionMapPointScreen())
                            Values.WhichAddress.value=Constants.ADD_TO_ADDRESS
                        })
                    }
                }
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.plus_icon),
                    contentDescription = "Logo"
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Добавить остановку", maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Image(
                modifier = Modifier.size(18.dp),
                imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                contentDescription = "icon"
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 55.dp, end = 15.dp)
        ) {
            Divider()
        }
        order.to_address?.let { address ->
            Row(
                modifier = Modifier
                    .clickable {
                        scope.launch {
                            if (order.to_address.size > 1) {
                                bottomNavigator.show(AddStopScreenOrderExcecution())
                            } else {
                                bottomNavigator.show(
                                    SearchAddressOrderExecutionNavigator(inx = 0){
                                        navigator.push(OrderExecutionMapPointScreen())
                                        Values.WhichAddress.value=Constants.TO_ADDRESS
                                    }
                                )
                            }
                        }
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
                        imageVector = if (MaterialTheme.colors.isLight) ImageVector.vectorResource(R.drawable.to_marker) else ImageVector.vectorResource(
                            R.drawable.to_marker_dark
                        ),
                        contentDescription = "Logo"
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    when (address.size) {
                        1 -> {
                            Text(
                                address[0].address,
                                maxLines = 1, overflow = TextOverflow.Ellipsis
                            )
                        }
                        else -> {
                            Text(
                                "" + address.size + " - остановки",
                                maxLines = 1, overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Image(
                    modifier = Modifier.size(18.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                    contentDescription = "icon"
                )
            }
        }
    }
}