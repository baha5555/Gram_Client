package com.gram.client.domain.promocod

import com.gram.client.domain.AppRepository
import com.gram.client.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class SendPromoCodeUseCase @Inject constructor(private val repository: AppRepository) {
    operator fun invoke(promocod: String?): Flow<Resource<PromoCode>> =
        flow{
            try {
                emit(Resource.Loading<PromoCode>())
                val response: PromoCode = repository.sendPromoCode(promocod)
                emit(Resource.Success<PromoCode>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<PromoCode>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<PromoCode>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<PromoCode>("${e.message}"))
            }
        }
}