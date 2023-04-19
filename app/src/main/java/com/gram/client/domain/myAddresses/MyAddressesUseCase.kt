package com.gram.client.domain.myAddresses

import com.gram.client.utils.Resource
import com.gram.client.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class MyAddressesUseCase @Inject constructor(private val repository: AppRepository) {
    fun addMyAddresses(
       addMyAddressRequest: AddMyAddressRequest
    ): Flow<Resource<AddMyAddressesResponse>> =
        flow {
            try {
                emit(Resource.Loading())
                val response = repository.addMyAddresses(
                   addMyAddressRequest
                )

                emit(Resource.Success(response))
            } catch (e: HttpException) {
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

    fun getAllMyAddresses(): Flow<Resource<GetAllMyAddressesResponse>> =
        flow {
            try {
                emit(Resource.Loading())
                val response = repository.getAllMyAddresses()

                emit(Resource.Success(response))
            } catch (e: HttpException) {
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

    fun updateMyAddresses(
       updateMyAddressRequest: UpdateMyAddressRequest
    ): Flow<Resource<UpdateMyAddressResponse>> =
        flow {
            try {
                emit(Resource.Loading())
                val response = repository.updateMyAddresses(updateMyAddressRequest)

                emit(Resource.Success(response))
            } catch (e: HttpException) {
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

    fun deleteMyAddresses(
        id: Int
    ): Flow<Resource<DeleteMyAddressesResponse>> =
        flow {
            try {
                emit(Resource.Loading())
                val response = repository.deleteMyAddresses(id)

                emit(Resource.Success(response))
            } catch (e: HttpException) {
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