package com.firstbreadclient.network

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import com.firstbreadclient.eventbus.NetworkEvent
import com.firstbreadclient.network.interceptor.AuthorizationInterceptor
import com.firstbreadclient.network.interceptor.NetworkConnectionInterceptor
import com.firstbreadclient.network.interceptor.TokenRenewInterceptor
import com.firstbreadclient.network.listener.AuthenticationListener
import com.firstbreadclient.network.listener.InternetConnectionListener
import com.firstbreadclient.network.security.Session
import com.firstbreadclient.service.LoginService
import com.firstbreadclient.storage.SharedPrefManager
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import java.util.*
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

object OkHttpClientInstance {
    private var sOkHttpClient: OkHttpClient? = null
    private var mInternetConnectionListener: InternetConnectionListener? = null
    private var mSession: Session? = null
    private var mAuthenticationListener: AuthenticationListener? = null
    private var mService: LoginService? = null

    @JvmStatic
    fun getOkHttpClientInstance(sslSocketFactory: SSLSocketFactory?, trustManager: X509TrustManager?): OkHttpClient? {
        if (sOkHttpClient == null) {
            sOkHttpClient = OkHttpClient.Builder()
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

    @JvmStatic
    fun getOkHttpClientInstance(): OkHttpClient? {
        if (sOkHttpClient == null) {
            sOkHttpClient = OkHttpClient.Builder()
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
        }
        return sOkHttpClient
    }

    fun setInternetConnectionListener(listener: InternetConnectionListener?) {
        mInternetConnectionListener = listener
    }

    fun removeInternetConnectionListener() {
        mInternetConnectionListener = null
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = (mInternetConnectionListener as AppCompatActivity?)!!
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = Objects.requireNonNull(connectivityManager).activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun setAuthenticationListener(service: LoginService?, listener: AuthenticationListener?) {
        mAuthenticationListener = listener
        mService = service
    }

    // use a storage option to store the
    // credentails and user info
    // i.e: SQLite, SharedPreference etc.
    fun getSession(): Session? {
        if (mSession == null) {
            mSession = object : Session {
                override fun getService(): LoginService? {
                    return mService
                }

                override fun isLoggedIn(): Boolean {
                    // check if token exist or not
                    // return true if exist otherwise false
                    // assuming that token exists
                    val isLogged = SharedPrefManager.getInstance(mAuthenticationListener as AppCompatActivity)?.token == ""
                    return !isLogged
                }

                override fun saveToken(token: String?) {
                    SharedPrefManager.getInstance(mAuthenticationListener as AppCompatActivity)?.saveToken(token)
                }

                override fun getToken(): String? {
                    return SharedPrefManager.getInstance(mAuthenticationListener as AppCompatActivity)?.token
                }

                override fun saveCntkod(cntkod: String?) {
                    SharedPrefManager.getInstance(mAuthenticationListener as AppCompatActivity)?.saveCntkod(cntkod)
                }

                override fun getCntkod(): String? {
                    return SharedPrefManager.getInstance(mAuthenticationListener as AppCompatActivity)?.cntkod
                }

                override fun savePassword(password: String?) {
                    SharedPrefManager.getInstance(mAuthenticationListener as AppCompatActivity)?.savePassword(password)
                }

                override fun getPassword(): String? {
                    return SharedPrefManager.getInstance(mAuthenticationListener as AppCompatActivity)?.password
                }

                override fun invalidate() {
                    // get called when user become logged out
                    // delete token and other user info
                    // (i.e: email, password)
                    // from the storage

                    // sending logged out event to it's listener
                    // i.e: Activity, Fragment, Service
                    if (mAuthenticationListener != null) {
                        mAuthenticationListener!!.onUserLoggedOut()
                    }
                }
            }
        }
        return mSession
    }
}