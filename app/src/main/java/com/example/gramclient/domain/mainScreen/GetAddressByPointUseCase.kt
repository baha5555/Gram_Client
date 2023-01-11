package com.example.gramclient.domain.mainScreen

import com.example.gramclient.utils.Resource
import com.example.gramclient.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetAddressByPointUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(lng: Double, lat: Double): Flow<Resource<AddressByPointResponse>> =
        flow{
            try {
                emit(Resource.Loading<AddressByPointResponse>())
                val response: AddressByPointResponse = repository.getAddressByPoint(lng, lat)
                emit(Resource.Success<AddressByPointResponse>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<AddressByPointResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<AddressByPointResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<AddressByPointResponse>("${e.message}"))
            }
        }
}