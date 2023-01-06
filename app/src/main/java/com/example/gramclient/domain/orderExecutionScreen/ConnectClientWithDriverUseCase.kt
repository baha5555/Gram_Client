package com.example.gramclient.domain.orderExecutionScreen

import com.example.gramclient.Resource
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.mainScreen.order.connectClientWithDriver.connectClientWithDriverResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ConnectClientWithDriverUseCase @Inject constructor(private val repository: AppRepository) {
    operator fun invoke(
        token: String,
        order_id:String
    ): Flow<Resource<connectClientWithDriverResponse>> =
        flow {
            try {
                emit(Resource.Loading())
                val response: connectClientWithDriverResponse =
                    repository.connectClientWithDriver(token,order_id)
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