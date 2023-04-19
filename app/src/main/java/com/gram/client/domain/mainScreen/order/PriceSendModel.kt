package com.gram.client.domain.mainScreen.order

data class PriceSendModel(
   val tariff_ids: String,
   val  allowances: String?,
   val value_allowances: String?,
   val search_address_id: Int?,
   val to_addresses: String?,
   val from_addresses: String?
)
