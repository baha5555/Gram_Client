package com.gram.client.domain.profile

import android.util.Log
import com.gram.client.utils.Resource
import com.gram.client.domain.AppRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SendProfileUseCase @Inject constructor(
private val repository: AppRepository
) {

    operator fun invoke(
        sendProfileInfoReequest: ProfileInfoSendModel
    ): Flow<Resource<ProfileResponse>> =
        flow{
            try {
                emit(Resource.Loading<ProfileResponse>())
                val response: ProfileResponse = repository.sendProfile(sendProfileInfoReequest)
                emit(Resource.Success<ProfileResponse>(response))
            }catch (e: HttpException) {
                var gson = Gson()
                lateinit var jsonString:String;
                lateinit var response:ProfileResponse
                e.response()?.errorBody()?.let {
                    jsonString = it.string()
                    Log.e("GSON", jsonString)
                    response = gson.fromJson(jsonString, ProfileResponse::class.java)
                }
                emit(
                    Resource.Error<ProfileResponse>(
                        e.localizedMessage ?: "Произошла непредвиденная ошибка",
                        if(e!=null) response else null
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<ProfileResponse>("${e.message}"))
            } catch (e: Exception) {
                emit(Resource.Error<ProfileResponse>("${e.message}"))
            }
        }
}