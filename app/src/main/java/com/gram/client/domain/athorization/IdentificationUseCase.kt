package com.gram.client.domain.athorization

import com.gram.client.utils.Resource
import com.gram.client.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class IdentificationUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(identificationRequest: IdentificationRequest): Flow<Resource<IdentificationResponse>> =
        flow {
            try {
                emit(Resource.Loading<IdentificationResponse>())
                val response = repository.identification(identificationRequest)
                emit(Resource.Success<IdentificationResponse>(response))
            } catch (e: HttpException) {
                emit(
                    Resource.Error<IdentificationResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<IdentificationResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<IdentificationResponse>("${e.message}"))
            }
        }
}