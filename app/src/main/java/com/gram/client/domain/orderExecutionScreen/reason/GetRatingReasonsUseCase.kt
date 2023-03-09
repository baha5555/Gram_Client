package com.gram.client.domain.orderExecutionScreen.reason

import com.gram.client.domain.AppRepository
import com.gram.client.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetRatingReasonsUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(): Flow<Resource<GetRatingReasonsResponse>> =
        flow{
            try {
                emit(Resource.Loading())
                val response: GetRatingReasonsResponse = repository.getRatingReasons()
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