package com.gram.client.domain.database.models

data class StoriesModel(
    val id: Int,
    val id_group: Int?,
    val title: String?,
    val text: String?,
    val src: String?,
)
