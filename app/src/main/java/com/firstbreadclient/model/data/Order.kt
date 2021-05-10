package com.firstbreadclient.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "order_table")
data class Order(@field:SerializedName("daysordermoveid") @field:ColumnInfo(name = "daysordermoveid") @field:PrimaryKey var daysordermoveid: String,
            @field:SerializedName("cntkod") @field:ColumnInfo(name = "cntkod") var cntkod: String,
            @field:SerializedName("cntnamestr") @field:ColumnInfo(name = "cntnamestr") var cntnamestr: String,
            @field:SerializedName("daysorderdate") @field:ColumnInfo(name = "daysorderdate") var daysorderdate: String)