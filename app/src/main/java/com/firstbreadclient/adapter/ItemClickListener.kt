package com.firstbreadclient.adapter

import android.view.View

interface ItemClickListener {
    fun onClick(view: View?, position: Int)
}