package com.gram.client.domain.mainScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.gram.client.domain.mainScreen.order.AllowanceRequest

data class Allowance(
    val allowance_id: Int,
    val name: String,
    val price: Double,
    val price_property: ArrayList<Int>?,
    val tariff: String,
    val tariff_id: Int,
    val type: String,
    val is_fix_price: Boolean
)

data class ToDesiredAllowance(
    val id: Int,
    val name: String,
    val price: Double,
    val price_property: ArrayList<Int>?,
    var isSelected: MutableState<Boolean>,
    val type: String,
    val is_fix_price: Boolean
)

fun Allowance.toDesiredAllowance(): ToDesiredAllowance = ToDesiredAllowance(
    id= allowance_id,
    name = name,
    price = price,
    price_property = price_property,
    isSelected = mutableStateOf(false),
    type = type,
    is_fix_price = is_fix_price
)

fun ToDesiredAllowance.toAllowanceRequest(price: Int? = null): AllowanceRequest = AllowanceRequest(
    allowance_id = id,
    value = price ?: if(is_fix_price) 1 else 0
)