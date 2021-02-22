package com.firstbreadclient.mvp.view

import com.firstbreadclient.model.data.Auth

interface AuthView {
    fun updateAuthUi(authList: ArrayList<Auth?>?)
    fun showToast(message: String)
    fun showSnackbar(message: String)
}