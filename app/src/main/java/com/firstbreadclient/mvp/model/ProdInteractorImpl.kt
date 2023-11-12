package com.firstbreadclient.mvp.model

import com.firstbreadclient.model.data.Prod
import com.firstbreadclient.network.OkHttpClientInstance
import com.firstbreadclient.network.RetrofitInstance
import com.firstbreadclient.network.listener.InternetConnectionListener
import com.firstbreadclient.service.GetDataService
import okhttp3.ResponseBody
import retrofit2.Call

class ProdInteractorImpl(internetConnectionListener: InternetConnectionListener?) : ProdInteractor {
    private var mService: GetDataService? = null

    init {
        mService = RetrofitInstance.retrofitInstance?.create(GetDataService::class.java)
        OkHttpClientInstance.setInternetConnectionListener(internetConnectionListener)
    }

    override fun getProdData(jwt: String?, daysId: String?): Call<ArrayList<Prod?>?>? {
        return mService?.getProdData(jwt, daysId)
    }

    override fun putProd(jwt: String?, prod: List<Prod>): Call<ResponseBody>? {
        return mService?.putProd(jwt, prod)
    }
}