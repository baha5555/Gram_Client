package com.example.gramclient.domain.realtimeDatabase

import androidx.lifecycle.LiveData
import com.example.gramclient.data.firebase.AllClientLiveData
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.FirebaseRepository
import com.example.gramclient.domain.realtimeDatabase.profile.Client
import com.example.gramclient.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RealtimeClientDatabaseUseCase @Inject constructor(private val repository: FirebaseRepository) {
    operator fun invoke(
        client: String,
        goToSearchAddressScreen: () -> Unit
    ): Flow<Resource<LiveData<Client>>> =
        flow {
            try {
                emit(Resource.Loading<LiveData<Client>>())
                val response: LiveData<Client> =
                    repository.getClientOrder(client, goToSearchAddressScreen)
                emit(Resource.Success<LiveData<Client>>(response))
            } catch (e: HttpException) {
                emit(
                    Resource.Error<LiveData<Client>>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<LiveData<Client>>("Не удалось связаться с сервером. Проверьте подключение к Интернету."))
            } catch (e: Exception) {
                emit(Resource.Error<LiveData<Client>>("${e.message}"))
            }
        }
}