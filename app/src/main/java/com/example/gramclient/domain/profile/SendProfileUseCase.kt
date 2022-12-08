package com.example.gramclient.domain.profile

import com.example.gramclient.Resource
import com.example.gramclient.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import javax.inject.Inject

class SendProfileUseCase @Inject constructor(
private val repository: AppRepository
) {

    operator fun invoke(token:String,
                        first_name: RequestBody,
                        last_name: RequestBody,
                        email: String,
                        avatar: MultipartBody.Part
    ): Flow<Resource<ProfileResponse>> =
        flow{
            try {
                emit(Resource.Loading<ProfileResponse>())
                val response: ProfileResponse = repository.sendProfile(token,first_name,last_name,email,avatar)
                emit(Resource.Success<ProfileResponse>(response))
            }catch (e: HttpException) {
                emit(
                    Resource.Error<ProfileResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<ProfileResponse>("${e.message}"))
            } catch (e: Exception) {
                emit(Resource.Error<ProfileResponse>("${e.message}"))
            }
        }
}