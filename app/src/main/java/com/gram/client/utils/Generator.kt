package com.gram.client.utils

import com.gram.client.domain.mainScreen.Address

fun getAddressText(address: Address): String {
    return if(address.type == "address") "${address.street}, ${address.address}" else address.address
}

fun isTestApp():Boolean{
    return Constants.URL == Constants.TEST_URL
}