package com.example.gramclient.domain

import com.example.gramclient.data.firebase.AllClientLiveData
import com.example.gramclient.data.firebase.AllOrdersLiveData

interface FirebaseRepository {
    suspend fun getClientOrder(client: String, goToSearchAddressScreen: () -> Unit): AllClientLiveData
    suspend fun getOrders(): AllOrdersLiveData
}