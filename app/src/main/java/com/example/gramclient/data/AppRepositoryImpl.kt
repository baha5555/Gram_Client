package com.example.gramclient.data

import androidx.lifecycle.LiveData
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
import com.example.gramclient.domain.mainScreen.order.UpdateOrderResponse
import com.example.gramclient.domain.mainScreen.order.connectClientWithDriver.connectClientWithDriverResponse
import com.example.gramclient.domain.orderExecutionScreen.ActiveOrdersResponse
import com.example.gramclient.domain.orderExecutionScreen.AddRatingResponse
import com.example.gramclient.domain.orderHistoryScreen.orderHistoryResponse
import com.example.gramclient.domain.profile.GetProfileInfoResponse
import com.example.gramclient.domain.profile.ProfileResponse
import com.example.gramclient.domain.realtimeDatabase.AllNotesLiveData
import com.example.gramclient.domain.realtimeDatabase.Order.RealtimeDatabaseOrder
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AppRepositoryImpl(
    private val api: ApplicationApi
):AppRepository {
    override val readAll: LiveData<List<RealtimeDatabaseOrder>> = AllNotesLiveData()

    override suspend fun authorization(phone_number: Long): AuthResponse = api.authorization("${Constants.PREFIX}$phone_number".toLong())

    override suspend fun identification(client_register_id: String, sms_code: Long,fcm_token:String): IdentificationResponse = api.identification(client_register_id, sms_code,fcm_token)

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
        avatar: MultipartBody.Part?
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

    override suspend fun getActiveOrders(token: String): ActiveOrdersResponse = api.getActiveOrders(token)

    override suspend fun connectClientWithDriver(
        token: String,
        order_id: String
    ): connectClientWithDriverResponse = api.connectClientWithDriver(token,order_id)
    override suspend fun editOrder(
        token: String,
        order_id: Int,
        dop_phone: String?,
        from_address: Int?,
        meeting_info: String?,
        to_addresses: String?,
        comment: String?,
        tariff_id: Int,
        allowances: String?
    ): UpdateOrderResponse = api.editOrder(token, order_id, dop_phone, from_address, meeting_info, to_addresses, comment, tariff_id, allowances)
}