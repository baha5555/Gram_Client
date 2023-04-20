package com.gram.client.domain.athorization

import retrofit2.http.Field

data class IdentificationRequest(
    @Field("client_register_id") var client_register_id: String,
    @Field("sms_code") var sms_code: String,
    @Field("fcm_token") var fcm_token:String
)
