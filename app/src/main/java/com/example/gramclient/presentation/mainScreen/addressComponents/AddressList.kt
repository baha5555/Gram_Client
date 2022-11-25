package com.example.gramclient.presentation.mainScreen.addressComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gramclient.presentation.MainActivity
import com.example.gramclient.presentation.components.Loader
import com.example.gramclient.presentation.mainScreen.states.SearchAddressResponseState
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddressList(
    navController: NavController,
    isVisible: MutableState<Boolean>,
    address: MutableState<String>,
    focusManager: FocusManager,
    stateSearchAddress: SearchAddressResponseState,
    isToListAddress: MutableState<Boolean>,
) {
    val activity = (LocalContext.current as? MainActivity)
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
       if (isToListAddress.value){
           Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
               Loader(isLoading = stateSearchAddress.isLoading)
           }
           stateSearchAddress.response?.let { items ->
               if(items.size != 0) {
                   items.forEach{
                       AddressListItem(
                           addressText = it.name,
                           onItemClick = { selectedAddress ->
                               address.value = selectedAddress
                               keyboardController?.hide()
                               isVisible.value = !isVisible.value
                               focusManager.clearFocus()
                           }
                       )
                   }
               }
           }
       }
    }
}