package com.example.gramclient.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.gramclient.domain.mainScreen.Address

object Values {
    val FirstName = mutableStateOf("")
    val LastName = mutableStateOf("")
    val Email = mutableStateOf("")
    val PhoneNumber = mutableStateOf("")
    val ImageUrl: MutableState<String?> = mutableStateOf("")
    val ClientOrdersSize = mutableStateOf(0)
    //Map
    val FromAddress = mutableStateOf(Address("", 0, "", ""))
    val ToAddress = mutableStateOf(listOf<Address>(Address("", 0, "", "")))
}