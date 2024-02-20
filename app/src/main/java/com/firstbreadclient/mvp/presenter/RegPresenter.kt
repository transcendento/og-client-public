package com.firstbreadclient.mvp.presenter

import android.util.Log
import com.firstbreadclient.mvp.model.RegInteractor
import com.firstbreadclient.mvp.view.RegView
import com.firstbreadclient.network.OkHttpClientInstance
import com.firstbreadclient.network.security.Registration
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RegPresenter @Inject constructor(private var regInteractor: RegInteractor) {
    private var mRegView: RegView? = null

    fun bind(regView: RegView) {
        mRegView = regView
    }

    fun unbind() {
        mRegView = null
    }
    fun signup(registration: Registration) {
        val jwt = OkHttpClientInstance.getSession()?.getToken()
        val call = regInteractor.signup(registration)
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.code() != 400) {
                    mRegView?.showToast("Код клиента уже существует")
                } else {
                    mRegView?.showSnackbar("Клиент сохранен")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.message?.let { Log.e("Error message", it) }
                mRegView?.showToast("Something went wrong...Error message: " + t.message)
            }
        })
    }

}