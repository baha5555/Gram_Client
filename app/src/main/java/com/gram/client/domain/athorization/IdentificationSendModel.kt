package com.gram.client.domain.athorization

data class IdentificationSendModel(
    val client_register_id: String,
    val sms_code: String,
    val fcm_token: String
)
