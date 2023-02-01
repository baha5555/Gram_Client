package com.example.gramclient.presentation.screens.order

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.gramclient.domain.mainScreen.order.*
import com.example.gramclient.domain.mainScreen.order.connectClientWithDriver.connectClientWithDriverResponse
import com.example.gramclient.domain.orderExecutionScreen.*
import com.example.gramclient.domain.realtimeDatabase.Order.RealtimeDatabaseOrder
import com.example.gramclient.domain.realtimeDatabase.RealtimeClientDatabaseUseCase
import com.example.gramclient.domain.realtimeDatabase.RealtimeDatabaseUseCase
import com.example.gramclient.domain.realtimeDatabase.profile.Client
import com.example.gramclient.domain.realtimeDatabase.realtimeClientOrderIdDatabaseResponseState
import com.example.gramclient.domain.realtimeDatabase.realtimeDatabaseResponseState
import com.example.gramclient.presentation.components.currentRoute
import com.example.gramclient.presentation.screens.main.states.CancelOrderResponseState
import com.example.gramclient.utils.Resource
import com.example.gramclient.utils.RoutesName
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
    private val realtimeClientDatabaseUseCase: RealtimeClientDatabaseUseCase,
    private val connectClientWithDriverUseCase: ConnectClientWithDriverUseCase
): ViewModel() {
    private val _stateAddRating = mutableStateOf(AddRatingResponseState())
    val stateSearchAddress: State<AddRatingResponseState> = _stateAddRating

    private val _stateConnectClientWithDriver = mutableStateOf(ConnectClientWithDriverResponseState())
    val stateConnectClientWithDriver = _stateConnectClientWithDriver
    private val _stateRealtimeOrdersDatabase = mutableStateOf(realtimeDatabaseResponseState())
    val stateRealtimeOrdersDatabase: State<realtimeDatabaseResponseState> = _stateRealtimeOrdersDatabase

    private val _stateRealtimeClientOrderIdDatabase = mutableStateOf(realtimeClientOrderIdDatabaseResponseState())
    val stateRealtimeClientOrderIdDatabase: State<realtimeClientOrderIdDatabaseResponseState> = _stateRealtimeClientOrderIdDatabase

    private val _stateActiveOrders = mutableStateOf(ActiveOrdersResponseState())
    val stateActiveOrders: State<ActiveOrdersResponseState> = _stateActiveOrders

    private val _stateCancelOrder = mutableStateOf(CancelOrderResponseState())
    val stateCancelOrder: State<CancelOrderResponseState> = _stateCancelOrder

    private val _stateEditOrder = mutableStateOf(EditOrderResponseState())
    val stateEditOrder: State<EditOrderResponseState> = _stateEditOrder

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

                                _stateRealtimeOrdersDatabase.value =
                                    realtimeDatabaseResponseState(response = addressResponse)
                            Log.e("readAllOrdersFromRealtimeResponse", "Success->\n ${_stateRealtimeOrdersDatabase.value.response?.value}")
                        }catch (e: Exception) {
                            Log.d("readAllOrdersFromRealtimeResponse", "${e.message} Exception")
                        }
                    }
                    is Resource.Error -> {
                        Log.e("readAllOrdersFromRealtimeResponse", "readAllOrdersFromRealtimeResponseError->\n ${result.message}")
                        _stateRealtimeOrdersDatabase.value = realtimeDatabaseResponseState(
                            error = "${result.message}"
                        )
                    }
                    is Resource.Loading -> {
                        _stateRealtimeOrdersDatabase.value = realtimeDatabaseResponseState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
    }
    fun readAllClient(client:String) {
        realtimeClientDatabaseUseCase.invoke(client).onEach { result: Resource<LiveData<Client>> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val realtimeClientOrderIdDatabaseResponseResponse: LiveData<Client>? = result.data

                        _stateRealtimeClientOrderIdDatabase.value =
                            realtimeClientOrderIdDatabaseResponseState(response = realtimeClientOrderIdDatabaseResponseResponse)

                        Log.e("RealtimeClientOrderIdDatabaseResponse", "Success->\n ${_stateRealtimeClientOrderIdDatabase.value.response?.value }")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("RealtimeClientOrderIdDatabaseResponse", "RealtimeClientOrderIdDatabaseResponseError->\n ${result.message}")
                    _stateRealtimeClientOrderIdDatabase.value = realtimeClientOrderIdDatabaseResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateRealtimeClientOrderIdDatabase.value = realtimeClientOrderIdDatabaseResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun sendRating2(
      order_id: Int,
      add_rating: Int){
        sendAddRatingUseCase.invoke(order_id, add_rating).onEach { result: Resource<AddRatingResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val addressResponse: AddRatingResponse? = result.data
                        _stateAddRating.value =
                            AddRatingResponseState(response = addressResponse?.result)
                        Log.e("AddRatingResponse", "AddRatingResponseSuccess->\n ${_stateAddRating.value}")
                    }catch (e: Exception) {
                        Log.d("AddRatingResponse", "${e.message} Exception")
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
        order_id: String,
        onSuccess: () -> Unit
    ){
        connectClientWithDriverUseCase.invoke(order_id).onEach { result: Resource<connectClientWithDriverResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val connectClientWithDriverResponse: connectClientWithDriverResponse? = result.data
                        _stateConnectClientWithDriver.value =
                            ConnectClientWithDriverResponseState(response = connectClientWithDriverResponse)
                        onSuccess()
                        Log.e("ConnectClientWithDriverResponseSuccess", "ConnectClientWithDriverResponseSuccess->\n ${_stateConnectClientWithDriver.value.response}")
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
    fun getActiveOrders(){
        getActiveOrdersUseCase.invoke().onEach { result: Resource<ActiveOrdersResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: ActiveOrdersResponse? = result.data
                        _stateActiveOrders.value =
                            ActiveOrdersResponseState(response = response?.result, success = true)
                        Log.e("ActiveOrdersResponse", "ActiveOrdersResponseSuccess->\n ${_stateActiveOrders.value}")
                    }catch (e: Exception) {
                        Log.d("ActiveOrdersResponse", "${e.message} Exception")
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
    fun cancelOrder(order_id: Int, onSuccess:()->Unit){
        cancelOrderUseCase.invoke(order_id).onEach { result: Resource<CancelOrderResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: CancelOrderResponse? = result.data
                        _stateCancelOrder.value =
                            CancelOrderResponseState(response = response)
                        onSuccess()
                        getActiveOrders()
                        Log.e("CancelOrderResponse", "CancelOrderResponseSuccess->\n ${_stateCancelOrder.value}")
                    }catch (e: Exception) {
                        Log.d("CancelOrderResponse", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("CancelOrderResponse", "CancelOrderResponseError->\n ${result.message}")
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

    fun editOrder(toAddressId: Int) {

        editOrderUseCase.invoke(
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
                        Log.e("EditOrderResponse", "EditOrderResponseSuccess->\n ${_stateEditOrder.value}")
                    }catch (e: Exception) {
                        Log.d("EditOrderResponse", "${e.message} Exception")
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