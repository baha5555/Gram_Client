package com.example.gramclient.domain.orderExecutionScreen

import com.example.gramclient.utils.Resource
import com.example.gramclient.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetActiveOrdersUseCase @Inject constructor(private val repository: AppRepository) {
    operator fun invoke(
    ): Flow<Resource<ActiveOrdersResponse>> =
        flow {
            try {
                emit(Resource.Loading<ActiveOrdersResponse>())
                val response: ActiveOrdersResponse =
                    repository.getActiveOrders()
                emit(Resource.Success<ActiveOrdersResponse>(response))
            } catch (e: HttpException) {
                emit(
                    Resource.Error<ActiveOrdersResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<ActiveOrdersResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<ActiveOrdersResponse>("${e.message}"))
            }
        }
}