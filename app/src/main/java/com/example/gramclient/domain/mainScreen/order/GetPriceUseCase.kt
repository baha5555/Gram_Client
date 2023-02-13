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


class GetPriceUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(
        tariff_id: Int,
        allowances: String?,
        from_address: Int?,
        to_addresses: SnapshotStateList<Address>?
    ): Flow<Resource<CalculateResponse>> =
        flow{
            try {
                emit(Resource.Loading())
                val toAddressesList = arrayListOf<String>()
                to_addresses?.forEach{
                    toAddressesList.add("{\"search_address_id\":${it.id}}")
                }
                val response: CalculateResponse = repository.getPrice(tariff_id, allowances, from_address, toAddressesList.toString())
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