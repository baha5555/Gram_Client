package com.gram.client.domain.orderExecutionScreen

import com.gram.client.domain.mainScreen.order.connectClientWithDriver.connectClientWithDriverResponse

class ConnectClientWithDriverResponseState(
    val isLoading: Boolean = false,
    var response: connectClientWithDriverResponse? = null,
    val error: String = ""
)
