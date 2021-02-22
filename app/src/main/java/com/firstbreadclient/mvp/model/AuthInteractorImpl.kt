package com.firstbreadclient.mvp.model

import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.network.LoginRetrofitInstance
import com.firstbreadclient.network.OkHttpClientInstance
import com.firstbreadclient.network.RetrofitInstance
import com.firstbreadclient.network.listener.AuthenticationListener
import com.firstbreadclient.network.listener.InternetConnectionListener
import com.firstbreadclient.network.security.Authorization
import com.firstbreadclient.network.security.Registration
import com.firstbreadclient.service.GetDataService
import com.firstbreadclient.service.LoginService
import io.reactivex.Observable
import retrofit2.Call
import java.util.ArrayList

class AuthInteractorImpl (internetConnectionListener: InternetConnectionListener?,
                                              authenticationListener: AuthenticationListener?) : AuthInteractor {
    var mService: GetDataService? = null
    private var mLoginService: LoginService? = null

    init {
        mService = RetrofitInstance.retrofitInstance?.create(GetDataService::class.java)
        mLoginService = LoginRetrofitInstance.retrofitInstance?.create(LoginService::class.java)

        OkHttpClientInstance.setInternetConnectionListener(internetConnectionListener)
        OkHttpClientInstance.setAuthenticationListener(mLoginService, authenticationListener)
    }

    override fun getAuthData(jwt: String?, tokenAll: String?): Call<ArrayList<Auth?>?>? {
        return mService!!.getAuthData(jwt, tokenAll)
    }

    override fun signin(registration: Registration?): Observable<Authorization?>? {
        return mLoginService!!.signin(registration)
    }
}