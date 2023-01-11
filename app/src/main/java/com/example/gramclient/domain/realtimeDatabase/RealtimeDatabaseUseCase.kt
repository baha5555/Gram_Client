package com.example.gramclient.domain.realtimeDatabase

import androidx.lifecycle.LiveData
import com.example.gramclient.utils.Resource
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.realtimeDatabase.Order.RealtimeDatabaseOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RealtimeDatabaseUseCase @Inject constructor(private val repository: AppRepository) {
    operator fun invoke(): Flow<Resource<LiveData<List<RealtimeDatabaseOrder>>>> =
        flow {
            try {
                emit(Resource.Loading<LiveData<List<RealtimeDatabaseOrder>>>())
                val response: LiveData<List<RealtimeDatabaseOrder>> =
                    repository.readAllOrders
                emit(Resource.Success<LiveData<List<RealtimeDatabaseOrder>>>(response))
            } catch (e: HttpException) {
                emit(
                    Resource.Error<LiveData<List<RealtimeDatabaseOrder>>>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<LiveData<List<RealtimeDatabaseOrder>>>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<LiveData<List<RealtimeDatabaseOrder>>>("${e.message}"))
            }
        }
}