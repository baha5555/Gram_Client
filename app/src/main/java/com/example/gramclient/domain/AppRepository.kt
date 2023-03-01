package com.example.gramclient.domain

import com.example.gramclient.domain.athorization.AuthResponse
import com.example.gramclient.domain.athorization.IdentificationResponse
import com.example.gramclient.domain.mainScreen.AddressByPointResponse
import com.example.gramclient.domain.mainScreen.AllowancesResponse
import com.example.gramclient.domain.mainScreen.SearchAddressResponse
import com.example.gramclient.domain.mainScreen.TariffsResponse
import com.example.gramclient.domain.mainScreen.fast_address.FastAddressesResponse
import com.example.gramclient.domain.mainScreen.order.CalculateResponse
import com.example.gramclient.domain.mainScreen.order.CancelOrderResponse
import com.example.gramclient.domain.mainScreen.order.OrderResponse
import com.example.gramclient.domain.mainScreen.order.UpdateOrderResponse
import com.example.gramclient.domain.mainScreen.order.connectClientWithDriver.connectClientWithDriverResponse
import com.example.gramclient.domain.orderExecutionScreen.ActiveOrdersResponse
import com.example.gramclient.domain.orderExecutionScreen.AddRatingResponse
import com.example.gramclient.domain.orderHistory.OrderHistoryPagingResult

import com.example.gramclient.domain.orderHistoryScreen.orderHistoryResponse
import com.example.gramclient.domain.profile.GetProfileInfoResponse
import com.example.gramclient.domain.profile.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AppRepository {
    suspend fun authorization(phone_number: String): AuthResponse

    suspend fun identification(
        client_register_id: String,
        sms_code: String,
        fcm_token: String
    ): IdentificationResponse

    suspend fun getTariffs(): TariffsResponse
    suspend fun getAllowancesByTariffId(tariff_id: Int): AllowancesResponse
    suspend fun getProfileInfo(): GetProfileInfoResponse
    suspend fun getOrderHistory(): OrderHistoryPagingResult
    suspend fun getOrderHistoryResponse(page:Int): OrderHistoryPagingResult
    suspend fun connectClientWithDriver(order_id: String): connectClientWithDriverResponse
    suspend fun sendProfile(
        first_name: RequestBody,
        last_name: RequestBody,
        email: String?,
        avatar: MultipartBody.Part?
    ): ProfileResponse

    suspend fun getAddressByPoint(lng: Double, lat: Double): AddressByPointResponse
    suspend fun sendRating(order_id: Int, add_rating: Int): AddRatingResponse
    suspend fun searchAddress(addressName: String): SearchAddressResponse
    suspend fun createOrder(
        dop_phone: String?,
        from_address: Int?,
        to_addresses: String?,
        comment: String?,
        tariff_id: Int,
        allowances: String?,
        date_time:String?,
        from_address_point: String?,
        check_point_start: Short
    ): OrderResponse


    suspend fun getPrice(
        tariff_id: Int,
        allowances: String?,
        from_address: Int?,
        to_addresses: String?,
    ): CalculateResponse

    suspend fun cancelOrder(order_id: Int): CancelOrderResponse

    suspend fun getActiveOrders(): ActiveOrdersResponse

    suspend fun editOrder(
        order_id: Int,
        dop_phone: String?,
        from_address: Int?,
        meeting_info: String?,
        to_addresses: String?,
        comment: String?,
        tariff_id: Int,
        allowances: String?,
    ): UpdateOrderResponse

    suspend fun getFastAddresses(): FastAddressesResponse
}

