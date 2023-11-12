package com.firstbreadclient.service

import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.model.data.Order
import com.firstbreadclient.model.data.Prod
import com.firstbreadclient.network.security.Authorization
import com.firstbreadclient.network.security.Registration
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface GetDataService {
    @GET("api/ogs/{tokenall}/auth")
    fun getAuthData(@Header("Authorization") jwt: String?, @Path("tokenall") tokenAll: String?): Call<ArrayList<Auth?>?>?

    @GET("api/ogs/{cntkod}/ord")
    fun getOrderData(@Header("Authorization") jwt: String?, @Path("cntkod") cntKod: String?): Call<ArrayList<Order?>?>?

    @GET("api/ogs/{cntkod}/ord")
    fun getOrderDataRx(@Header("Authorization") jwt: String?, @Path("cntkod") cntKod: String?): Observable<ArrayList<Order?>?>?

    @GET("api/ogs/{id}/cnt")
    fun getProdData(@Header("Authorization") jwt: String?, @Path("id") daysId: String?): Call<ArrayList<Prod?>?>?

    @POST("api/auth/signin")
    fun loginAccount(@Body registration: Registration?): Call<Authorization?>?

    @PUT("api/ogs/prod")
    fun putProd(@Header("Authorization") jwt: String?, @Body prod: List<Prod>): Call<ResponseBody>

    @DELETE("api/ogs/prod/del")
    fun delProd(@Header("Authorization") jwt: String?): Call<ResponseBody>
}