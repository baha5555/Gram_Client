package com.example.gramclient.presentation.mainScreen

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gramclient.PreferencesName
import com.example.gramclient.presentation.mainScreen.addressComponents.AddressList
import com.example.gramclient.ui.theme.PrimaryColor

@Composable
fun SearchAddressScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel= hiltViewModel(),
    preferences: SharedPreferences,
    string: String?,
) {
    val searchText=remember{ mutableStateOf("") }
    val isAddressList= remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    val stateSearchAddress by mainViewModel.stateSearchAddress

    val focusRequester = remember { FocusRequester() }


    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE2EAF2))
            .padding(top = 20.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)){
                SearchView(state = searchText, preferences=preferences, navController = navController, focusRequester = focusRequester )
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            )
            {
                AddressList(
                    navController = navController,
                    isVisible = isAddressList,
                    address = searchText,
                    focusManager = focusManager,
                    stateSearchAddress = stateSearchAddress,
                ){address ->
                    if(string=="toAddress"){
                        mainViewModel.updateToAddress(0, address)
                        navController.popBackStack()
                        mainViewModel.getPrice(preferences)
                    }else if(string=="fromAddress"){
                        mainViewModel.updateFromAddress(address)
                        navController.popBackStack()
                        mainViewModel.getPrice(preferences)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchView(
    state: MutableState<String>,
    mainViewModel: MainViewModel= hiltViewModel(),
    preferences: SharedPreferences,
    navController: NavHostController,
    focusRequester: FocusRequester
) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
            mainViewModel.searchAddress(preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString(), value)
        },
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
        leadingIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(15.dp)
                        .size(24.dp)
                )
            }
        },
        trailingIcon = {
            if (state.value != "") {
                IconButton(
                    onClick = {
                        state.value =
                            ""
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        placeholder= {Text(text = "Введите адрес для поиска")},
        singleLine = true,
        shape = RoundedCornerShape(50.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            cursorColor = Color.Black,
            leadingIconColor = Color.Black,
            trailingIconColor = Color.Black,
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun saveButton(){
    FloatingActionButton(
        onClick = { /*TODO*/ },
        backgroundColor = PrimaryColor,
        contentColor = Color.White,
        modifier = Modifier
            .size(65.dp)
            .offset(y = (-40).dp, x = (-10).dp)
    ) {
        Icon(
            Icons.Filled.Check,
            contentDescription = "",
            modifier = Modifier
                .padding(15.dp)
                .size(24.dp)
        )
    }
}