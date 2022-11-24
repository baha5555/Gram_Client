package com.example.gramclient.data

import com.example.gramclient.Constants
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.mainScreen.TariffsResponse
import com.example.gramclient.domain.athorization.AuthResponse
import com.example.gramclient.domain.athorization.IdentificationResponse
import com.example.gramclient.domain.mainScreen.AllowancesResponse
import com.example.gramclient.domain.profile.ProfileResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object AppRepositoryImpl:AppRepository {
    override suspend fun authorization(phone_number: Long): AuthResponse {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(Constants.LOCAL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build())
            .build()
        val authApi = retrofit.create(AppRepository::class.java)
        val phone="${Constants.PREFIX}$phone_number".toLong()
        authApi.authorization(phone).let{
            return it
        }
    }

    override suspend fun identification(client_register_id: String, sms_code: Long): IdentificationResponse {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(Constants.LOCAL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build())
            .build()
        val authApi = retrofit.create(AppRepository::class.java)
        authApi.identification(client_register_id, sms_code).let{
            return it
        }
    }

    override suspend fun getTariffs(token: String): TariffsResponse {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(Constants.LOCAL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build())
            .build()
        val tariffsApi = retrofit.create(AppRepository::class.java)
        tariffsApi.getTariffs(token).let{
            return it
        }
    }

    override suspend fun getAllowancesByTariffId(
        token: String,
        tariff_id: Int
    ): AllowancesResponse {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(Constants.LOCAL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build())
            .build()
        val allowancesApi = retrofit.create(AppRepository::class.java)
        allowancesApi.getAllowancesByTariffId(token, tariff_id).let{
            return it
        }
    }

    override suspend fun sendProfile(
        token: String,
        first_name: String,
        last_name: String,
        gender: String,
        birth_date: Date,
        email: String
    ): ProfileResponse {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(Constants.LOCAL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build())
            .build()
        val profileApi = retrofit.create(AppRepository::class.java)
        profileApi.sendProfile(token,first_name,last_name,gender,birth_date,email).let{
            return it
        }
    }
}