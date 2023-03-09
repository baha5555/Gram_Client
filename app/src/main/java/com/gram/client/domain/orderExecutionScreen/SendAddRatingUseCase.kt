package com.gram.client.domain.orderExecutionScreen

import com.gram.client.utils.Resource
import com.gram.client.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SendAddRatingUseCase @Inject constructor(private val repository: AppRepository) {
    operator fun invoke(
        order_id: Int, add_rating: Int,
        rating_reason: String?
    ): Flow<Resource<AddRatingResponse>> =
        flow {
            try {
                emit(Resource.Loading<AddRatingResponse>())
                val response: AddRatingResponse =
                    repository.sendRating(order_id, add_rating, rating_reason)
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