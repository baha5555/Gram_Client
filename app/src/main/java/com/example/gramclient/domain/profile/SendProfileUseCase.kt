package com.example.gramclient.domain.profile

import com.example.gramclient.Resource
import com.example.gramclient.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class SendProfileUseCase (private val repository: AppRepository) {

    operator fun invoke(token:String,
                        first_name: String,
                        last_name: String,
                        gender: String,
                        birth_date: String,
                        email: String,
    ): Flow<Resource<ProfileResponse>> =
        flow{
            try {
                emit(Resource.Loading<ProfileResponse>())
                val response: ProfileResponse = repository.sendProfile(token,first_name,last_name,gender,birth_date,email)
                emit(Resource.Success<ProfileResponse>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<ProfileResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<ProfileResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<ProfileResponse>("${e.message}"))
            }
        }
}