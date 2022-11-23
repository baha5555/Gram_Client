package com.example.gramclient.domain.mainScreen

import com.example.gramclient.Resource
import com.example.gramclient.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException


class GetAllowancesUseCase(private val repository: AppRepository) {

    operator fun invoke(token:String, tariff_id:Int): Flow<Resource<AllowancesResponse>> =
        flow{
            try {
                emit(Resource.Loading<AllowancesResponse>())
                val response: AllowancesResponse = repository.getAllowancesByTariffId(token, tariff_id)
                emit(Resource.Success<AllowancesResponse>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<AllowancesResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<AllowancesResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<AllowancesResponse>("${e.message}"))
            }
        }
}