package com.gram.client.domain.socket

import com.gram.client.domain.orderExecutionScreen.active.AllActiveOrdersResult

data class EditOrderSocketResponse(
    val client_socket_id: String,
    val data: AllActiveOrdersResult
)