package com.firstbreadclient.mvp.presenter

import android.util.Log
import com.firstbreadclient.model.data.Prod
import com.firstbreadclient.mvp.model.ProdInteractor
import com.firstbreadclient.mvp.view.ProdView
import com.firstbreadclient.network.OkHttpClientInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ProdPresenter @Inject constructor(private var prodInteractor: ProdInteractor) {
    private var mProdView: ProdView? = null
    private var mProdList: ArrayList<Prod?>? = null

    fun bind(prodView: ProdView) {
        mProdView = prodView
    }

    fun unbind() {
        mProdView = null
    }

    fun prodData() {
        val daysId = mProdView?.putIntent()?.getStringExtra("daysordermoveid")
        val jwt = OkHttpClientInstance.getSession()?.getToken()
        val call = prodInteractor.getProdData(jwt, daysId)
        Log.i("URL Called", call?.request()?.url.toString() + "")
        call?.enqueue(object : Callback<ArrayList<Prod?>?> {
            override fun onResponse(
                call: Call<ArrayList<Prod?>?>,
                response: Response<ArrayList<Prod?>?>
            ) {
                if (!response.isSuccessful) {
                    mProdView?.showToast("Ошибка соединения")
                    return
                }
                mProdList = response.body()
                for (prod in mProdList!!) {
                    if (prod != null) {
                        mProdView?.putViewModel()?.insertProd(prod)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Prod?>?>, t: Throwable) {
                t.message?.let { Log.e("Error message", it) }
                mProdView?.showToast("Something went wrong...Error message: " + t.message)
            }
        })
    }

    fun prodPost() {
        val allProd = mProdView?.putViewModel()?.allProds?.value
        val call = allProd?.let { prodInteractor.postProd(it) }
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}
