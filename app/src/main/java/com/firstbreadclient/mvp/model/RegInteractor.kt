package com.firstbreadclient.mvp.model

import com.firstbreadclient.network.security.Registration
import okhttp3.ResponseBody
import retrofit2.Call

interface RegInteractor {
    fun signup(registration: Registration): Call<ResponseBody>?
}