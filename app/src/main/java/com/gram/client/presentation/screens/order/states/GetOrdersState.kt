package com.gram.client.presentation.screens.order.states

import androidx.lifecycle.LiveData
import com.gram.client.domain.firebase.order.RealtimeDatabaseOrder

data class GetOrdersState(
    var response: LiveData<List<RealtimeDatabaseOrder>>? = null,
)
