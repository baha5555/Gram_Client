package com.gram.client.presentation.screens.main.addressComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gram.client.domain.mainScreen.Address
import com.gram.client.presentation.MainActivity
import com.gram.client.presentation.screens.main.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddressList(
    isVisible: MutableState<Boolean>,
    address: MutableState<String>,
    focusManager: FocusManager,
    mainViewModel: MainViewModel = hiltViewModel(),
    onclick: (address: Address) -> Unit
) {
    val activity = (LocalContext.current as? MainActivity)
    val keyboardController = LocalSoftwareKeyboardController.current
    val stateSearchAddress by mainViewModel.stateSearchAddress
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
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
                            keyboardController?.hide()
                            isVisible.value = !isVisible.value
                            focusManager.clearFocus()
                            onclick(it)
                        }
                    )
                    Divider()
                }

            }
        }
    }
}

@Composable
fun Loader(isLoading: Boolean) {
    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color(0xFF1E88E5))
    }
}