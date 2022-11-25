package com.example.gramclient.domain.profile

import com.example.gramclient.Resource
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.mainScreen.TariffsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetProfileInfoUseCase (private val repository: AppRepository) {

    operator fun invoke(token:String): Flow<Resource<GetProfileInfoResponse>> =
        flow{
            try {
                emit(Resource.Loading<GetProfileInfoResponse>())
                val response: GetProfileInfoResponse = repository.getProfileInfo(token)
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