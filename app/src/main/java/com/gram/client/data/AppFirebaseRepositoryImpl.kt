package com.gram.client.data

import com.gram.client.data.firebase.GetClientOrderLiveData
import com.gram.client.data.firebase.GetOrdersLiveData
import com.gram.client.domain.FirebaseRepository

class AppFirebaseRepositoryImpl : FirebaseRepository {
    override suspend fun getClientOrder(client:String) = GetClientOrderLiveData(client)
    override suspend fun getOrders() = GetOrdersLiveData()
}