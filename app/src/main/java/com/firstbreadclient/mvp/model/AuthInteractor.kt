package com.firstbreadclient.mvp.model

import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.model.data.Prod
import com.firstbreadclient.network.security.Authorization
import com.firstbreadclient.network.security.Registration
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

interface AuthInteractor {
    fun getAuthData(jwt: String?, tokenAll: String?): Call<ArrayList<Auth?>?>?
    fun signin(registration: Registration?): Observable<Authorization?>?
    fun putProd(jwt: String?, prod: List<Prod>): Call<ResponseBody>?
    fun delProd(jwt: String?): Call<ResponseBody>?
}