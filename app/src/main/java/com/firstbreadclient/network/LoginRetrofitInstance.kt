package com.firstbreadclient.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object LoginRetrofitInstance {
    private var sRetrofit: Retrofit? = null

    // FIXME LoginRetrofit BASE_URL
    private const val BASE_URL = "http://192.168.57.60:9090/"
    //private const val BASE_URL = "http://10.0.2.2:9090/"

    val retrofitInstance: Retrofit?
        get() {
            if (sRetrofit == null) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                sRetrofit = Retrofit.Builder()
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .baseUrl(BASE_URL)
                        .client(OkHttpClientInstance.getOkHttpClientInstance())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return sRetrofit
        }
}