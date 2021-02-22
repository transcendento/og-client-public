package com.firstbreadclient.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * A basic class representing an entity that is a row in a one-column database table.
 *
 * @ Entity - You must annotate the class as an entity and supply a table name if not class name.
 * @ PrimaryKey - You must identify the primary key.
 * @ ColumnInfo - You must supply the column name if it is different from the variable name.
 *
 * See the documentation for the full rich set of annotations.
 * https://developer.android.com/topic/libraries/architecture/room.html
 */
@Entity(tableName = "order_table")
data class Order(@field:SerializedName("daysordermoveid") @field:ColumnInfo(name = "daysordermoveid") @field:PrimaryKey var daysordermoveid: String,
            @field:SerializedName("cntkod") @field:ColumnInfo(name = "cntkod") var cntkod: String,
            @field:SerializedName("cntnamestr") @field:ColumnInfo(name = "cntnamestr") var cntnamestr: String,
            @field:SerializedName("daysorderdate") @field:ColumnInfo(name = "daysorderdate") var daysorderdate: String)