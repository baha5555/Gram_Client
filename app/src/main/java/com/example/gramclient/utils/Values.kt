package com.example.gramclient.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.gramclient.domain.firebase.profile.Client
import com.example.gramclient.domain.mainScreen.Address
import org.osmdroid.util.GeoPoint

object Values {
    val FirstName = mutableStateOf("")
    val LastName = mutableStateOf("")
    val Email = mutableStateOf("")
    val PhoneNumber = mutableStateOf("")
    val ImageUrl: MutableState<String?> = mutableStateOf("")
    val ClientOrdersSize = mutableStateOf(0)
    //Map
    val DriverLocation = mutableStateOf(GeoPoint(0.0, 0.0))
    val FromAddress2 = mutableStateOf(Address("", 0, "", ""))
    val ToAddress2 = mutableStateOf(listOf<Address>(Address("", 0, "", "")))
    val BtnBack = mutableStateOf(false)
    //Orders
    val ClientOrders: MutableState<Client?> = mutableStateOf(null)
}