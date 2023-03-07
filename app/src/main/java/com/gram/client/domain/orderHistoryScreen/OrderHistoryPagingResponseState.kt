package com.gram.client.domain.orderHistoryScreen

import androidx.paging.Pager
import com.gram.client.domain.orderHistory.Data

data class OrderHistoryPagingResponseState(
    val isLoading: Boolean = false,
    var response: Pager<Int,Data>? = null,
    val error: String = ""
)