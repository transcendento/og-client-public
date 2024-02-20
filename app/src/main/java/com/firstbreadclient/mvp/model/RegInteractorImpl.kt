package com.firstbreadclient.mvp.model

import com.firstbreadclient.network.LoginRetrofitInstance
import com.firstbreadclient.network.OkHttpClientInstance
import com.firstbreadclient.network.listener.AuthenticationListener
import com.firstbreadclient.network.listener.InternetConnectionListener
import com.firstbreadclient.network.security.Registration
import com.firstbreadclient.service.LoginService
import okhttp3.ResponseBody
import retrofit2.Call

class RegInteractorImpl (internetConnectionListener: InternetConnectionListener?,
                         authenticationListener: AuthenticationListener?) : RegInteractor {
    private var mLoginService: LoginService? = null

    init {
        mLoginService = LoginRetrofitInstance.retrofitInstance?.create(LoginService::class.java)

        OkHttpClientInstance.setInternetConnectionListener(internetConnectionListener)
        OkHttpClientInstance.setAuthenticationListener(mLoginService, authenticationListener)
    }
    override fun signup(registration: Registration): Call<ResponseBody>? {
        return mLoginService?.signup(registration)
    }

}