package com.gram.client.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.osmdroid.util.GeoPoint

object Values {
    val FirstName = mutableStateOf("")
    val LastName = mutableStateOf("")
    val Email = mutableStateOf("")
    val PhoneNumber = mutableStateOf("")
    val ImageUrl: MutableState<String?> = mutableStateOf("")

    val SMS_CODE: MutableState<String?> = mutableStateOf("")
    val CLIENT_REGISTER_ID: MutableState<String?> = mutableStateOf("")
    val PHONE_NUMBER: MutableState<String?> = mutableStateOf("")


    val ClientOrdersSize = mutableStateOf(0)
    //Map
    val DriverLocation = mutableStateOf(GeoPoint(0.0, 0.0))
    //Coment
    val CommentCancelReasons = mutableStateOf("")
    val CommentRatingReasons = mutableStateOf("")
    val CommentDriver = mutableStateOf("")
    val CommentToAnotherHuman = mutableStateOf("992")

    val WhichAddress = mutableStateOf("")

    val AdjustResize = mutableStateOf(false)
    val ActiveOrdersInx: MutableState<Int> = mutableStateOf(-1)

}