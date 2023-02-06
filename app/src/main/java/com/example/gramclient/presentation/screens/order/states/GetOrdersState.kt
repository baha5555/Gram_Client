package com.example.gramclient.presentation.screens.order.states

import androidx.lifecycle.LiveData
import com.example.gramclient.domain.firebase.order.RealtimeDatabaseOrder

data class GetOrdersState(
    var response: LiveData<List<RealtimeDatabaseOrder>>? = null,
)
