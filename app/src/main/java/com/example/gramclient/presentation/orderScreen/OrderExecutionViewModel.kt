package com.example.gramclient.presentation.orderScreen

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramclient.PreferencesName
import com.example.gramclient.Resource
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.domain.mainScreen.order.CancelOrderResponse
import com.example.gramclient.domain.mainScreen.order.CancelOrderUseCase
import com.example.gramclient.domain.orderExecutionScreen.*
import com.example.gramclient.presentation.mainScreen.states.CancelOrderResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OrderExecutionViewModel  @Inject constructor(
    private val sendAddRatingUseCase: SendAddRatingUseCase,
    private val getActiveOrdersUseCase: GetActiveOrdersUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase
): ViewModel() {

    private val _stateAddRating = mutableStateOf(AddRatingResponseState())
    val stateSearchAddress: State<AddRatingResponseState> = _stateAddRating

    private val _stateActiveOrders = mutableStateOf(ActiveOrdersResponseState())
    val stateActiveOrders: State<ActiveOrdersResponseState> = _stateActiveOrders

    private val _stateCancelOrder = mutableStateOf(CancelOrderResponseState())
    val stateCancelOrder: State<CancelOrderResponseState> = _stateCancelOrder

    private val _selectedOrder = mutableStateOf(Order(listOf(), "", "", "", null, 1, "", null, "", 1, "", "", "", 1, null))
    val selectedOrder: State<Order> = _selectedOrder

    fun updateSelectedOrder(order: Order) {
        _selectedOrder.value = order
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

    fun getActiveOrders(token:String){
        getActiveOrdersUseCase.invoke(token="Bearer $token").onEach { result: Resource<ActiveOrdersResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: ActiveOrdersResponse? = result.data
                        _stateActiveOrders.value =
                            ActiveOrdersResponseState(response = response?.result)
                        Log.e("ActiveOrdersResponse", "ActiveOrdersResponse->\n ${_stateActiveOrders.value}")
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
    fun cancelOrder(preferences: SharedPreferences, order_id: Int){
        cancelOrderUseCase.invoke(token="Bearer ${preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString()}", order_id).onEach { result: Resource<CancelOrderResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val response: CancelOrderResponse? = result.data
                        _stateCancelOrder.value =
                            CancelOrderResponseState(response = response)
                        getActiveOrders(token = preferences.getString(PreferencesName.ACCESS_TOKEN, "").toString())
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
}