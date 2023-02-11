package com.example.gramclient.data

import com.example.gramclient.data.firebase.GetClientOrderLiveData
import com.example.gramclient.data.firebase.GetOrdersLiveData
import com.example.gramclient.domain.FirebaseRepository

class AppFirebaseRepositoryImpl : FirebaseRepository {
    override suspend fun getClientOrder(client:String) = GetClientOrderLiveData(client)
    override suspend fun getOrders() = GetOrdersLiveData()
}