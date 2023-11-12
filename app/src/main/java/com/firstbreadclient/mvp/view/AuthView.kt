package com.firstbreadclient.mvp.view

import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.model.data.Prod

interface AuthView {
    fun updateAuthUi(authList: ArrayList<Auth?>?)
    fun showToast(message: String)
    fun showSnackbar(message: String)
    fun getListProds(): List<Prod>?

}