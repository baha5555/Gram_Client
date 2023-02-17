package com.example.gramclient.presentation.screens.main.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.presentation.screens.main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay


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
            .padding(15.dp)
    ) {

        if (!isSearchState.value) {
            if(searchText.value !="") searchText.value = ""
            ToAddressField(
                toAddress = toAddress
            )
            Spacer(modifier = Modifier.height(15.dp))
            FastAddresses(mainViewModel)
//            Spacer(modifier = Modifier.height(15.dp))
//            Services()
        }
    }
}
