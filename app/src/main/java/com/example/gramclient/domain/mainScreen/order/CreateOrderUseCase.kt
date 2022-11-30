package com.example.gramclient.domain.mainScreen.order

import com.example.gramclient.Resource
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.mainScreen.TariffsResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException


class CreateOrderUseCase(private val repository: AppRepository) {

    operator fun invoke(
        token: String,
        dop_phone: String?,
        from_address: Int?,
        to_addresses: List<AddressModel>?,
        comment: String?,
        tariff_id: Int,
        allowances: String?
    ): Flow<Resource<OrderResponse>> =
        flow{
            try {
                emit(Resource.Loading<OrderResponse>())
                var jsonToAddress: String? = null
                if(to_addresses != null) {
                    jsonToAddress = Gson().toJson(to_addresses)
                }
                val response: OrderResponse = repository.createOrder(token, dop_phone, from_address, jsonToAddress, comment, tariff_id, allowances)
                emit(Resource.Success<OrderResponse>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<OrderResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<OrderResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<OrderResponse>("${e.message}"))
            }
        }
}