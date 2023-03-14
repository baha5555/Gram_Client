package com.gram.client.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object Constants {
    val TAG = "TAG"
    var FCM_TOKEN:String? = ""
    const val LOCAL_BASE_URL="http://10.250.1.96/"
    const val TEST_URL="http://testapi.client.gram.tj/"
    const val BASE_URL="https://api-client.gram.tj"
    var IDENTIFY_TO_SCREEN = ""
    const val PREFIX="992"
    const val FROM_ADDRESS="FROM_ADDRESS"
    const val TO_ADDRESS="TO_ADDRESS"
    const val ADD_TO_ADDRESS="ADD_TO_ADDRESS"
    const val SOON = "(Скоро)"
    var stateOfDopInfoForDriver = mutableStateOf("")
    var STATE_RATING: MutableState<Boolean> = mutableStateOf(false)
    var STATE_RAITING_ORDER_ID: MutableState<Int> = mutableStateOf(-1)
    var STATE_ASSIGNED_ORDER:MutableState<Boolean> = mutableStateOf(false)
    var STATE_DRIVER_IN_SITE:MutableState<Boolean> = mutableStateOf(false)
    var STATE_DRIVER_IN_SITE_ORDER_ID:MutableState<Int> = mutableStateOf(-1)

    var STATE_ASSIGNED_ORDER_ID:MutableState<Int> = mutableStateOf(-1)
    var KONFIG_URL = "https://gram.tj/legal"
}

object PreferencesName {
    const val ACCESS_TOKEN="ACCESS_TOKEN"
    const val  APP_PREFERENCES="APP_PREFERENCES"
    const val  PHONE_NUMBER="PHONE_NUMBER"
    const val  CLIENT_REGISTER_ID="CLIENT_REGISTER_ID"
    const val FCM_TOKEN="FCM_TOKEN"
}

object Comments {
    const val RATING = "RATING"
    const val CANCEL = "CANCEL"
    const val DRIVER = "DRIVER"
    const val TO_ANOTHER_HUMAN = "TO_ANOTHER_HUMAN"
}