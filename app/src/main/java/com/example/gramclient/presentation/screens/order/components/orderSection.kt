package com.example.gramclient.presentation.screens.order.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.example.gramclient.R
import com.example.gramclient.domain.firebase.order.RealtimeDatabaseOrder
import com.example.gramclient.presentation.components.voyager.AddStopScreenOrderExcecution
import com.example.gramclient.presentation.components.voyager.SearchAddressOrderExecutionNavigator
import com.example.gramclient.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun orderSection(
    order: RealtimeDatabaseOrder,
    scope: CoroutineScope,
    bottomSheetState: BottomSheetScaffoldState,
    isSearchState: MutableState<Boolean>
) {
    val bottomNavigator = LocalBottomSheetNavigator.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable {
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
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = order.from_address?.address ?: "Откуда?"/*, maxLines = 1*/,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
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
                        bottomNavigator.show(SearchAddressOrderExecutionNavigator(Constants.ADD_TO_ADDRESS))
                    }
                }
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Image(
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
                            if(order.to_address.size>1){
                                bottomNavigator.show(AddStopScreenOrderExcecution())
                            }else{
                                bottomNavigator.show(SearchAddressOrderExecutionNavigator(Constants.TO_ADDRESS, inx = 0))
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
                        imageVector = ImageVector.vectorResource(R.drawable.to_marker),
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