package com.example.gramclient.presentation.screens.drawer.orderHistoryScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.gramclient.domain.mainScreen.order.UpdateOrderResponse
import com.example.gramclient.domain.orderExecutionScreen.EditOrderResponseState
import com.example.gramclient.domain.orderHistory.Data
import com.example.gramclient.domain.orderHistoryScreen.*
import com.example.gramclient.utils.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderHistoryUseCase: OrderHistoryUseCase,
    ): ViewModel() {
    private val _stateOrderHistoryPagingItems = mutableStateOf(OrderHistoryPagingResponseState())
    val stateOrderHistoryPagingItems = _stateOrderHistoryPagingItems
    fun response() = orderHistoryUseCase.response()
    fun paging() {
        orderHistoryUseCase.invoke().onEach { result: Resource<Pager<Int,Data>> ->
            when (result) {
                is Resource.Success -> {
                    try {
                            val response = result.data
                            _stateOrderHistoryPagingItems.value =
                                OrderHistoryPagingResponseState(response = response)
                        Log.e("SUCCESS","${_stateOrderHistoryPagingItems.value}")
                    } catch (e:Exception){

                    }
                }
                is Resource.Error -> {
                    Log.e("OrderHistory", "OrderHistoryResponseError->\n ${result.message}")
                    _stateOrderHistoryPagingItems.value = OrderHistoryPagingResponseState(
                        error = "${result.message}"
                    )
                }
                is Resource.Loading -> {
                    _stateOrderHistoryPagingItems.value = OrderHistoryPagingResponseState(isLoading = true)
                }
            }
        }
    }
}