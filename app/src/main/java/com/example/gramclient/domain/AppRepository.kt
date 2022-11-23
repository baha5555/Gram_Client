package com.example.gramclient.domain

import com.example.gramclient.domain.athorization.AuthResponse
import com.example.gramclient.domain.athorization.IdentificationResponse
import retrofit2.Response
import retrofit2.http.*

interface AppRepository {
    @FormUrlEncoded
    @POST("/api/auth/client-registers")
    suspend fun authorization(@Field( "phone_number") phone_number:Long): AuthResponse

    @FormUrlEncoded
    @POST("/api/auth/client-registers/2")
    suspend fun identification(
        @Field( "client_register_id") client_register_id:String,
        @Field( "sms_code") sms_code:Long
    ): IdentificationResponse

    @GET("/api/orders/tariffs")
    suspend fun getTariffs(@Header("Authorization") token: String): TariffsResponse
}