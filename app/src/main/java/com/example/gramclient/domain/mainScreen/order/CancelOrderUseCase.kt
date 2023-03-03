package com.example.gramclient.domain.mainScreen.order

import com.example.gramclient.utils.Resource
import com.example.gramclient.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CancelOrderUseCase @Inject constructor(private val repository: AppRepository) {
    operator fun invoke(order_id: Int, reason_cancel_order: String): Flow<Resource<CancelOrderResponse>> =
        flow{
            try {
                emit(Resource.Loading<CancelOrderResponse>())
                val response: CancelOrderResponse = repository.cancelOrder(order_id, reason_cancel_order)
                emit(Resource.Success<CancelOrderResponse>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<CancelOrderResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<CancelOrderResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<CancelOrderResponse>("${e.message}"))
            }
        }
}