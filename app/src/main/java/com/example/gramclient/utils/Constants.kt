package com.example.gramclient.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.gramclient.data.remote.ApplicationApi
import com.example.gramclient.domain.AppRepository

lateinit var REPOSITORY: AppRepository

object Constants {
    val TAG = "TAG"
    var FCM_TOKEN:String? = ""
    const val LOCAL_BASE_URL="http://10.250.1.114:8080/"
    const val BASE_URL="http://testapi.client.gram.tj/"
    const val PREFIX="992"
    const val FROM_ADDRESS="FROM_ADDRESS"
    const val TO_ADDRESS="TO_ADDRESS"
    var STATE_RAITING: MutableState<Boolean> = mutableStateOf(false)
}

object PreferencesName {
    const val ACCESS_TOKEN="ACCESS_TOKEN"
    const val  APP_PREFERENCES="APP_PREFERENCES"
    const val  PHONE_NUMBER="PHONE_NUMBER"
    const val  CLIENT_REGISTER_ID="CLIENT_REGISTER_ID"
    const val FCM_TOKEN="FCM_TOKEN"
}