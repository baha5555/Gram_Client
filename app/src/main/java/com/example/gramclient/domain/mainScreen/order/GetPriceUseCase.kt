package com.example.gramclient.domain.mainScreen.order

import com.example.gramclient.Resource
import com.example.gramclient.domain.AppRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetPriceUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(
        tariff_id: Int,
        allowances: String?,
        from_address: Int?,
        to_addresses: List<AddressModel>?
    ): Flow<Resource<CalculateResponse>> =
        flow{
            try {
                emit(Resource.Loading<CalculateResponse>())
                var jsonToAddress: String? = null
                if(to_addresses != null) {
                    jsonToAddress = Gson().toJson(to_addresses)
                }
                val response: CalculateResponse = repository.getPrice(tariff_id, allowances, from_address, jsonToAddress)
                emit(Resource.Success<CalculateResponse>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<CalculateResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<CalculateResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<CalculateResponse>("${e.message}"))
            }
        }
}