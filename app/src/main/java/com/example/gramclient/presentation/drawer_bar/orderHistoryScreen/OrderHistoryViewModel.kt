package com.example.gramclient.presentation.drawer_bar.orderHistoryScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramclient.Resource
import com.example.gramclient.domain.orderHistoryScreen.OrderHistoryResponseState
import com.example.gramclient.domain.orderHistoryScreen.OrderHistoryUseCase
import com.example.gramclient.domain.orderHistoryScreen.orderHistoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val getOrderHistoryUseCase: OrderHistoryUseCase
): ViewModel() {
    private val _stateGetOrderHistory = mutableStateOf(OrderHistoryResponseState())
    val stateGetOrderHistory: State<OrderHistoryResponseState> = _stateGetOrderHistory
    fun getOrderHistory(token:String){
        getOrderHistoryUseCase.invoke(token="Bearer $token").onEach { result: Resource<orderHistoryResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val tariffsResponse: orderHistoryResponse? = result.data
                        _stateGetOrderHistory.value =
                            OrderHistoryResponseState(response = tariffsResponse?.result)
                        Log.e("GetProfileResponse", "GetProfileResponseSuccess->\n ${_stateGetOrderHistory.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("GetProfileResponse", "GetProfileResponseError->\n ${result.message}")
                    _stateGetOrderHistory.value = OrderHistoryResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateGetOrderHistory.value = OrderHistoryResponseState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}