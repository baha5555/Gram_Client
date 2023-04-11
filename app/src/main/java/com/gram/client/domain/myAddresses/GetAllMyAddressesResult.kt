package com.gram.client.domain.myAddresses

data class GetAllMyAddressesResult(
    val home: List<Home>,
    val other: List<Other>,
    val work: List<Work>
)