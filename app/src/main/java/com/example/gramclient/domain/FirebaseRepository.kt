package com.example.gramclient.domain

import com.example.gramclient.data.firebase.GetClientOrderLiveData
import com.example.gramclient.data.firebase.GetOrdersLiveData

interface FirebaseRepository {
    suspend fun getClientOrder(client: String): GetClientOrderLiveData
    suspend fun getOrders(): GetOrdersLiveData
}