package com.example.gramclient.domain.orderExecutionScreen

import com.example.gramclient.Resource
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.mainScreen.order.AddressModel
import com.example.gramclient.domain.mainScreen.order.OrderResponse
import com.example.gramclient.domain.mainScreen.order.UpdateOrderResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class EditOrderUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(
        token: String,
        order_id: Int,
        dop_phone: String?,
        from_address: Int?,
        meeting_info: String?,
        to_addresses: List<AddressModel>?,
        comment: String?,
        tariff_id : Int,
        allowances: String?,
    ): Flow<Resource<UpdateOrderResponse>> =
        flow{
            try {
                emit(Resource.Loading<UpdateOrderResponse>())
                var jsonToAddress: String? = null
                if(to_addresses != null) {
                    jsonToAddress = Gson().toJson(to_addresses)
                }
                val response: UpdateOrderResponse = repository.editOrder(token, order_id, dop_phone, from_address, meeting_info, jsonToAddress, comment, tariff_id, allowances)
                emit(Resource.Success<UpdateOrderResponse>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<UpdateOrderResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<UpdateOrderResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<UpdateOrderResponse>("${e.message}"))
            }
        }
}