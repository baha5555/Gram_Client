package com.gram.client.presentation.components.voyager

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gram.client.presentation.screens.main.addressComponents.AddressListItem
import com.gram.client.presentation.screens.main.addressComponents.Loader
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.utils.Constants
import com.gram.client.utils.Values
import kotlinx.coroutines.launch

class SearchAddressOrderExecutionNavigator(val orderExecutionViewModel: OrderExecutionViewModel,val inx: Int = -1,  val function: () -> Unit) : Screen {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val searchText = remember {
            mutableStateOf("")
        }
        val address = remember {
            mutableStateOf("")
        }
        val scope = rememberCoroutineScope()
        val focusRequester = remember { FocusRequester() }
        val bottomNavigator = LocalBottomSheetNavigator.current

        LaunchedEffect(true) {
            orderExecutionViewModel.searchAddress("")
            //focusRequester.requestFocus()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .background(
                    MaterialTheme.colors.background,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(top = 15.dp, start = 15.dp, end = 15.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = searchText.value,
                    onValueChange = { value ->
                        scope.launch {
                            searchText.value = value
                            orderExecutionViewModel.searchAddress(value)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
                    leadingIcon = {
                        IconButton(onClick = { /*navController.popBackStack()*/ }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                                    .size(24.dp)
                            )
                        }
                    },
                    trailingIcon = {
                        if (searchText.value != "") {
                            IconButton(
                                modifier = Modifier.offset(x = (-40).dp),
                                onClick = {
                                    scope.launch { searchText.value = "" }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(vertical = 15.dp, horizontal = 40.dp)
                                        .size(24.dp)
                                )
                            }
                        }
                    },
                    placeholder = { Text(text = "Введите адрес для поиска") },
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 20.dp)
                        .clickable {
                            if (bottomNavigator
                                    .pop()
                                    .not()
                            ) {
                                bottomNavigator.hide()
                                function.invoke()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(
                            modifier =
                            Modifier
                                .width(1.dp)
                                .height(55.dp)
                                .padding(vertical = 10.dp)
                                .background(Color(0xFFE0DBDB))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Карта",
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            )
            {
                val keyboardController = LocalSoftwareKeyboardController.current
                val stateSearchAddress by orderExecutionViewModel.stateSearchAddress
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(10.dp))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Loader(isLoading = stateSearchAddress.isLoading)
                    }
                    stateSearchAddress.response?.let { items ->
                        if (items.isNotEmpty()) {
                            items.forEach {
                                AddressListItem(
                                    it = it,
                                    onItemClick = { selectedAddress ->
                                        address.value = selectedAddress
                                        Log.e(
                                            "EditOrderResponse",
                                            ""+it.id
                                        )
                                        if (inx == -2) bottomNavigator.replaceAll(AddStopScreen {
                                            function.invoke()
                                        })
                                        else bottomNavigator.hide()
                                        if (keyboardController != null) {
                                            keyboardController.hide()
                                        }
                                        when (Values.WhichAddress.value) {
                                            Constants.TO_ADDRESS_ACTIVE -> {
                                                if(inx>=0) orderExecutionViewModel.updateToAddressInx(it, inx)
                                            }
                                            Constants.ADD_TO_ADDRESS_ACTIVE -> {
                                                orderExecutionViewModel.addToAddress(it)
                                            }
                                            Constants.FROM_ADDRESS->{
                                                orderExecutionViewModel.updateFromAddress(it)
                                                orderExecutionViewModel.editOrder {

                                                }
                                            }
                                        }
                                        orderExecutionViewModel.getActiveOrders {  }
                                    }
                                )
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}