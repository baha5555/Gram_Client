package com.example.gramclient.presentation.mainScreen.addressComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavController
import com.example.gramclient.presentation.MainActivity
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddressList(
    navController: NavController,
    isVisible: MutableState<Boolean>,
    address: MutableState<String>,
    focusManager: FocusManager
) {
    val addresses = getListOfCountries()
    var filteredAddresses: List<String>
    val activity = (LocalContext.current as? MainActivity)
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = Modifier.fillMaxWidth()) {
        val searchedText = address
        filteredAddresses = if (searchedText.value.isEmpty()) {
            addresses
        }else {
            val resultList = mutableListOf<String>()
            for (address in addresses) {
                if (address.lowercase(Locale.getDefault())
                        .contains(searchedText.value.lowercase(Locale.getDefault()))
                ) {
                    resultList.add(address)
                }
            }
            resultList
        }
        filteredAddresses.forEach() { filteredCountry ->
            if(isVisible.value) {
                AddressListItem(
                    addressText = filteredCountry.toString(),
                    onItemClick = { selectedCountry ->
                        address.value=selectedCountry
                        keyboardController?.hide()
                        isVisible.value=!isVisible.value
                        focusManager.clearFocus()
                    }
                )
            }
        }
    }
}

fun getListOfCountries(): List<String> {
    val addresses = listOf("Кори ниёзи 1", "Камоли Хучанди 56", "Ленина 12", "Содирхони Хофиз 87", "Хакимбоев 45", "Рахимбоев 14", "Танбури 11")
    return  addresses
}