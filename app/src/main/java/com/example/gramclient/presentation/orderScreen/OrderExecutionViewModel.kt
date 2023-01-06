package com.example.gramclient.presentation.orderScreen

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.gramclient.*
import com.example.gramclient.domain.mainScreen.order.*
import com.example.gramclient.domain.mainScreen.order.connectClientWithDriver.connectClientWithDriverResponse
import com.example.gramclient.domain.orderExecutionScreen.*
import com.example.gramclient.domain.realtimeDatabase.Order.RealtimeDatabaseOrder
import com.example.gramclient.domain.realtimeDatabase.RealtimeDatabaseUseCase
import com.example.gramclient.domain.realtimeDatabase.realtimeDatabaseResponseState
import com.example.gramclient.presentation.components.currentRoute
import com.example.gramclient.presentation.mainScreen.states.CancelOrderResponseState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OrderExecutionViewModel  @Inject constructor(
    private val sendAddRatingUseCase: SendAddRatingUseCase,
    private val getActiveOrdersUseCase: GetActiveOrdersUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val editOrderUseCase: EditOrderUseCase,
    private val realtimeDatabaseUseCase: RealtimeDatabaseUseCase,
    private val connectClientWithDriverUseCase: ConnectClientWithDriverUseCase
): ViewModel() {
    private val _stateAddRating = mutableStateOf(AddRatingResponseState())
    val stateSearchAddress: State<AddRatingResponseState> = _stateAddRating

    private val _stateConnectClientWithDriver = mutableStateOf(ConnectClientWithDriverResponseState())
    val stateConnectClientWithDriver = _stateConnectClientWithDriver
    private val _stateRealtimeDatabase = mutableStateOf(realtimeDatabaseResponseState())
    val stateRealtimeDatabase: State<realtimeDatabaseResponseState> = _stateRealtimeDatabase

    private val _stateActiveOrders = mutableStateOf(ActiveOrdersResponseState())
    val stateActiveOrders: State<ActiveOrdersResponseState> = _stateActiveOrders

    private val _stateCancelOrder = mutableStateOf(CancelOrderResponseState())
    val stateCancelOrder: State<CancelOrderResponseState> = _stateCancelOrder

    private val _stateEditOrder = mutableStateOf(EditOrderResponseState())
    val stateEditOrder: State<EditOrderResponseState> = _stateEditOrder

//    private val _selectedOrder = mutableStateOf(Order(listOf(), "", "", "", null, 1, "", null, "", 1, "", "", "", 1, null))
private val _selectedOrder = mutableStateOf(RealtimeDatabaseOrder())
    val selectedOrder: State<RealtimeDatabaseOrder> = _selectedOrder

    fun updateSelectedOrder(order: RealtimeDatabaseOrder) {
        _selectedOrder.value = order
    }

    fun readAllOrders() {
            realtimeDatabaseUseCase.invoke().onEach { result: Resource<LiveData<List<RealtimeDatabaseOrder>>> ->
                when (result){
                    is Resource.Success -> {
                        try {
                            val addressResponse: LiveData<List<RealtimeDatabaseOrder>>? = result.data

                                _stateRealtimeDatabase.value =
                                    realtimeDatabaseResponseState(response = addressResponse)
                            Log.e("AddRatingResponse", "SendRatingResponse->\n ${_stateAddRating.value}")
                        }catch (e: Exception) {
                            Log.d("Exception", "${e.message} Exception")
                        }
                    }
                    is Resource.Error -> {
                        Log.e("AddRatingResponse", "AddRatingResponseError->\n ${result.message}")
                        _stateRealtimeDatabase.value = realtimeDatabaseResponseState(
                            error = "${result.message}"
                        )
                    }
                    is Resource.Loading -> {
                        _stateRealtimeDatabase.value = realtimeDatabaseResponseState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun sendRating2(token:String,
      order_id: Int,
      add_rating: Int){
        sendAddRatingUseCase.invoke(token="Bearer $token",order_id, add_rating).onEach { result: Resource<AddRatingResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val addressResponse: AddRatingResponse? = result.data
                        _stateAddRating.value =
                            AddRatingResponseState(response = addressResponse?.result)
                        Log.e("AddRatingResponse", "SendRatingResponse->\n ${_stateAddRating.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("AddRatingResponse", "AddRatingResponseError->\n ${result.message}")
                    _stateAddRating.value = AddRatingResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateAddRating.value = AddRatingResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun connectClientWithDriver(
        token:String,
        order_id: String
    ){
        connectClientWithDriverUseCase.invoke(token="Bearer $token",order_id).onEach { result: Resource<connectClientWithDriverResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val connectClientWithDriverResponse: connectClientWithDriverResponse? = result.data
                        _stateConnectClientWithDriver.value =
                            ConnectClientWithDriverResponseState(response = connectClientWithDriverResponse)
                        Log.e("ConnectClientWithDriverResponseSuccess", "ConnectClientWithDriverResponse->\n ${_stateConnectClientWithDriver.value.response}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("ConnectClientWithDriverResponse", "ConnectClientWithDriverResponseError->\n ${result.message}")
                    _stateConnectClientWithDriver.value = ConnectClientWithDriverResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateConnectClientWithDriver.value = ConnectClientWithDriverResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
    fun getActiveOrders(token:String, navController: NavController){
        getActiveOrdersUseCase.invoke(token="Bearer $token").onEach { result: Resource<ActiveOrdersResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: ActiveOrdersResponse? = result.data
                        _stateActiveOrders.value =
                            ActiveOrdersResponseState(response = response?.result)
                        Log.e("ActiveOrdersResponse", "ActiveOrdersResponse->\n ${_stateActiveOrders.value}")
                        currentRoute = navController.currentBackStackEntry?.destination?.route
                        if(navController.currentBackStackEntry?.destination?.route == RoutesName.SPLASH_SCREEN) {
                            if (_stateActiveOrders.value.response!!.isEmpty()) {
                                navController.navigate(RoutesName.SEARCH_ADDRESS_SCREEN) {
                                    popUpTo(RoutesName.SPLASH_SCREEN) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                navController.navigate(RoutesName.SEARCH_DRIVER_SCREEN) {
                                    popUpTo(RoutesName.SPLASH_SCREEN) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("ActiveOrdersResponse", "ActiveOrdersResponseError->\n ${result.message}")
                    _stateActiveOrders.value = ActiveOrdersResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateActiveOrders.value = ActiveOrdersResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
    fun cancelOrder(preferences: SharedPreferences, order_id: Int, navController: NavController,onSuccess:()->Unit){
        cancelOrderUseCase.invoke(token="Bearer ${preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString()}", order_id).onEach { result: Resource<CancelOrderResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: CancelOrderResponse? = result.data
                        _stateCancelOrder.value =
                            CancelOrderResponseState(response = response)
                        onSuccess()
                        getActiveOrders(token = preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString(), navController)
                        Log.e("TariffsResponse", "CancelOrderResponse->\n ${_stateCancelOrder.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("TariffsResponse", "CancelOrderResponseError->\n ${result.message}")
                    _stateCancelOrder.value = CancelOrderResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateCancelOrder.value = CancelOrderResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun editOrder(preferences: SharedPreferences, toAddressId: Int) {

        editOrderUseCase.invoke(
            token="Bearer ${preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString()}",
            order_id = selectedOrder.value.id,
            dop_phone = null,
            from_address = selectedOrder.value.from_address?.id,
            meeting_info = null,
            to_addresses = listOf(AddressModel(toAddressId)),
            comment = null,
            tariff_id = selectedOrder.value.tariff_id?:0,
            allowances= if(selectedOrder.value.allowances!=null) Gson().toJson(selectedOrder.value.allowances) else null
        ).onEach { result: Resource<UpdateOrderResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: UpdateOrderResponse? = result.data
                        _stateEditOrder.value =
                            EditOrderResponseState(response = response)
                        readAllOrders()
                        Log.e("EditOrderResponse", "EditOrderResponse->\n ${_stateEditOrder.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("EditOrderResponse", "EditOrderResponseError->\n ${result.message}")
                    _stateEditOrder.value = EditOrderResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateEditOrder.value = EditOrderResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}