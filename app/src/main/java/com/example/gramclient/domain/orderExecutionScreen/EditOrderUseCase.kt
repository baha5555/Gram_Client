package com.example.gramclient.domain.orderExecutionScreen

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.gramclient.utils.Resource
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.domain.mainScreen.order.UpdateOrderResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class EditOrderUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(
        order_id: Int,
        dop_phone: String?,
        from_address: Int?,
        meeting_info: String?,
        to_addresses: SnapshotStateList<Address>,
        comment: String?,
        tariff_id: Int,
        allowances: String?,
    ): Flow<Resource<UpdateOrderResponse>> =
        flow{
            try {
                emit(Resource.Loading<UpdateOrderResponse>())
                val toAddressesList = arrayListOf<String>()
                to_addresses.forEach{
                    toAddressesList.add("{\"search_address_id\":${it.id}}")
                }
                val response: UpdateOrderResponse = repository.editOrder(order_id, dop_phone, from_address, meeting_info, toAddressesList.toString(), comment, tariff_id, allowances)
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