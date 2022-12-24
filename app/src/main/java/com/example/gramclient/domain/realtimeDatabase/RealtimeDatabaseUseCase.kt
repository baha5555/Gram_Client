package com.example.gramclient.domain.realtimeDatabase

import androidx.lifecycle.LiveData
import com.example.firebaserealtimedatabase.orders.Order
import com.example.gramclient.Resource
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.orderExecutionScreen.ActiveOrdersResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RealtimeDatabaseUseCase @Inject constructor(private val repository: AppRepository) {
    operator fun invoke(): Flow<Resource<LiveData<List<Order>>>> =
        flow {
            try {
                emit(Resource.Loading<LiveData<List<Order>>>())
                val response: LiveData<List<Order>> =
                    repository.readAll
                emit(Resource.Success<LiveData<List<Order>>>(response))
            } catch (e: HttpException) {
                emit(
                    Resource.Error<LiveData<List<Order>>>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<LiveData<List<Order>>>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<LiveData<List<Order>>>("${e.message}"))
            }
        }
}