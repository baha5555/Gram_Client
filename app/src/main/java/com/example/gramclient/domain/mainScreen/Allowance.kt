package com.example.gramclient.domain.mainScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.gramclient.domain.mainScreen.order.AllowanceRequest

data class Allowance(
    val allowance_id: Int,
    val name: String,
    val price: Int,
    val tariff: String,
    val tariff_id: Int
)

data class ToDesiredAllowance(
    val id: Int,
    val name: String,
    val price: Int,
    var isSelected: MutableState<Boolean>
)

fun Allowance.toDesiredAllowance(): ToDesiredAllowance = ToDesiredAllowance(
    id= allowance_id,
    name = name,
    price = price,
    isSelected = mutableStateOf(false)
)

fun ToDesiredAllowance.toAllowanceRequest(): AllowanceRequest = AllowanceRequest(
    allowance_id = id
)