package com.example.gramclient.presentation.screens.drawer.orderHistoryScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gramclient.data.remote.ApplicationApi
import com.example.gramclient.domain.orderHistoryScreen.*

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val repository: NewsRepository,

    ): ViewModel() {
    /*private val _stateGetOrderHistory = mutableStateOf(OrderHistoryResponseState())
    val stateGetOrderHistory: State<OrderHistoryResponseState> = _stateGetOrderHistory*/

fun paging(): Flow<PagingData<OrderHistoryResult>> = repository.getPaging().cachedIn(viewModelScope)

    /*fun getOrderHistory(){
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
    }*/

}