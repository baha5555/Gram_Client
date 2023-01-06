package com.example.gramclient.domain.orderExecutionScreen

import com.example.gramclient.domain.mainScreen.order.connectClientWithDriver.connectClientWithDriverResponse

class ConnectClientWithDriverResponseState(
    val isLoading: Boolean = false,
    var response: connectClientWithDriverResponse? = null,
    val error: String = ""
)
