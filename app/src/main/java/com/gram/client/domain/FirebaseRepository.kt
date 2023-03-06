package com.gram.client.domain

import com.gram.client.data.firebase.GetClientOrderLiveData
import com.gram.client.data.firebase.GetOrdersLiveData

interface FirebaseRepository {
    suspend fun getClientOrder(client: String): GetClientOrderLiveData
    suspend fun getOrders(): GetOrdersLiveData
}