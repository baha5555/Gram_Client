package com.example.gramclient.domain.firebase

import androidx.lifecycle.LiveData
import com.example.gramclient.utils.Resource
import com.example.gramclient.domain.FirebaseRepository
import com.example.gramclient.domain.firebase.order.RealtimeDatabaseOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(private val repository: FirebaseRepository) {
    operator fun invoke(): Flow<Resource<LiveData<List<RealtimeDatabaseOrder>>>> =
        flow {
            try {
                val response: LiveData<List<RealtimeDatabaseOrder>> = repository.getOrders()
                emit(Resource.Success(response))
            } catch (_: Exception) {
            }
        }
}