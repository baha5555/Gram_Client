package com.example.gramclient.domain.realtimeDatabase.Order

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gramclient.domain.orderHistoryScreen.Performer

@Entity(tableName = "orders_table")
data class RealtimeDatabaseOrder(
    @PrimaryKey(autoGenerate = true)
    val userid: Int = 0,
    @ColumnInfo
    var allowances: List<Allowance>? = null,
    @ColumnInfo
    val created_at: String?=null,
    @ColumnInfo
    val from_address: FromAddress?=null,
    @ColumnInfo
    val id: Int=0,
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
    val tariff_id: Int?=null,
    @ColumnInfo
    val to_address: List<ToAddresse>? = null,
    @ColumnInfo
    val performer: RealtimeDatabasePerformer? = null,
    @ColumnInfo
    val filing_time:String?=null
    )