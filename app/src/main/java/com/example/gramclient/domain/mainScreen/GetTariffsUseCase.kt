package com.example.gramclient.domain.mainScreen

import com.example.gramclient.Resource
import com.example.gramclient.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetTariffsUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(): Flow<Resource<TariffsResponse>> =
        flow{
            try {
                emit(Resource.Loading<TariffsResponse>())
                val response: TariffsResponse = repository.getTariffs()
                emit(Resource.Success<TariffsResponse>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<TariffsResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<TariffsResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<TariffsResponse>("${e.message}"))
            }
        }
}