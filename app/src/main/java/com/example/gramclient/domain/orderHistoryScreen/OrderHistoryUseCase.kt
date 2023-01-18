package com.example.gramclient.domain.orderHistoryScreen

import com.example.gramclient.utils.Resource
import com.example.gramclient.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class OrderHistoryUseCase @Inject constructor(
    private val repository: AppRepository
) {

    operator fun invoke(): Flow<Resource<orderHistoryResponse>> =
        flow{
            try {
                emit(Resource.Loading<orderHistoryResponse>())
                val response: orderHistoryResponse = repository.getOrderHistory()
                emit(Resource.Success<orderHistoryResponse>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<orderHistoryResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<orderHistoryResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<orderHistoryResponse>("${e.message}"))
            }
        }
}