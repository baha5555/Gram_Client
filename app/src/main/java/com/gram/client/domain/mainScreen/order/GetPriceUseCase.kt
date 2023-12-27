package com.gram.client.domain.mainScreen.order

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.gram.client.domain.AppRepository
import com.gram.client.domain.mainScreen.Address
import com.gram.client.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetPriceUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(
        tariff_ids: String,
        allowances: String?,

        search_address_id: Int?,
        to_addresses: SnapshotStateList<Address>?,
        from_address: String?
    ): Flow<Resource<CalculateResponse>> =
        flow{
            try {
                emit(Resource.Loading())
                val toAddressesList = arrayListOf<String>()
                to_addresses?.forEach{
                    toAddressesList.add("{\"search_address_id\":${it.id}}")
                }
                val response: CalculateResponse = repository.getPrice(tariff_ids, allowances, search_address_id, toAddressesList.toString(), from_address)
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