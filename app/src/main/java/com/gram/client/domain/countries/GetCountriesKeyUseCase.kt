package com.gram.client.domain.countries

import com.gram.client.domain.AppRepository
import com.gram.client.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCountriesKeyUseCase @Inject constructor(private val repository: AppRepository) {
    operator fun invoke(str:String
    ): Flow<Resource<CountriesKeyResponse>> =
        flow {
            try {
                emit(Resource.Loading<CountriesKeyResponse>())
                val response: CountriesKeyResponse =
                    repository.getCountriesKey(str)
                emit(Resource.Success<CountriesKeyResponse>(response))
            } catch (e: HttpException) {
                emit(
                    Resource.Error<CountriesKeyResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<CountriesKeyResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<CountriesKeyResponse>("${e.message}"))
            }
        }
}