package com.gram.client.presentation.screens.order.states

import androidx.lifecycle.LiveData
import com.gram.client.domain.firebase.profile.Client

data class GetClientOrderState(
    var response:LiveData<Client>? = null,
)
