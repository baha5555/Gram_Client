package com.gram.client.domain.orderExecutionScreen

import com.gram.client.utils.Resource
import com.gram.client.domain.AppRepository
import com.gram.client.domain.mainScreen.order.connectClientWithDriver.connectClientWithDriverResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ConnectClientWithDriverUseCase @Inject constructor(private val repository: AppRepository) {
    operator fun invoke(
        order_id: String
    ): Flow<Resource<connectClientWithDriverResponse>> =
        flow {
            try {
                emit(Resource.Loading())
                val response: connectClientWithDriverResponse =
                    repository.connectClientWithDriver(order_id)
                emit(Resource.Success<connectClientWithDriverResponse>(response))
            } catch (e: HttpException) {
                emit(
                    Resource.Error<connectClientWithDriverResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<connectClientWithDriverResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<connectClientWithDriverResponse>("${e.message}"))
            }
        }
}