package com.example.gramclient.domain.orderExecutionScreen

import com.example.gramclient.Resource
import com.example.gramclient.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class SendAddRatingUseCase(private val repository: AppRepository) {
    operator fun invoke(
        token: String, order_id: Int, add_rating: Int
    ): Flow<Resource<AddRatingResponse>> =
        flow {
            try {
                emit(Resource.Loading<AddRatingResponse>())
                val response: AddRatingResponse =
                    repository.sendRating(token, order_id, add_rating)
                emit(Resource.Success<AddRatingResponse>(response))
            } catch (e: HttpException) {
                emit(
                    Resource.Error<AddRatingResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<AddRatingResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<AddRatingResponse>("${e.message}"))
            }
        }
}