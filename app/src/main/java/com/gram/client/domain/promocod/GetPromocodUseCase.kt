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

    operator fun invoke(): Flow<Resource<Promocod>> =
        flow{
            try {
                emit(Resource.Loading<Promocod>())
                val response: Promocod = repository.getPromocod()
                emit(Resource.Success<Promocod>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<Promocod>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<Promocod>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<Promocod>("${e.message}"))
            }
        }
}