package com.gram.client.presentation.screens.order.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.domain.orderExecutionScreen.active.AllActiveOrdersResult
import com.gram.client.presentation.components.voyager.AddStopScreenOrderExcecution
import com.gram.client.presentation.components.voyager.SearchAddressOrderExecutionNavigator
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.utils.Constants
import com.gram.client.utils.Routes
import com.gram.client.utils.Values
import com.gram.client.utils.getAddressText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun orderSection(
    order: AllActiveOrdersResult,
    scope: CoroutineScope,
    navController: NavHostController,
    orderExecutionViewModel: OrderExecutionViewModel,
) {
    val navigator = LocalNavigator.currentOrThrow
    val bottomNavigator = LocalBottomSheetNavigator.current
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier
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
                    if(it == null) text = "Откуда?"
                    else text = getAddressText(it)

                    if(order.meeting_info!=null){
                        text+=", Место встречи ${order.meeting_info}"
                    }
                }
                Text(
                    text =  text /*, maxLines = 1*/,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 55.dp, end = 15.dp)
        ) {
            Divider()
        }
        if((order.to_addresses?.size ?: 0) < 4){
            Row(
                modifier = Modifier
                    .clickable {
                        Values.WhichAddress.value=Constants.ADD_TO_ADDRESS_ACTIVE
                        scope.launch {
                            bottomNavigator.show(SearchAddressOrderExecutionNavigator(orderExecutionViewModel){
                               // navigator.push(OrderExecutionMapPointScreen())
                                navController.navigate(Routes.MAP_POINT_SHEET)
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
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 55.dp, end = 15.dp)
        ) {
            Divider()
        }
        order.to_addresses?.let { address ->
            if(address.isEmpty()) return@let
            Row(
                modifier = Modifier
                    .clickable {
                        scope.launch {
                            if (order.to_addresses.size > 1) {
                                Values.WhichAddress.value=Constants.ADD_TO_ADDRESS_ACTIVE
                                bottomNavigator.show(AddStopScreenOrderExcecution())
                                Toast.makeText(context,"Можно изменить порядок остановки", Toast.LENGTH_SHORT).show()

                            } else {
                                Values.WhichAddress.value=Constants.TO_ADDRESS_ACTIVE
                                bottomNavigator.show(
                                    SearchAddressOrderExecutionNavigator(orderExecutionViewModel = orderExecutionViewModel,inx = 0){
                                        navController.navigate(Routes.MAP_POINT_SHEET)
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
                                getAddressText(address[0]),
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