package com.firstbreadclient.network.security

import com.google.gson.annotations.SerializedName

data class Registration(
    @field:SerializedName("cntkod") var cntkod: String?,
    @field:SerializedName("pass") var password: String?
)