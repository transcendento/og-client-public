package com.firstbreadclient.network.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NetworkResult {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}