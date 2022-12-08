package com.example.gramclient.data.remote

import com.example.gramclient.domain.athorization.AuthResponse
import com.example.gramclient.domain.athorization.IdentificationResponse
import com.example.gramclient.domain.mainScreen.AddressByPointResponse
import com.example.gramclient.domain.mainScreen.AllowancesResponse
import com.example.gramclient.domain.mainScreen.SearchAddressResponse
import com.example.gramclient.domain.mainScreen.TariffsResponse
import com.example.gramclient.domain.mainScreen.order.CalculateResponse
import com.example.gramclient.domain.mainScreen.order.CancelOrderResponse
import com.example.gramclient.domain.mainScreen.order.OrderResponse
import com.example.gramclient.domain.orderExecutionScreen.AddRatingResponse
import com.example.gramclient.domain.orderHistoryScreen.orderHistoryResponse
import com.example.gramclient.domain.profile.GetProfileInfoResponse
import com.example.gramclient.domain.profile.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApplicationApi {
    @FormUrlEncoded
    @POST("/api/auth/client-registers")
    suspend fun authorization(@Field("phone_number") phone_number: Long): AuthResponse

    @FormUrlEncoded
    @POST("/api/auth/client-registers/2")
    suspend fun identification(
        @Field("client_register_id") client_register_id: String,
        @Field("sms_code") sms_code: Long
    ): IdentificationResponse

    @GET("/api/orders/tariffs")
    suspend fun getTariffs(@Header("Authorization") token: String): TariffsResponse

    @GET("api/orders/tariff-allowances")
    suspend fun getAllowancesByTariffId(
        @Header("Authorization") token: String,
        @Query("tariff_id") tariff_id: Int
    ): AllowancesResponse

    @GET("/api/profile")
    suspend fun getProfileInfo(@Header("Authorization") token: String): GetProfileInfoResponse

    @GET("/api/orders")
    suspend fun getOrderHistory(@Header("Authorization") token: String): orderHistoryResponse

    @Multipart
    @POST("/api/profile")
    suspend fun sendProfile(
        @Header("Authorization") token: String,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Query("email") email: String,
        @Part avatar: MultipartBody.Part
    ): ProfileResponse

    @FormUrlEncoded
    @POST("/api/get-address-by-point")
    suspend fun getAddressByPoint(
        @Header("Authorization") token: String,
        @Field("lng") lng: Double,
        @Field("lat") lat: Double
    ): AddressByPointResponse

    @FormUrlEncoded
    @POST("/api/orders/{order_id}/add-rating")
    suspend fun sendRating(
        @Header("Authorization") token: String,
        @Path("order_id") order_id: Int,
        @Field("add_rating") add_rating: Int,
    ): AddRatingResponse

    @FormUrlEncoded
    @POST("/api/orders/search-addresses")
    suspend fun searchAddress(
        @Header("Authorization") token: String,
        @Field("search") addressName: String,
    ): SearchAddressResponse

    @FormUrlEncoded
    @POST("/api/orders")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Field("dop_phone") dop_phone: String?,
        @Field("search_address_id") from_address: Int?,
        @Field("to_addresses") to_addresses: String?,
        @Field("comment") comment: String?,
        @Query("tariff_id") tariff_id : Int,
        @Field("allowances") allowances: String?,
    ): OrderResponse

    @FormUrlEncoded
    @POST("/api/orders/calculate")
    suspend fun getPrice(
        @Header("Authorization") token: String,
        @Query("tariff_id") tariff_id : Int,
        @Field("allowances") allowances: String?,
        @Field("search_address_id") from_address: Int?,
        @Field("to_addresses") to_addresses: String?,
    ): CalculateResponse

    @POST("/api/orders/{order_id}/cancel")
    suspend fun cancelOrder(
        @Header("Authorization") token: String,
        @Path("order_id") order_id: Int,
    ): CancelOrderResponse

}