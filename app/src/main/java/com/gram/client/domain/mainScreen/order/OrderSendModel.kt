package com.gram.client.domain.mainScreen.order

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.gram.client.domain.mainScreen.Address

data class OrderSendModel(
   val dop_phone: String?,
   val from_address: Int?,
   val to_addresses: SnapshotStateList<Address>?,
   val comment: String?,
   val tariff_id: Int,
   val allowances: String?,
   val date_time: String?,
   val from_address_point: String?,
   val meeting_info: String?,
)
