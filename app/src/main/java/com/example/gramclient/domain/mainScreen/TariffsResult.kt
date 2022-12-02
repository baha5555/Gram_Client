package com.example.gramclient.domain.mainScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class TariffsResult(
    val id: Int,
    val name: String,
    val min_price: Int
)

data class ToTariffsResult(
    val id: Int,
    val name: String
)

fun TariffsResult.toTariffsResult(): ToTariffsResult = ToTariffsResult(
    id= id,
    name = name
)