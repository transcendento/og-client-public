package com.firstbreadclient.network.interceptor

import com.firstbreadclient.network.security.Session
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class TokenRenewInterceptor(private val session: Session) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        val newToken = response.header("x-auth-token")
        if (newToken != null) {
            session.saveToken(newToken)
        }
        return response
    }
}