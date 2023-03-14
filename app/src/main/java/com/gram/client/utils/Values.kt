package com.gram.client.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.gram.client.domain.firebase.profile.Client
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
    //Orders
    val ClientOrders: MutableState<Client?> = mutableStateOf(null)
    //Coment
    val CommentCancelReasons = mutableStateOf("")
    val CommentRatingReasons = mutableStateOf("")
    val CommentDriver = mutableStateOf("")

    val WhichAddress = mutableStateOf("")

}