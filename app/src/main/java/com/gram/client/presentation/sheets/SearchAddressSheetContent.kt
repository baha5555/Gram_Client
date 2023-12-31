package com.gram.client.presentation.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gram.client.presentation.screens.main.MainViewModel
import com.gram.client.presentation.screens.main.components.FastAddresses
import com.gram.client.presentation.screens.main.components.StoriesList
import com.gram.client.presentation.screens.main.components.ToAddressField

@Composable
fun SearchAddressSheetContent(
    focusRequester: FocusRequester,
    isSearchState: MutableState<Boolean>,
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val toAddress = mainViewModel.toAddresses
    val searchText = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .fillMaxHeight(fraction = 0.80f)
            .background(Color(0xffEEEEEE)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(197.dp)
                .padding(2.dp)
                .background(
                    color = Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(size = 30.dp)
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(9.dp)
                    .background(
                        color = Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(size = 30.dp)
                    )
            ) {
                if (!isSearchState.value) {
                    if (searchText.value != "") searchText.value = ""
                    ToAddressField(
                        toAddress = toAddress,
                        navController = navController,
                        mainViewModel = mainViewModel
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    FastAddresses(mainViewModel, navController)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(size = 30.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .background(
                        color = Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(size = 30.dp)
                    )
            ) {
                Text(
                    text = "Добро пожаловать в Gram",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF343434),
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                StoriesList()
            }


        }
    }
}