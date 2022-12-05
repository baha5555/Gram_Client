package com.example.gramclient.domain

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
import com.example.gramclient.domain.profile.GetProfileInfoResponse
import com.example.gramclient.domain.profile.ProfileResponse
import okhttp3.MultipartBody
import retrofit2.http.*
import java.util.*

interface AppRepository {
    suspend fun authorization(phone_number: Long): AuthResponse

    suspend fun identification(
        client_register_id: String,
        sms_code: Long
    ): IdentificationResponse

    suspend fun getTariffs(token: String): TariffsResponse

    suspend fun getAllowancesByTariffId(
         token: String,
         tariff_id: Int
    ): AllowancesResponse

    suspend fun getProfileInfo(token: String): GetProfileInfoResponse


    suspend fun sendProfile(
         token: String,
         first_name: String,
         last_name: String,
         gender: String,
         birth_date: String,
         email: String,
         avatar: MultipartBody.Part
    ): ProfileResponse


    suspend fun getAddressByPoint(
        token: String,
        lng: Double,
        lat: Double
    ): AddressByPointResponse


    suspend fun sendRating(
         token: String,
         order_id: Int,
         add_rating: Int,
    ): AddRatingResponse


    suspend fun searchAddress(
         token: String,
         addressName: String,
    ): SearchAddressResponse


    suspend fun createOrder(
        token: String,
        dop_phone: String?,
        from_address: Int?,
        to_addresses: String?,
        comment: String?,
        tariff_id : Int,
        allowances: String?,
        ): OrderResponse


    suspend fun getPrice(
        token: String,
        tariff_id : Int,
        allowances: String?,
        from_address: Int?,
        to_addresses: String?,
    ): CalculateResponse


    suspend fun cancelOrder(
         token: String,
         order_id: Int,
    ): CancelOrderResponse

}

