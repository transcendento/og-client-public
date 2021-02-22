package com.firstbreadclient.mvp.model

import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.network.security.Authorization
import com.firstbreadclient.network.security.Registration
import io.reactivex.Observable
import retrofit2.Call
import java.util.ArrayList

interface AuthInteractor {
    fun getAuthData(jwt: String?, tokenAll: String?): Call<ArrayList<Auth?>?>?
    fun signin(registration: Registration?): Observable<Authorization?>?
}