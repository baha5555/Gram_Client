package com.gram.client.domain.firebase.order

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gram.client.domain.mainScreen.Address

@Entity(tableName = "orders_table")
data class RealtimeDatabaseOrder(
    @PrimaryKey(autoGenerate = true)
    val userid: Int = 0,
    @ColumnInfo
    var allowances: List<Allowance>? = null,
    @ColumnInfo
    val create_order: String?=null,
    @ColumnInfo
    val from_address: Address?=null,
    @ColumnInfo
    val id: Int=-1,
    @ColumnInfo
    val phone: String?=null,
    @ColumnInfo
    val price: Int?=null,
    @ColumnInfo
    val search_address_id: Int?=null,
    @ColumnInfo
    val status: String?=null,
    @ColumnInfo
    val tariff: String?=null,
    @ColumnInfo
    val meeting_info: String?=null,
    @ColumnInfo
    val tariff_id: Int?=null,
    @ColumnInfo
    val to_address: List<Address>? = null,
    @ColumnInfo
    val performer: RealtimeDatabasePerformer? = null,
    @ColumnInfo
    val filing_time:String?=null
    )