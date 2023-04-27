package com.gram.client.data.remote

import com.gram.client.domain.athorization.AuthResponse
import com.gram.client.domain.athorization.IdentificationResponse
import com.gram.client.domain.athorization.IdentificationRequest
import com.gram.client.domain.countries.CountriesKeyResponse
import com.gram.client.domain.mainScreen.AddressByPointResponse
import com.gram.client.domain.mainScreen.AllowancesResponse
import com.gram.client.domain.mainScreen.SearchAddressResponse
import com.gram.client.domain.mainScreen.TariffsResponse
import com.gram.client.domain.mainScreen.fast_address.FastAddressesResponse
import com.gram.client.domain.mainScreen.order.CalculateResponse
import com.gram.client.domain.mainScreen.order.CancelOrderResponse
import com.gram.client.domain.mainScreen.order.OrderResponse
import com.gram.client.domain.mainScreen.order.UpdateOrderResponse
import com.gram.client.domain.mainScreen.order.connectClientWithDriver.connectClientWithDriverResponse
import com.gram.client.domain.myAddresses.*
import com.gram.client.domain.orderExecutionScreen.ActiveOrdersResponse
import com.gram.client.domain.orderExecutionScreen.AddRatingResponse
import com.gram.client.domain.orderExecutionScreen.reason.GetRatingReasonsResponse
import com.gram.client.domain.orderExecutionScreen.reason.Reasons
import com.gram.client.domain.orderHistory.OrderHistoryPagingResult

import com.gram.client.domain.profile.GetProfileInfoResponse
import com.gram.client.domain.profile.ProfileResponse
import com.gram.client.domain.promocod.PromoCode
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApplicationApi {
    @FormUrlEncoded
    @POST("/api/auth/client-registers")
    suspend fun authorization(@Field("phone_number") phone_number: String): AuthResponse

    @POST("/api/auth/client-registers/2")
    suspend fun identification(
        @Body identificationRequest: IdentificationRequest
    ): IdentificationResponse

    @GET("/api/orders/tariffs")
    suspend fun getTariffs(): TariffsResponse

    @GET("api/orders/tariff-allowances")
    suspend fun getAllowancesByTariffId(
        @Query("tariff_id") tariff_id: Int
    ): AllowancesResponse

    @GET("/api/profile")
    suspend fun getProfileInfo(@Header("Authorization") token: String): GetProfileInfoResponse

    @GET("/api/edit")
    suspend fun getPromocod(@Header("Authorization") token: String): PromoCode



    @GET("/api/orders")
    suspend fun getOrderHistory(@Header("Authorization") token: String): OrderHistoryPagingResult

    @GET("/api/orders")
    suspend fun getOrderHistoryResponse(
        @Header("Authorization") token: String,
        @Query("page") page:Int
    ): OrderHistoryPagingResult

    @Multipart
    @POST("/api/profile")
    suspend fun sendProfile(
        @Header("Authorization") token: String,
        @Part("first_name")  first_name: RequestBody,
        @Part("last_name")  last_name: RequestBody,
        @Query("email")  email: String?,
        @Part avatar: MultipartBody.Part?
    ): ProfileResponse

    @FormUrlEncoded
    @POST("/api/get-address-by-point")
    suspend fun getAddressByPoint(
        @Field("lng") lng: Double,
        @Field("lat") lat: Double
    ): AddressByPointResponse

    @FormUrlEncoded
    @POST("/api/orders/add-rating")
    suspend fun sendRating(
        @Header("Authorization") token: String,
        @Query("order_id") order_id: Int,
        @Field("add_rating") add_rating: Int,
        @Query("rating_reason") rating_reason: String?,
    ): AddRatingResponse

    @FormUrlEncoded
    @POST("/api/mob-app/orders/search-addresses")
    suspend fun searchAddress(
        @Field("search") promocod: String?,
    ): SearchAddressResponse

    @FormUrlEncoded
    @POST("/api/active-promocode")
    suspend fun sendPromoCode(
        @Header("Authorization") token: String,
        @Field("promocode") promocode: String?,
    ): PromoCode

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
        @Field("date_time")date_time:String?,
        @Field("from_address") from_address_point:String?,
        @Field("meeting_info") meeting_info:String?): OrderResponse

    @FormUrlEncoded
    @POST("/api/orders/calculate")
    suspend fun getPrice(
        @Query("tariff_ids") tariff_ids : String,
        @Field("allowances") allowances: String?,
        @Field("value_allowances") value_allowances: String?,
        @Field("search_address_id") search_address_id: Int?,
        @Field("to_addresses") to_addresses: String?,
        @Field("from_address") from_address: String?,
    ): CalculateResponse

    @POST("/api/orders/cancel")
    suspend fun cancelOrder(
        @Header("Authorization") token: String,
        @Query("order_id") order_id: Int,
        @Query("reason_cancel_order") reason_cancel_order: String,
    ): CancelOrderResponse

    @GET("/api/orders/active")
    suspend fun getActiveOrders(
        @Header("Authorization") token: String,
    ): ActiveOrdersResponse

    @POST("api/orders/connect-client-performer/{order_id}")
    suspend fun connectClientWithDriver(
        @Header("Authorization")token:String,
        @Path("order_id")order_id:String
    ):connectClientWithDriverResponse

    @PATCH("/api/orders/{order_id}")
    suspend fun editOrder(
        @Header("Authorization") token: String,
        @Path("order_id") order_id: Int,
        @Query("dop_phone") dop_phone: String?,
        @Query("search_address_id") search_address_id: Int?,
        @Query("meeting_info") meeting_info: String?,
        @Query("to_addresses") to_addresses: String?,
        @Query("comment") comment: String?,
        @Query("tariff_id") tariff_id : Int,
        @Query("allowances") allowances: String?,
        @Query("from_address") from_address: String?,
    ): UpdateOrderResponse

    @GET("/api/mob-app/popular-addresses")
    suspend fun getFastAddresses(
        @Header("Authorization") token: String,
    ) : FastAddressesResponse

    @GET("/api/orders/reason")
    suspend fun getReasons(
        @Header("Authorization") token: String,
    ) : Reasons

    @GET("/api/orders/rating-reason-list")
    suspend fun getRatingReasons(
        @Header("Authorization") token: String,
    ) : GetRatingReasonsResponse

    @POST("/api/mob-app/my-address/create")
    suspend fun addMyAddresses(
        @Header("Authorization") token: String,
        @Body addMyAddressRequest: AddMyAddressRequest,
    ): AddMyAddressesResponse

    @GET("/api/mob-app/my-address/all")
    suspend fun getAllMyAddresses(
        @Header("Authorization") token: String,
    ) : GetAllMyAddressesResponse

    @FormUrlEncoded
    @PATCH("/api/mob-app/my-address/{id}/update")
    suspend fun updateMyAddresses(
        @Header("Authorization") token: String,
        @Body updateMyAddressRequest: UpdateMyAddressRequest
    ): UpdateMyAddressResponse

    @DELETE("/api/mob-app/my-address/{id}/delete")
    suspend fun deleteMyAddresses(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        ): DeleteMyAddressesResponse

    @GET("/api/mob-app/countries/{code}")
    suspend fun getCountriesKey(
        @Header("Authorization") token: String,
        @Path("code") code :String
    ): CountriesKeyResponse
}