package com.example.gramclient.domain.mainScreen

import com.example.gramclient.Resource
import com.example.gramclient.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchAddressUseCase @Inject constructor(private val repository: AppRepository) {
    operator fun invoke(addressName: String): Flow<Resource<SearchAddressResponse>> =
        flow{
            try {
                emit(Resource.Loading<SearchAddressResponse>())
                val response: SearchAddressResponse = repository.searchAddress(addressName)
                emit(Resource.Success<SearchAddressResponse>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<SearchAddressResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<SearchAddressResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<SearchAddressResponse>("${e.message}"))
            }
        }
}