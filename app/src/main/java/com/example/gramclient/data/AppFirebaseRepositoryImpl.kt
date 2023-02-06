package com.example.gramclient.data

import com.example.gramclient.data.firebase.AllClientLiveData
import com.example.gramclient.data.firebase.AllOrdersLiveData
import com.example.gramclient.domain.FirebaseRepository

class AppFirebaseRepositoryImpl : FirebaseRepository {
    override suspend fun getClientOrder(client:String, goToSearchAddressScreen:()->Unit) = AllClientLiveData(client,goToSearchAddressScreen)
    override suspend fun getOrders() = AllOrdersLiveData()
}