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
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddressSearchBottomSheet(
    heightFraction: Float = 0.98f,
    isSearchState: MutableState<Boolean>,
    mainViewModel: MainViewModel,
    bottomSheetState: BottomSheetScaffoldState,
    focusRequester: FocusRequester,
    coroutineScope: CoroutineScope,
    WHICH_ADDRESS: MutableState<String>,
    toAddress: List<Address>
) {
    val searchText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .fillMaxHeight(fraction = heightFraction)
            .background(Color.White)
            .padding(15.dp)
    ) {

        if (!isSearchState.value) {
            if(searchText.value !="") searchText.value = ""
            ToAddressField(
                WHICH_ADDRESS = WHICH_ADDRESS,
                toAddress = toAddress,
                isSearchState = isSearchState,
                bottomSheetState = bottomSheetState,
                scope = coroutineScope
            )
            Spacer(modifier = Modifier.height(15.dp))
            FastAddresses(mainViewModel)
//            Spacer(modifier = Modifier.height(15.dp))
//            Services()
        }
    }
}
