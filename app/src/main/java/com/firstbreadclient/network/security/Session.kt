package com.firstbreadclient.network.security

import com.firstbreadclient.service.LoginService

interface Session {
    fun getService(): LoginService?
    fun isLoggedIn(): Boolean
    fun saveToken(token: String?)
    fun getToken(): String?
    fun saveCntkod(cntkod: String?)
    fun getCntkod(): String?
    fun savePassword(password: String?)
    fun getPassword(): String?
    fun getDaysId(): String?
    fun invalidate()
}