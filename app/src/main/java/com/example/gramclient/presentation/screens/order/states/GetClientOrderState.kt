package com.example.gramclient.presentation.screens.order.states

import androidx.lifecycle.LiveData
import com.example.gramclient.domain.firebase.profile.Client

data class GetClientOrderState(
    var response:LiveData<Client>? = null,
)
