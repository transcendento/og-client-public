package com.firstbreadclient.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "auth_table")
data class Auth(@field:SerializedName("cntid") @field:ColumnInfo(name = "cntid") @field:PrimaryKey var cntid: String,
                @field:SerializedName("cntkod") @field:ColumnInfo(name = "cntkod") var cntkod: String,
                @field:SerializedName("cntname") @field:ColumnInfo(name = "cntname") var cntname: String,
                @field:SerializedName("cntadres") @field:ColumnInfo(name = "cntadres") var cntadres: String,
                @field:SerializedName("tokenall") @field:ColumnInfo(name = "tokenall") var tokenall: String) {
/*
    @ColumnInfo(name = "cntadres")
    @SerializedName("cntadres")
    var cntadres: String? = null
*/
}