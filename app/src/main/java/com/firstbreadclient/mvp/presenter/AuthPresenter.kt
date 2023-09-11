package com.firstbreadclient.mvp.presenter

import android.util.Log
import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.mvp.model.AuthInteractor
import com.firstbreadclient.mvp.view.AuthView
import com.firstbreadclient.network.OkHttpClientInstance
import com.firstbreadclient.network.result.LoginResult
import com.firstbreadclient.network.result.UIEvent
import com.firstbreadclient.network.security.Authorization
import com.firstbreadclient.network.security.Registration
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AuthPresenter @Inject constructor(private var authInteractor: AuthInteractor) {
    private var mAuthView: AuthView? = null
    private val loginCompositeDisposable = CompositeDisposable()

    fun bind(authView: AuthView) {
        mAuthView = authView
    }

    fun unbind() {
        mAuthView = null
    }

    fun authToken() {
        val userNameObservable = Observable.just(OkHttpClientInstance.getSession()?.getCntkod())
        val passwordObservable = Observable.just(OkHttpClientInstance.getSession()?.getPassword())

        val uiResultObservable = Observable.combineLatest(
            userNameObservable,
            passwordObservable
        ) { cntkod: String?, password: String? -> UIEvent(cntkod, password) }

        val loginObservable = uiResultObservable
            .flatMap { log: UIEvent ->
                authInteractor.signin(Registration(log.cntkod, log.password))!!
                    .subscribeOn(Schedulers.io())
                    .map { author: Authorization ->
                        when (author.tokenType) {
                            "Bearer" -> LoginResult(false, author.accessToken)
                            else -> LoginResult(true, "Ошибка авторизации")
                        }
                    }
                    .onErrorReturnItem(LoginResult(true, "Сетевая ошибка"))
            }
            .observeOn(AndroidSchedulers.mainThread())

        loginCompositeDisposable.add(loginObservable.subscribe { be: LoginResult ->
            if (be.hasError) {
                be.message?.let {
                    mAuthView?.showSnackbar(it)
                }
            } else {
                OkHttpClientInstance.getSession()?.saveToken("Bearer " + be.message)
                authData()
            }
        })
    }

    fun authData() {
        val tokenAll = "25d55ad283aa400af464c76d713c07ad"

        val jwt = OkHttpClientInstance.getSession()?.getToken()
        jwt ?: return

        val call = authInteractor.getAuthData(jwt, tokenAll)

        Log.i("URL Called", call?.request()?.url.toString() + "")

        call?.enqueue(object : Callback<ArrayList<Auth?>?> {
            override fun onResponse(
                call: Call<ArrayList<Auth?>?>,
                response: Response<ArrayList<Auth?>?>
            ) {
                if (!response.isSuccessful) {
                    if (response.code() != 401)
                        mAuthView?.showToast("Ошибка соединения")
                    return
                }
                mAuthView?.updateAuthUi(response.body())
            }

            override fun onFailure(call: Call<ArrayList<Auth?>?>, t: Throwable) {
                t.message?.let { Log.e("Error message", it) }
                mAuthView?.showToast("Something went wrong...Error message: " + t.message)
            }
        })
    }
}