package com.gram.client.domain.orderExecutionScreen

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.gram.client.domain.AppRepository
import com.gram.client.domain.mainScreen.Address
import com.gram.client.domain.mainScreen.order.UpdateOrderResponse
import com.gram.client.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class EditOrderUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(
        order_id: Int,
        dop_phone: String?,
        search_address_id: Int?,
        meeting_info: String?,
        to_addresses: SnapshotStateList<Address>,
        comment: String?,
        tariff_id : Int,
        allowances: String?,
        from_address: String?,
    ): Flow<Resource<UpdateOrderResponse>> =
        flow{
            try {
                emit(Resource.Loading())
                val toAddressesList = arrayListOf<String>()
                to_addresses.forEach{
                    toAddressesList.add("{\"search_address_id\":${it.id}}")
                }
                val response: UpdateOrderResponse = repository.editOrder(order_id, dop_phone, search_address_id, meeting_info, toAddressesList.toString(), comment, tariff_id, allowances, from_address)
                emit(Resource.Success(response))
            }catch (e: HttpException) {
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