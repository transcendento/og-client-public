package com.firstbreadclient.firebase

interface OnGetServerTime {
    fun onSuccess(dateServer: String?, timeServer: String?)
    fun onFailed(dateDevice: String?, timeDevice: String?)
}