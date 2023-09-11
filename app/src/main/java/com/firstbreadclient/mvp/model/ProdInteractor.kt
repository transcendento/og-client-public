package com.firstbreadclient.mvp.model

import com.firstbreadclient.model.data.Prod
import okhttp3.ResponseBody
import retrofit2.Call

interface ProdInteractor {
    fun getProdData(jwt: String?, daysId: String?): Call<ArrayList<Prod?>?>?
    fun postProd(prod: List<Prod>): Call<ResponseBody>?
}