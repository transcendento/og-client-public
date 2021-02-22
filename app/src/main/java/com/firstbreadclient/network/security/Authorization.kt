package com.firstbreadclient.network.security

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Authorization {
    @SerializedName("accessToken")
    @Expose
    var accessToken: String? = null

    @SerializedName("tokenType")
    @Expose
    var tokenType: String? = null
}