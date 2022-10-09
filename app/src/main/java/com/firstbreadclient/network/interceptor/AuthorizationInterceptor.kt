package com.firstbreadclient.network.interceptor

import android.util.Base64
import com.firstbreadclient.network.OkHttpClientInstance
import com.firstbreadclient.network.result.LoginResult
import com.firstbreadclient.network.result.UIEvent
import com.firstbreadclient.network.security.Authorization
import com.firstbreadclient.network.security.Registration
import com.firstbreadclient.network.security.Session
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthorizationInterceptor(private val mSession: Session) : Interceptor {
    private var mResponse: Response? = null
    private var mRequest: Request? = null
    private var mMethod: String? = null

    private val loginCompositeDisposable = CompositeDisposable()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        mResponse = chain.proceed(chain.request())
        mRequest = chain.request()
        mMethod = mRequest!!.method

        if (mSession.isLoggedIn() && mSession.getService() != null && mRequest!!.method != "POST") {
            if (mResponse!!.code == 401 || mResponse!!.code == 403) {
                val userNameObservable = Observable.just(mSession.getCntkod())
                val passwordObservable = Observable.just(mSession.getPassword())

                val uiResultObservable = Observable.combineLatest(
                    userNameObservable,
                    passwordObservable
                ) { cntkod: String?, password: String? -> UIEvent(cntkod, password) }

                val loginObservable = uiResultObservable
                    .flatMap { log: UIEvent ->
                        mSession.getService()!!.signin(Registration(log.cntkod, log.password))!!
                            .subscribeOn(Schedulers.io())
                            .map { n: Authorization ->
                                if (n.tokenType == "Bearer")
                                    return@map LoginResult(false, n.accessToken) else
                                    return@map LoginResult(true, "Ошибка авторизации")
                            }
                            .onErrorReturnItem(LoginResult(true, "Сетевая ошибка"))
                    }
                    .observeOn(AndroidSchedulers.mainThread())

                loginCompositeDisposable.add(loginObservable.subscribe { be: LoginResult ->
                    if (be.hasError) {
                        mSession.invalidate()
                    } else {
                        mSession.saveToken("Bearer " + be.message)
                        val builder = mSession.getToken()?.let {
                            mRequest!!.newBuilder()
                                .header("Authorization", it)
                                .method(mRequest!!.method, mRequest!!.body)
                        }
                        mResponse = chain.proceed(builder!!.build())
                    }
                })
            }
        }
        return mResponse!!
    }

    companion object {
        fun getAuthorizationHeader(email: String, password: String): String {
            val credential = "$email:$password"
            return "Basic " + Base64.encodeToString(credential.toByteArray(), Base64.DEFAULT)
        }
    }
}