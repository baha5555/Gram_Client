package com.example.gramclient.domain.firebase

import androidx.lifecycle.LiveData
import com.example.gramclient.domain.FirebaseRepository
import com.example.gramclient.domain.firebase.profile.Client
import com.example.gramclient.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetClientOrderUseCase @Inject constructor(private val repository: FirebaseRepository) {
    operator fun invoke(
        client: String,
        goToSearchAddressScreen: () -> Unit
    ): Flow<Resource<LiveData<Client>>> =
        flow {
            try {
                val response: LiveData<Client> = repository.getClientOrder(client, goToSearchAddressScreen)
                emit(Resource.Success(response))
            }catch (_: Exception){

            }
        }
}