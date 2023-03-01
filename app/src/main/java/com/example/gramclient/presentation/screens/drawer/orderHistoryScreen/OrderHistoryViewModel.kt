package com.example.gramclient.presentation.screens.drawer.orderHistoryScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gramclient.domain.firebase.order.RealtimeDatabaseOrder
import com.example.gramclient.domain.mainScreen.order.UpdateOrderResponse
import com.example.gramclient.domain.orderExecutionScreen.EditOrderResponseState
import com.example.gramclient.domain.orderHistory.Data
import com.example.gramclient.domain.orderHistory.Pagination
import com.example.gramclient.domain.orderHistoryScreen.*
import com.example.gramclient.utils.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderHistoryUseCase: OrderHistoryUseCase,
) : ViewModel() {
    private val _selectedOrder = mutableStateOf(Data())
    val selectedOrder: State<Data> = _selectedOrder
    private val _stateOrderHistoryPagingItems = mutableStateOf(OrderHistoryPagingResponseState())
    val stateOrderHistoryPagingItems = _stateOrderHistoryPagingItems
    fun response() = orderHistoryUseCase.response()
    fun paging(pageSize: Int): Flow<PagingData<Data>> {
        return try {
            orderHistoryUseCase.invoke(pageSize)
                .cachedIn(viewModelScope)
        }
        catch (e: Exception) {
            // Обработка ошибки, например, запись в лог или отображение сообщения пользователю
            throw Exception("Ошибка при загрузке данных: ${e.message}")
        }
    }
    fun updateSelectedOrder(order: Data) {
        _selectedOrder.value = order
    }

}



