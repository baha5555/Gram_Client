package com.example.gramclient.domain.profile

import androidx.compose.runtime.MutableState
import com.example.gramclient.utils.Resource
import com.example.gramclient.domain.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject

class SendProfileUseCase @Inject constructor(
private val repository: AppRepository
) {

    operator fun invoke(
                        first_name: RequestBody,
                        last_name: RequestBody,
                        email: String?,
                        avatar: MutableState<File?>
    ): Flow<Resource<ProfileResponse>> =
        flow{
            try {
                emit(Resource.Loading<ProfileResponse>())
                val response: ProfileResponse = repository.sendProfile(first_name,last_name,email,
                    if(avatar.value!=null)
                        MultipartBody.Part
                        .createFormData(
                            name = "avatar",
                            filename = avatar.value!!.name,
                            body = avatar.value!!.asRequestBody()
                        )
                else null
                )
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