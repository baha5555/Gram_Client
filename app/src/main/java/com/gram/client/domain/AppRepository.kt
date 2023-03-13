package com.gram.client.domain

import com.gram.client.domain.athorization.AuthResponse
import com.gram.client.domain.athorization.IdentificationResponse
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
import com.gram.client.domain.orderExecutionScreen.ActiveOrdersResponse
import com.gram.client.domain.orderExecutionScreen.AddRatingResponse
import com.gram.client.domain.orderExecutionScreen.reason.GetRatingReasonsResponse
import com.gram.client.domain.orderExecutionScreen.reason.GetReasonsResponse
import com.gram.client.domain.orderHistory.OrderHistoryPagingResult

import com.gram.client.domain.profile.GetProfileInfoResponse
import com.gram.client.domain.profile.ProfileResponse
import com.gram.client.domain.promocod.GetPromocodResponse
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
    suspend fun getPromocod(): GetPromocodResponse
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
    suspend fun sendRating(order_id: Int, add_rating: Int,rating_reason:String?): AddRatingResponse
    suspend fun searchAddress(addressName: String?): SearchAddressResponse
    suspend fun createOrder(
        dop_phone: String?,
        from_address: Int?,
        to_addresses: String?,
        comment: String?,
        tariff_id: Int,
        allowances: String?,
        date_time:String?,
        from_address_point: String?,
        meeting_info: String?
    ): OrderResponse


    suspend fun getPrice(
        tariff_ids: String,
        allowances: String?,
        value_allowances: String?,
        search_address_id: Int?,
        to_addresses: String?,
        from_addresses: String?
    ): CalculateResponse

    suspend fun cancelOrder(order_id: Int, reason_cancel_order: String): CancelOrderResponse

    suspend fun getActiveOrders(): ActiveOrdersResponse

    suspend fun editOrder(
        order_id: Int,
        dop_phone: String?,
        search_address_id: Int?,
        meeting_info: String?,
        to_addresses: String?,
        comment: String?,
        tariff_id: Int,
        allowances: String?,
        from_address: String?,
    ): UpdateOrderResponse

    suspend fun getFastAddresses(): FastAddressesResponse

    suspend fun getReasons(): GetReasonsResponse

    suspend fun getRatingReasons(): GetRatingReasonsResponse

}

