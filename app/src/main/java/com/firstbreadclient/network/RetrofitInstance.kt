package com.firstbreadclient.network

import com.firstbreadclient.network.OkHttpClientInstance.getOkHttpClientInstance
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private var sRetrofit: Retrofit? = null

    // FIXME Retrofit BASE_URL
    private const val BASE_URL = "http://10.0.2.2:9090/"
    //private const val BASE_URL = "http://192.168.57.60:9090/"

    val retrofitInstance: Retrofit?
        get() {
            when (sRetrofit) {
                null -> sRetrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(getOkHttpClientInstance())
                        .build()
            }
            return sRetrofit
        }
}