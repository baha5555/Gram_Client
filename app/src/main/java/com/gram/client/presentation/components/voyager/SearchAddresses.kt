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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gram.client.R
import com.gram.client.presentation.screens.drawer.myaddresses_screen.MyAddressViewModel
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.utils.Constants
import com.gram.client.utils.Values
import com.gram.client.utils.getAddressText
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
        val fromAddressTxt = getAddressText(mainViewModel.fromAddress.value)
        val fromText = remember {
            mutableStateOf(
                TextFieldValue(
                    fromAddressTxt,
                    selection = TextRange(fromAddressTxt.length)
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
                val toAddressTxt = getAddressText(mainViewModel.toAddresses[0])
                toText.value = TextFieldValue(
                    toAddressTxt,
                    TextRange(toAddressTxt.length)
                )
            }
            when (Values.WhichAddress.value) {
                Constants.FROM_ADDRESS, Constants.ADD_FROM_ADDRESS_FOR_NAVIGATE -> {
                    focusRequester.requestFocus()
                }

                Constants.TO_ADDRESS -> {
                    if (toCreate != null) {
                        if (mainViewModel.fromAddress.value.name == "") {
                            focusRequester.requestFocus()
                        } else {
                            focusRequesterTo.requestFocus()
                        }
                    } else {
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
                    if (fromIsFocused.value && fromText.value == TextFieldValue("") || toIsFocused.value && toText.value == TextFieldValue(
                            ""
                        )
                    ) {
                        showMyAddresses(
                            fromIsFocused,
                            toIsFocused,
                            fromText,
                            toText,
                            focusRequesterTo
                        )
                    }
                    searchState.response?.forEach {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (fromIsFocused.value) {
                                        mainViewModel.updateFromAddress(
                                            it
                                        )
                                        fromText.value = TextFieldValue(getAddressText(it))
                                        if (Values.WhichAddress.value == Constants.ADD_FROM_ADDRESS_FOR_NAVIGATE) {
                                            bottomNavigator.hide()
                                            if (toCreate != null) toCreate.invoke()
                                            return@clickable
                                        }
                                        focusRequesterTo.requestFocus()
                                    } else if (toIsFocused.value) {
                                        mainViewModel.clearToAddress()
                                        mainViewModel.addToAddress(it)
                                        toText.value = TextFieldValue(getAddressText(it))
                                        keyboard?.hide()
                                        if (mainViewModel.fromAddress.value.name != "") {
                                            if (toCreate != null) {
                                                toCreate.invoke()
                                            }
                                            bottomNavigator.hide()
                                        }
                                    }
                                }
                                .padding(start = 45.dp)) {
                            Column( modifier = Modifier.padding(vertical = 10.dp)) {
                                Text(
                                    text = if(it.type == "address") "${it.street}, ${it.name}" else it.name,
                                    fontSize = 18.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = if(it.type == "address") "${it.city}, ${it.region}" else "${it.street}",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF989898),

                                        ),
                                )
                            }
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

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun showMyAddresses(
        fromIsFocused: MutableState<Boolean>,
        toIsFocused: MutableState<Boolean>,
        fromText: MutableState<TextFieldValue>,
        toText: MutableState<TextFieldValue>,
        focusRequesterTo: FocusRequester
    ) {
        val mainViewModel: MainViewModel = hiltViewModel()
        val myAddressViewModel: MyAddressViewModel = hiltViewModel()
        val stateMyAddresses = myAddressViewModel.stateGetAllMyAddresses.value
        val bottomNavigator = LocalBottomSheetNavigator.current
        val keyboard = LocalSoftwareKeyboardController.current

        stateMyAddresses.response?.result?.home.let {
            if (it == null) return@let
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (fromIsFocused.value) {
                            mainViewModel.updateFromAddress(
                                it[0].address
                            )
                            fromText.value = TextFieldValue(
                                "Дом: " + it[0].address.name,
                                selection = TextRange("Дом: ${it[0].address.name}".length)
                            )
                            if (Values.WhichAddress.value == Constants.ADD_FROM_ADDRESS_FOR_NAVIGATE) {
                                bottomNavigator.hide()
                                if (toCreate != null) toCreate.invoke()
                                return@clickable
                            }
                            focusRequesterTo.requestFocus()
                        } else if (toIsFocused.value) {
                            mainViewModel.clearToAddress()
                            mainViewModel.addToAddress(it[0].address)
                            toText.value =
                                TextFieldValue("Дом: " + it[0].address.name)
                            keyboard?.hide()
                            if (mainViewModel.fromAddress.value.name != "") {
                                if (toCreate != null) {
                                    toCreate.invoke()
                                }
                                bottomNavigator.hide()
                            }
                        }
                    }
                    .padding(start = 7.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 15.dp)
                )
                Column() {
                    Text(
                        text = "Дом",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 10.dp),
                        maxLines = 1
                    )
                    Divider()
                }
            }
        }
        stateMyAddresses.response?.result?.work.let {
            if (it == null) return@let
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (fromIsFocused.value) {
                            mainViewModel.updateFromAddress(it[0].address)
                            fromText.value = TextFieldValue(
                                getAddressText(it[0].address),
                                selection = TextRange(getAddressText(it[0].address).length)
                            )
                            if (Values.WhichAddress.value == Constants.ADD_FROM_ADDRESS_FOR_NAVIGATE) {
                                bottomNavigator.hide()
                                if (toCreate != null) toCreate.invoke()
                                return@clickable
                            }
                            focusRequesterTo.requestFocus()
                        } else if (toIsFocused.value) {
                            mainViewModel.clearToAddress()
                            mainViewModel.addToAddress(it[0].address)
                            toText.value = TextFieldValue(
                                it[0].address.name,
                                selection = TextRange(it[0].address.name.length)
                            )
                            keyboard?.hide()
                            if (mainViewModel.fromAddress.value.name != "") {
                                if (toCreate != null) {
                                    toCreate.invoke()
                                }
                                bottomNavigator.hide()
                            }
                        }
                    }
                    .padding(start = 7.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_work),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 15.dp)
                )
                Column() {
                    Text(
                        text = "Работа",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 10.dp),
                        maxLines = 1
                    )
                    Divider()
                }
            }
        }
        stateMyAddresses.response?.result?.other?.forEach {
            if (it == null) return@forEach

            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (fromIsFocused.value) {
                            mainViewModel.updateFromAddress(it.address)
                            fromText.value = TextFieldValue(
                                it.address.name,
                                selection = TextRange(it.address.name.length)
                            )
                            if (Values.WhichAddress.value == Constants.ADD_FROM_ADDRESS_FOR_NAVIGATE) {
                                bottomNavigator.hide()
                                if (toCreate != null) toCreate.invoke()
                                return@clickable
                            }
                            focusRequesterTo.requestFocus()
                        } else if (toIsFocused.value) {
                            mainViewModel.clearToAddress()
                            mainViewModel.addToAddress(it.address)
                            toText.value = TextFieldValue(
                                it.address.name,
                                selection = TextRange(it.address.name.length)
                            )
                            keyboard?.hide()
                            if (mainViewModel.fromAddress.value.name != "") {
                                if (toCreate != null) {
                                    toCreate.invoke()
                                }
                                bottomNavigator.hide()
                            }
                        }
                    }
                    .padding(start = 7.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_favorites),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 15.dp)
                )
                Column() {
                    Text(
                        text = it.name,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 10.dp),
                        maxLines = 1
                    )
                    Divider()
                }
            }
        }
    }
}