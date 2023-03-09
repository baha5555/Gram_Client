package com.gram.client.data

import com.gram.client.app.preference.CustomPreference
import com.gram.client.data.remote.ApplicationApi
import com.gram.client.domain.AppRepository
import com.gram.client.domain.mainScreen.TariffsResponse
import com.gram.client.domain.athorization.AuthResponse
import com.gram.client.domain.athorization.IdentificationResponse
import com.gram.client.domain.mainScreen.AddressByPointResponse
import com.gram.client.domain.mainScreen.AllowancesResponse
import com.gram.client.domain.mainScreen.SearchAddressResponse
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
import com.gram.client.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AppRepositoryImpl(
    private val api: ApplicationApi,
    private val prefs: CustomPreference
) : AppRepository {
    override suspend fun authorization(phone_number: String): AuthResponse =
        api.authorization("${Constants.PREFIX}$phone_number")

    override suspend fun identification(
        client_register_id: String,
        sms_code: String,
        fcm_token: String
    ): IdentificationResponse = api.identification(client_register_id, sms_code, fcm_token)

    override suspend fun getTariffs(): TariffsResponse = api.getTariffs()

    override suspend fun getProfileInfo(): GetProfileInfoResponse =
        api.getProfileInfo(prefs.getAccessToken())

    override suspend fun getOrderHistory(): OrderHistoryPagingResult =
        api.getOrderHistory(prefs.getAccessToken())

    override suspend fun getOrderHistoryResponse(page: Int): OrderHistoryPagingResult =
        api.getOrderHistoryResponse(prefs.getAccessToken(), page)

    override suspend fun getAllowancesByTariffId(tariff_id: Int): AllowancesResponse =
        api.getAllowancesByTariffId(tariff_id)

    override suspend fun sendProfile(
        first_name: RequestBody,
        last_name: RequestBody,
        email: String?,
        avatar: MultipartBody.Part?
    ): ProfileResponse =
        api.sendProfile(prefs.getAccessToken(), first_name, last_name, email, avatar)

    override suspend fun getAddressByPoint(
        lng: Double,
        lat: Double
    ): AddressByPointResponse = api.getAddressByPoint(lng, lat)

    override suspend fun sendRating(
        order_id: Int,
        add_rating: Int,
        rating_reason: String?
    ): AddRatingResponse =
        api.sendRating(prefs.getAccessToken(), order_id, add_rating, rating_reason)

    override suspend fun searchAddress(addressName: String?): SearchAddressResponse =
        api.searchAddress(addressName)

    override suspend fun createOrder(
        dop_phone: String?,
        from_address: Int?,
        to_addresses: String?,
        comment: String?,
        tariff_id: Int,
        allowances: String?,
        date_time: String?,
        from_address_point: String?,
        meeting_info: String?
    ): OrderResponse = api.createOrder(
        prefs.getAccessToken(),
        dop_phone,
        from_address,
        to_addresses,
        comment,
        tariff_id,
        allowances,
        date_time,
        from_address_point,
        meeting_info
    )

    override suspend fun getPrice(
        tariff_id: Int,
        allowances: String?,
        search_address_id: Int?,
        to_addresses: String?,
        from_addresses: String?
    ): CalculateResponse =
        api.getPrice(tariff_id, allowances, search_address_id, to_addresses, from_addresses)

    override suspend fun cancelOrder(
        order_id: Int,
        reason_cancel_order: String
    ): CancelOrderResponse =
        api.cancelOrder(prefs.getAccessToken(), order_id, reason_cancel_order)

    override suspend fun getActiveOrders(): ActiveOrdersResponse =
        api.getActiveOrders(prefs.getAccessToken())

    override suspend fun connectClientWithDriver(order_id: String): connectClientWithDriverResponse =
        api.connectClientWithDriver(prefs.getAccessToken(), order_id)

    override suspend fun editOrder(
        order_id: Int,
        dop_phone: String?,
        from_address: Int?,
        meeting_info: String?,
        to_addresses: String?,
        comment: String?,
        tariff_id: Int,
        allowances: String?
    ): UpdateOrderResponse = api.editOrder(
        prefs.getAccessToken(),
        order_id,
        dop_phone,
        from_address,
        meeting_info,
        to_addresses,
        comment,
        tariff_id,
        allowances
    )

    override suspend fun getFastAddresses(): FastAddressesResponse =
        api.getFastAddresses(prefs.getAccessToken())

    override suspend fun getReasons(): GetReasonsResponse = api.getReasons(prefs.getAccessToken())

    override suspend fun getRatingReasons(): GetRatingReasonsResponse =
        api.getRatingReasons(prefs.getAccessToken())

}