package com.example.gramclient.domain.orderHistoryScreen

import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.gramclient.domain.mainScreen.order.UpdateOrderResponse
import com.example.gramclient.domain.orderHistory.Data
import com.example.gramclient.domain.orderHistory.OrderHistoryPagingResult
import kotlinx.coroutines.flow.Flow

data class OrderHistoryPagingResponseState(
    val isLoading: Boolean = false,
    var response: Pager<Int,Data>? = null,
    val error: String = ""
)