package com.gram.client.domain.orderExecutionScreen

import com.gram.client.utils.Resource
import com.gram.client.domain.AppRepository
import com.gram.client.domain.orderExecutionScreen.active.ActiveOrdersResponse
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
                emit(Resource.Loading())
                val response: ActiveOrdersResponse =
                    repository.getActiveOrders()
                emit(Resource.Success(response))
            } catch (e: HttpException) {
                emit(
                    Resource.Error(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error("${e.message}"))
            }
        }
}