package com.firstbreadclient.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "prod_table")
data class Prod(@field:SerializedName("daysordermovecontentid") @field:ColumnInfo(name = "daysordermovecontentid") @field:PrimaryKey var daysordermovecontentid: String,
           @field:SerializedName("daysordermoveidstr") @field:ColumnInfo(name = "daysordermoveidstr") var daysordermoveidstr: String,
           @field:SerializedName("prodlongname") @field:ColumnInfo(name = "prodlongname") var prodlongname: String,
           @field:SerializedName("amountstr") @field:ColumnInfo(name = "amountstr") var amountstr: String,
           @field:SerializedName("flagacceptstr") @field:ColumnInfo(name = "flagacceptstr") var flagacceptstr: String)