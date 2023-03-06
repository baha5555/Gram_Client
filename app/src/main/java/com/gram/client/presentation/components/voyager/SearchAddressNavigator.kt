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
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.main.addressComponents.AddressListItem
import com.gram.client.presentation.screens.main.addressComponents.Loader
import com.gram.client.presentation.screens.navigator
import com.gram.client.utils.Constants
import kotlinx.coroutines.launch

class SearchAddressNavigator(val whichScreen: String, val inx: Int = -1) : Screen {
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
        val mainViewModel: MainViewModel = hiltViewModel()
        val focusRequester = remember { FocusRequester() }
        val bottomNavigator = LocalBottomSheetNavigator.current

        LaunchedEffect(true) {
            mainViewModel.searchAddress("")
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
                            mainViewModel.searchAddress(value)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(fontSize = 18.sp),
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
                        backgroundColor = MaterialTheme.colors.background,
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
                            Log.e("WHICHSCREEN" ,"$whichScreen")
                            when(whichScreen) {
                                Constants.FROM_ADDRESS -> {
                                    bottomNavigator.hide()
                                    navigator.push(MapPointScreen(whichScreen))
                                }
                                else -> {
                                    bottomNavigator.hide()
                                    //navigator.push(MapPointScreen(whichScreen))
                                }
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
                val stateSearchAddress by mainViewModel.stateSearchAddress
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
                                    addressText = it.address,
                                    onItemClick = { selectedAddress ->
                                        address.value = selectedAddress
                                        if (inx == -2) bottomNavigator.replaceAll(AddStopScreen())
                                        else bottomNavigator.hide()
                                        if (keyboardController != null) {
                                            keyboardController.hide()
                                        }
                                        when (whichScreen) {
                                            Constants.TO_ADDRESS -> {
                                                mainViewModel.updateToAddressInx(it, inx)
                                            }
                                            Constants.ADD_TO_ADDRESS -> {
                                                mainViewModel.addToAddress(it)
                                            }
                                            Constants.FROM_ADDRESS -> {
                                                mainViewModel.updateFromAddress(it)
                                            }
                                        }
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