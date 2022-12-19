package com.example.gramclient.data

import com.example.gramclient.Constants
import com.example.gramclient.data.remote.ApplicationApi
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.mainScreen.TariffsResponse
import com.example.gramclient.domain.athorization.AuthResponse
import com.example.gramclient.domain.athorization.IdentificationResponse
import com.example.gramclient.domain.mainScreen.AddressByPointResponse
import com.example.gramclient.domain.mainScreen.AllowancesResponse
import com.example.gramclient.domain.mainScreen.SearchAddressResponse
import com.example.gramclient.domain.mainScreen.order.CalculateResponse
import com.example.gramclient.domain.mainScreen.order.CancelOrderResponse
import com.example.gramclient.domain.mainScreen.order.OrderResponse
import com.example.gramclient.domain.orderExecutionScreen.AddRatingResponse
import com.example.gramclient.domain.orderHistoryScreen.orderHistoryResponse
import com.example.gramclient.domain.profile.GetProfileInfoResponse
import com.example.gramclient.domain.profile.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class AppRepositoryImpl(
    private val api: ApplicationApi
):AppRepository {
    override suspend fun authorization(phone_number: Long): AuthResponse = api.authorization("${Constants.PREFIX}$phone_number".toLong())

    override suspend fun identification(client_register_id: String, sms_code: Long): IdentificationResponse = api.identification(client_register_id, sms_code)

    override suspend fun getTariffs(): TariffsResponse = api.getTariffs()

    override suspend fun getProfileInfo(token: String): GetProfileInfoResponse = api.getProfileInfo(token)

    override suspend fun getOrderHistory(token: String): orderHistoryResponse = api.getOrderHistory(token)

    override suspend fun getAllowancesByTariffId(
        tariff_id: Int
    ): AllowancesResponse = api.getAllowancesByTariffId(tariff_id)

    override suspend fun sendProfile(
        token: String,
        first_name: RequestBody,
        last_name: RequestBody,
        email: String,
        avatar: MultipartBody.Part
    ): ProfileResponse = api.sendProfile(token, first_name, last_name, email, avatar)

    override suspend fun getAddressByPoint(
        lng: Double,
        lat: Double
    ): AddressByPointResponse = api.getAddressByPoint(lng, lat)

    override suspend fun sendRating(
        token: String,
        order_id: Int,
        add_rating: Int
    ): AddRatingResponse = api.sendRating(token, order_id, add_rating)

    override suspend fun searchAddress(addressName: String): SearchAddressResponse = api.searchAddress(addressName)

    override suspend fun createOrder(
        token: String,
        dop_phone: String?,
        from_address: Int?,
        to_addresses: String?,
        comment: String?,
        tariff_id: Int,
        allowances: String?
    ): OrderResponse = api.createOrder(token, dop_phone, from_address, to_addresses, comment, tariff_id, allowances)

    override suspend fun getPrice(
        tariff_id: Int,
        allowances: String?,
        from_address: Int?,
        to_addresses: String?
    ): CalculateResponse = api.getPrice(tariff_id, allowances, from_address, to_addresses)

    override suspend fun cancelOrder(token: String, order_id: Int): CancelOrderResponse = api.cancelOrder(token, order_id)
}