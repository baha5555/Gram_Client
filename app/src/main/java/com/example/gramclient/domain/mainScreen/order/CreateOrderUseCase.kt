package com.example.gramclient.domain.mainScreen.order

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.gramclient.utils.Resource
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.mainScreen.Address
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class CreateOrderUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(
        dop_phone: String?,
        from_address: Int?,
        to_addresses: SnapshotStateList<Address>?,
        comment: String?,
        tariff_id: Int,
        allowances: String?,
        date_time:String?
    ): Flow<Resource<OrderResponse>> =
        flow{
            try {
                emit(Resource.Loading<OrderResponse>())
                val toAddressesList = arrayListOf<String>()
                to_addresses?.forEach{
                    toAddressesList.add("{\"search_address_id\":${it.id}}")
                }
                val response: OrderResponse = repository.createOrder(dop_phone, from_address, toAddressesList.toString(), comment, tariff_id, allowances,date_time)
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