package com.example.gramclient.presentation.screens.drawer.orderHistoryScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gramclient.domain.AppRepository
import com.example.gramclient.domain.orderHistory.Character
import com.example.gramclient.domain.orderHistory.Result
import com.example.gramclient.domain.orderHistory.getOrderHistoryCharacterUseCase
import com.example.gramclient.utils.Resource
import com.example.gramclient.domain.orderHistoryScreen.OrderHistoryResponseState
import com.example.gramclient.domain.orderHistoryScreen.OrderHistoryUseCase
import com.example.gramclient.domain.orderHistoryScreen.orderHistoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val getOrderHistoryUseCase: OrderHistoryUseCase,
    private val getOrderHistoryCharacterUseCase: getOrderHistoryCharacterUseCase
): ViewModel() {
    private val _stateGetOrderHistory = mutableStateOf(OrderHistoryResponseState())
    val stateGetOrderHistory: State<OrderHistoryResponseState> = _stateGetOrderHistory
    val character:Flow<PagingData<Result>> = Pager(PagingConfig(pageSize = 1)){

         getOrderHistoryCharacterUseCase

    }.flow.cachedIn(viewModelScope)
    fun getOrderHistory(){
        getOrderHistoryUseCase.invoke().onEach { result: Resource<orderHistoryResponse> ->
            when (result){
                is Resource.Success -> {
                    try {
                        val tariffsResponse: orderHistoryResponse? = result.data
                        _stateGetOrderHistory.value =
                            OrderHistoryResponseState(response = tariffsResponse?.result)
                        Log.e("OrderHistoryResponse", "OrderHistoryResponseSuccess->\n ${_stateGetOrderHistory.value}")
                    }catch (e: Exception) {
                        Log.d("Exception", "${e.message} Exception")
                    }
                }
                is Resource.Error -> {
                    Log.e("OrderHistoryResponse", "OrderHistoryResponseError->\n ${result.message}")
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