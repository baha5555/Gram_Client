package com.gram.client.domain.firebase

import androidx.lifecycle.LiveData
import com.gram.client.domain.FirebaseRepository
import com.gram.client.domain.firebase.profile.Client
import com.gram.client.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetClientOrderUseCase @Inject constructor(private val repository: FirebaseRepository) {
    operator fun invoke(
        client: String
    ): Flow<Resource<LiveData<Client>>> =
        flow {
            try {
                val response: LiveData<Client> = repository.getClientOrder(client)
                emit(Resource.Success(response))
            }catch (_: Exception){

            }
        }
}