package com.example.gramclient.domain.profile

import com.example.gramclient.utils.Resource
import com.example.gramclient.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetProfileInfoUseCase @Inject constructor(
    private val repository: AppRepository
) {

    operator fun invoke(): Flow<Resource<GetProfileInfoResponse>> =
        flow{
            try {
                emit(Resource.Loading<GetProfileInfoResponse>())
                val response: GetProfileInfoResponse = repository.getProfileInfo()
                emit(Resource.Success<GetProfileInfoResponse>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<GetProfileInfoResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<GetProfileInfoResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<GetProfileInfoResponse>("${e.message}"))
            }
        }
}