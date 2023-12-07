package com.gram.client.presentation.screens.main.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gram.client.domain.mainScreen.Address
import com.gram.client.presentation.screens.main.*


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AddressSearchBottomSheet(
    heightFraction: Float = 0.98f,
    isSearchState: MutableState<Boolean>,
    mainViewModel: MainViewModel,
    focusRequester: FocusRequester,
    toAddress: List<Address>
) {
    val searchText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .fillMaxHeight(fraction = heightFraction)
            .padding(15.dp).background(Color(0xFF000000))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)

                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 30.dp))
        ) {
            if (!isSearchState.value) {
                if (searchText.value != "") searchText.value = ""
                ToAddressField(
                    toAddress = toAddress
                )
                Spacer(modifier = Modifier.height(15.dp))
                FastAddresses(mainViewModel)
                Spacer(modifier = Modifier.height(15.dp))

            }
        }
        Column(
            modifier = Modifier
                .width(393.dp)
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 30.dp))
        ) {
            Text(
                text = "Добро пожаловать в Gram",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF343434),
                )
            )
            StoriesList()

        }
//            Spacer(modifier = Modifier.height(15.dp))
//            Services()
    }
}


