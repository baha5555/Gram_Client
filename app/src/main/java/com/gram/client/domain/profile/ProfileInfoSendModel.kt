package com.gram.client.domain.profile

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class ProfileInfoSendModel(
    val first_name: RequestBody,
    val last_name: RequestBody,
    val email: String?,
    val avatar: MultipartBody.Part?
)
