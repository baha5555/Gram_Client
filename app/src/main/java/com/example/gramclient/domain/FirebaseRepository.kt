package com.example.gramclient.domain

import com.example.gramclient.data.firebase.GetClientOrderLiveData
import com.example.gramclient.data.firebase.GetOrdersLiveData

interface FirebaseRepository {
    suspend fun getClientOrder(client: String, goToSearchAddressScreen: () -> Unit): GetClientOrderLiveData
    suspend fun getOrders(): GetOrdersLiveData
}