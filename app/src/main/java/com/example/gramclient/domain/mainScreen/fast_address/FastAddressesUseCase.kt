package com.example.gramclient.domain.mainScreen.fast_address

import com.example.gramclient.utils.Resource
import com.example.gramclient.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class FastAddressesUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(): Flow<Resource<FastAddressesResponse>> =
        flow{
            try {
                emit(Resource.Loading())
                val response: FastAddressesResponse = repository.getFastAddresses()
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