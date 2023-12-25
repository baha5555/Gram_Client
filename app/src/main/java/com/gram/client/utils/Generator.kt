package com.gram.client.utils

import com.gram.client.domain.mainScreen.Address

fun getAddressText(address: Address): String {
    return if(address.id==0) ""
    else if(address.type == "address") "${address.street}, ${address.name}" else address.name
}

fun isTestApp():Boolean{
    return Constants.URL == Constants.TEST_URL
}