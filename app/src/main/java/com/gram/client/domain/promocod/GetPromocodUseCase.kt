package com.gram.client.domain.promocod

import com.gram.client.domain.AppRepository
import com.gram.client.domain.profile.GetProfileInfoResponse
import com.gram.client.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPromocodUseCase @Inject constructor(
    private val repository: AppRepository
) {

    operator fun invoke(): Flow<Resource<GetPromocodResponse>> =
        flow{
            try {
                emit(Resource.Loading<GetPromocodResponse>())
                val response: GetPromocodResponse = repository.getPromocod()
                emit(Resource.Success<GetPromocodResponse>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<GetPromocodResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<GetPromocodResponse>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<GetPromocodResponse>("${e.message}"))
            }
        }
}