package com.gram.client.presentation.screens.drawer.orderHistoryScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gram.client.domain.orderHistory.Data
import com.gram.client.domain.orderHistoryScreen.*

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

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



