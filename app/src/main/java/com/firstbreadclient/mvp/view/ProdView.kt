package com.firstbreadclient.mvp.view

import android.content.Intent
import com.firstbreadclient.model.data.Prod
import com.firstbreadclient.room.FirstViewModel

interface ProdView {
    fun updateProdUi(prodList: ArrayList<Prod?>?)
    fun showToast(message: String)
    fun showSnackbar(message: String)
    fun putIntent(): Intent?
    fun putViewModel(): FirstViewModel

}