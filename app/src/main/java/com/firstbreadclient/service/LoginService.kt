package com.firstbreadclient.service

import com.firstbreadclient.network.security.Authorization
import com.firstbreadclient.network.security.Registration
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("api/auth/signin")
    fun signin(@Body registration: Registration?): Observable<Authorization?>?
}