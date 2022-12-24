package com.example.gramclient

import com.example.gramclient.data.remote.ApplicationApi
import com.example.gramclient.domain.AppRepository

lateinit var REPOSITORY: AppRepository

object Constants {
    const val LOCAL_BASE_URL="http://10.250.1.114:8080/"
    const val BASE_URL="http://testapi.client.gram.tj/"
    const val PREFIX="992"
    const val FROM_ADDRESS="FROM_ADDRESS"
    const val TO_ADDRESS="TO_ADDRESS"
}