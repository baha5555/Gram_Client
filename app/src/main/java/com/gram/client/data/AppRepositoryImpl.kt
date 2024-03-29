package com.gram.client.data

import com.gram.client.app.preference.CustomPreference
import com.gram.client.data.remote.ApplicationApi
import com.gram.client.domain.AppRepository
import com.gram.client.domain.athorization.AuthResponse
import com.gram.client.domain.athorization.IdentificationRequest
import com.gram.client.domain.athorization.IdentificationResponse
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
import com.gram.client.domain.myAddresses.AddMyAddressRequest
import com.gram.client.domain.myAddresses.AddMyAddressesResponse
import com.gram.client.domain.myAddresses.DeleteMyAddressesResponse
import com.gram.client.domain.myAddresses.GetAllMyAddressesResponse
import com.gram.client.domain.myAddresses.UpdateMyAddressRequest
import com.gram.client.domain.myAddresses.UpdateMyAddressResponse
import com.gram.client.domain.orderExecutionScreen.AddRatingResponse
import com.gram.client.domain.orderExecutionScreen.active.ActiveOrdersResponse
import com.gram.client.domain.orderExecutionScreen.reason.GetRatingReasonsResponse
import com.gram.client.domain.orderExecutionScreen.reason.Reasons
import com.gram.client.domain.orderHistory.OrderHistoryPagingResult
import com.gram.client.domain.profile.GetProfileInfoResponse
import com.gram.client.domain.profile.ProfileInfoSendModel
import com.gram.client.domain.profile.ProfileResponse
import com.gram.client.domain.promocod.PromoCode
import com.gram.client.utils.Constants
import okhttp3.MultipartBody

class AppRepositoryImpl(
    private val api: ApplicationApi,
    private val prefs: CustomPreference
) : AppRepository {
    override suspend fun authorization(phone_number: String): AuthResponse =
        api.authorization("${Constants.PREFIX}$phone_number")

    override suspend fun identification(
        identificationRequest: IdentificationRequest
    ): IdentificationResponse = api.identification(identificationRequest)

    override suspend fun getTariffs(): TariffsResponse = api.getTariffs()

    override suspend fun getProfileInfo(): GetProfileInfoResponse =
        api.getProfileInfo(prefs.getAccessToken())

    override suspend fun getPromocod(): PromoCode =
        api.getPromocod(prefs.getAccessToken())

    override suspend fun getOrderHistory(): OrderHistoryPagingResult =
        api.getOrderHistory(prefs.getAccessToken())

    override suspend fun getOrderHistoryResponse(page: Int): OrderHistoryPagingResult =
        api.getOrderHistoryResponse(prefs.getAccessToken(), page)

    override suspend fun getAllowancesByTariffId(tariff_id: Int): AllowancesResponse =
        api.getAllowancesByTariffId(tariff_id)

    override suspend fun sendProfile(
        sendProfileInfoReequest: ProfileInfoSendModel
    ): ProfileResponse =
        api.sendProfile(prefs.getAccessToken(),sendProfileInfoReequest.first_name, sendProfileInfoReequest.last_name, sendProfileInfoReequest.email, sendProfileInfoReequest.avatar)
    override suspend fun sendAvatar(
        avatar: MultipartBody.Part
    ): ProfileResponse =
        api.sendAvatar(prefs.getAccessToken(), avatar)

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

    override suspend fun sendPromoCode(promocod: String?): PromoCode =
        api.sendPromoCode(prefs.getAccessToken(),promocod)

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
        meeting_info,
        null
    )

    override suspend fun getPrice(
        tariff_ids: String,
        allowances: String?,
        search_address_id: Int?,
        to_addresses: String?,
        from_addresses: String?
    ): CalculateResponse =
        api.getPrice(tariff_ids, allowances, search_address_id, to_addresses, from_addresses)

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
        search_address_id: Int?,
        meeting_info: String?,
        to_addresses: String?,
        comment: String?,
        tariff_id: Int,
        allowances: String?,
        from_address: String?,
    ): UpdateOrderResponse = api.editOrder(
        prefs.getAccessToken(),
        order_id,
        dop_phone,
        search_address_id,
        meeting_info,
        to_addresses,
        comment,
        tariff_id,
        allowances,
        from_address
    )

    override suspend fun getFastAddresses(): FastAddressesResponse =
        api.getFastAddresses(prefs.getAccessToken())

    override suspend fun getReasons(): Reasons = api.getReasons(prefs.getAccessToken())

    override suspend fun getRatingReasons(): GetRatingReasonsResponse =
        api.getRatingReasons(prefs.getAccessToken())

    override suspend fun addMyAddresses(
        addMyAddressRequest: AddMyAddressRequest
    ): AddMyAddressesResponse =
        api.addMyAddresses(prefs.getAccessToken(), addMyAddressRequest)

    override suspend fun getAllMyAddresses(): GetAllMyAddressesResponse = api.getAllMyAddresses(prefs.getAccessToken())

    override suspend fun updateMyAddresses(
        updateMyAddressRequest: UpdateMyAddressRequest
    ): UpdateMyAddressResponse =
        api.updateMyAddresses(prefs.getAccessToken(), updateMyAddressRequest.id, updateMyAddressRequest.name, updateMyAddressRequest.search_address_id, updateMyAddressRequest.meet_info, updateMyAddressRequest.comment_to_driver, updateMyAddressRequest.type)

    override suspend fun deleteMyAddresses(
        id: Int,
    ): DeleteMyAddressesResponse =
        api.deleteMyAddresses(prefs.getAccessToken(), id)

    override suspend fun getCountriesKey(str:String): CountriesKeyResponse =
        api.getCountriesKey(prefs.getAccessToken(),str)

}