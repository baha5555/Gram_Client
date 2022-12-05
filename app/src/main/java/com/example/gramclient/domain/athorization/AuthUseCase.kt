package com.example.gramclient.domain.athorization

import com.example.gramclient.Resource
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.mainScreen.TariffsResponse
import retrofit2.HttpException
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(phone:Int): Flow<Resource<AuthResponse>> =
        flow{
            try {
                emit(Resource.Loading<AuthResponse>())
                val response: AuthResponse = repository.authorization(phone.toLong())
                emit(Resource.Success<AuthResponse>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<AuthResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<AuthResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<AuthResponse>("${e.message}"))
            }
        }
}