package com.firstbreadclient.network

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import com.firstbreadclient.eventbus.NetworkEvent
import com.firstbreadclient.network.interceptor.AuthorizationInterceptor
import com.firstbreadclient.network.interceptor.NetworkConnectionInterceptor
import com.firstbreadclient.network.interceptor.TokenRenewInterceptor
import com.firstbreadclient.network.listener.AuthenticationListener
import com.firstbreadclient.network.listener.InternetConnectionListener
import com.firstbreadclient.network.security.Session
import com.firstbreadclient.service.LoginService
import com.firstbreadclient.storage.SharedPreferencesManager
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import java.util.Objects
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

object OkHttpClientInstance {
    private var sOkHttpClient: OkHttpClient? = null
    private var mInternetConnectionListener: InternetConnectionListener? = null
    private var mSession: Session? = null
    private var mAuthenticationListener: AuthenticationListener? = null
    private var mService: LoginService? = null

    fun getOkHttpClientInstance(
        sslSocketFactory: SSLSocketFactory?,
        trustManager: X509TrustManager?
    ): OkHttpClient? {
        when (sOkHttpClient) {
            null -> sOkHttpClient = OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory!!, trustManager!!)
                .addInterceptor(object : NetworkConnectionInterceptor() {
                    override val isInternetAvailable: Boolean
                        get() = false

                    override fun onInternetUnavailable() {
                        EventBus.getDefault().post(NetworkEvent("onInternetUnavailable"))
                    }
                })
                .build()
        }
        return sOkHttpClient
    }

    fun getOkHttpClientInstance(): OkHttpClient? {
        sOkHttpClient ?: return OkHttpClient.Builder()
            .addInterceptor(object : NetworkConnectionInterceptor() {
                override val isInternetAvailable: Boolean
                    get() = isInternetAvailable()

                override fun onInternetUnavailable() {
                    mInternetConnectionListener?.onInternetUnavailable()
                }
            })
            .addInterceptor(TokenRenewInterceptor(getSession()!!))
            .addInterceptor(AuthorizationInterceptor(getSession()!!))
            .build()

        return sOkHttpClient
    }

    fun setInternetConnectionListener(listener: InternetConnectionListener?) {
        mInternetConnectionListener = listener
    }

    fun removeInternetConnectionListener() {
        mInternetConnectionListener = null
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = (mInternetConnectionListener as Activity?)!!
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = Objects.requireNonNull(connectivityManager).activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun setAuthenticationListener(service: LoginService?, listener: AuthenticationListener?) {
        mAuthenticationListener = listener
        mService = service
        SharedPreferencesManager.init(mAuthenticationListener as Activity)
    }

    fun getSession(): Session? {
        if (mSession == null) {
            mSession = object : Session {
                override fun getService(): LoginService? {
                    return mService
                }

                override fun isLoggedIn(): Boolean {
                    val isLogged =
                        SharedPreferencesManager.token == ""
                    return !isLogged
                }

                override fun saveToken(token: String?) {
                    SharedPreferencesManager.token = token
                }

                override fun getToken(): String? {
                    return SharedPreferencesManager.token
                }

                override fun saveCntkod(cntkod: String?) {
                    SharedPreferencesManager.cntkod = cntkod
                }

                override fun getCntkod(): String? {
                    return SharedPreferencesManager.cntkod
                }

                override fun savePassword(password: String?) {
                    SharedPreferencesManager.password = password
                }

                override fun getPassword(): String? {
                    return SharedPreferencesManager.password
                }

                override fun getDaysId(): String? {
                    return SharedPreferencesManager.password
                }

                override fun invalidate() {
                    SharedPreferencesManager.clear()
                    if (mAuthenticationListener != null) {
                        mAuthenticationListener!!.onUserLoggedOut()
                    }
                }
            }
        }
        return mSession
    }
}
