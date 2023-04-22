package com.gram.client.presentation.components.voyager

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gram.client.R
import com.gram.client.domain.mainScreen.Address
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.utils.Constants
import com.gram.client.utils.Values
import com.valentinilk.shimmer.shimmer

class SearchAddresses(val toCreate: (() -> Unit)? = null, val function: () -> Unit) : Screen {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val mainViewModel: MainViewModel = hiltViewModel()
        val searchState = mainViewModel.stateSearchAddress.value

        val bottomNavigator = LocalBottomSheetNavigator.current

        val focusRequester = remember { FocusRequester() }
        val focusRequesterTo = remember { FocusRequester() }

        val keyboard = LocalSoftwareKeyboardController.current

        val fromText = remember {
            mutableStateOf(
                TextFieldValue(
                    "" + mainViewModel.fromAddress.value.address,
                    selection = TextRange(mainViewModel.fromAddress.value.address.length)
                )
            )
        }
        val toText = remember {
            mutableStateOf(
                TextFieldValue(
                    "",
                    TextRange(0)
                )
            )
        }
        val fromIsFocused = remember {
            mutableStateOf(true)
        }
        val toIsFocused = remember {
            mutableStateOf(true)
        }
        val focusedFrom = remember {
            mutableStateOf(true)
        }

        LaunchedEffect(key1 = true) {
            if (mainViewModel.toAddresses.isNotEmpty()) {
                toText.value = TextFieldValue(
                    mainViewModel.toAddresses[0].address,
                    TextRange(mainViewModel.toAddresses[0].address.length)
                )
            }
            when (Values.WhichAddress.value) {
                Constants.FROM_ADDRESS, Constants.ADD_FROM_ADDRESS_FOR_NAVIGATE -> {
                    focusRequester.requestFocus()
                }
                Constants.TO_ADDRESS -> {
                    if (toCreate!=null){
                        if(mainViewModel.fromAddress.value.address==""){
                            focusRequester.requestFocus()
                        }else{
                            focusRequesterTo.requestFocus()
                        }
                    }else{
                        focusRequesterTo.requestFocus()
                    }
                }
            }
            mainViewModel.searchAddress("")
        }
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxHeight(0.93f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(15.dp))
                    .clip(
                        RoundedCornerShape(15.dp)
                    )
                    .background(
                        Color.White
                    )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        imageVector = ImageVector.vectorResource(if (fromIsFocused.value) R.drawable.ic_serach_address_f else R.drawable.ic_serach_address_from),
                        contentDescription = "",
                        modifier = Modifier.padding(start = 15.dp)
                    )
                    TextField(
                        value = fromText.value,
                        onValueChange = {
                            fromText.value = it
                            mainViewModel.searchAddress(it.text)
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            disabledTextColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(fontSize = 18.sp),
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                fromIsFocused.value = it.isFocused
                            },
                        maxLines = 1
                    )
                    if (fromIsFocused.value) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (fromText.value.text != "") {
                                ClearText(fromText) {
                                    //mainViewModel.updateFromAddress(Address())
                                }
                            }
                            Divider(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(35.dp)
                            )
                            Text(text = "Карта",
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 15.dp)
                                    .clickable {
                                        bottomNavigator.hide()
                                        Values.WhichAddress.value = Constants.FROM_ADDRESS
                                        function.invoke()
                                    })
                        }
                    }
                }
                Divider(modifier = Modifier.padding(start = 45.dp, end = 15.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        imageVector = ImageVector.vectorResource(if (toIsFocused.value) R.drawable.ic_serach_address else R.drawable.ic_serach_address_to),
                        contentDescription = "",
                        modifier = Modifier.padding(start = 15.dp)
                    )
                    TextField(
                        value = toText.value,
                        onValueChange = {
                            toText.value = it
                            mainViewModel.searchAddress(it.text)
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            disabledTextColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(fontSize = 18.sp),
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequesterTo)
                            .onFocusChanged {
                                toIsFocused.value = it.isFocused
                            },
                        maxLines = 1
                    )
                    if (toIsFocused.value) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (toText.value.text != "") {
                                ClearText(toText) {
                                    mainViewModel.clearToAddress()
                                }
                            }
                            Divider(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(35.dp)
                            )
                            Text(text = "Карта",
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 15.dp)
                                    .clickable {
                                        bottomNavigator.hide()
                                        Values.WhichAddress.value = Constants.TO_ADDRESS
                                        function.invoke()
                                    })
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(Modifier.verticalScroll(rememberScrollState())) {
                if (searchState.isLoading) {
                    repeat(2) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shimmer(), verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .size(20.dp)
                                    .clip(RoundedCornerShape(100))
                                    .background(Color.Gray)
                            )
                            Column {
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                        .height(18.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(100))
                                        .background(Color(0xFFAAAAAA))
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                        .height(12.dp)
                                        .fillMaxWidth(0.8f)
                                        .clip(RoundedCornerShape(100))
                                        .background(Color(0xFFAAAAAA))
                                )
                            }
                        }
                        Divider(Modifier.padding(vertical = 10.dp))
                    }
                } else {
                    searchState.response?.forEach {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (fromIsFocused.value) {
                                        mainViewModel.updateFromAddress(
                                            Address(
                                                address = it.address,
                                                id = it.id,
                                                address_lat = it.address_lat,
                                                address_lng = it.address_lng
                                            )
                                        )
                                        fromText.value = TextFieldValue(it.address)
                                        if (Values.WhichAddress.value == Constants.ADD_FROM_ADDRESS_FOR_NAVIGATE){
                                            bottomNavigator.hide()
                                            if(toCreate!=null) toCreate.invoke()
                                            return@clickable
                                        }
                                        focusRequesterTo.requestFocus()
                                    } else if (toIsFocused.value) {
                                        mainViewModel.clearToAddress()
                                        mainViewModel.addToAddress(
                                            Address(
                                                address = it.address,
                                                id = it.id,
                                                address_lat = it.address_lat,
                                                address_lng = it.address_lng
                                            )
                                        )
                                        toText.value = TextFieldValue(it.address)
                                        keyboard?.hide()
                                        if (mainViewModel.fromAddress.value.address != "") {
                                            if (toCreate != null) {
                                                toCreate.invoke()
                                            }
                                            bottomNavigator.hide()
                                        }
                                    }
                                }
                                .padding(start = 45.dp)) {
                            Text(
                                text = "" + it.address,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 10.dp),
                                maxLines = 1
                            )
                            Divider()
                        }
                    }
                    Spacer(modifier = Modifier.height(300.dp))
                }
            }
        }
    }

    @Composable
    fun ClearText(text: MutableState<TextFieldValue>, function: () -> Unit) {
        IconButton(onClick = {
            text.value = TextFieldValue("")
            function.invoke()
        }) {
            Icon(
                Icons.Default.Close, contentDescription = ""
            )
        }
    }
}