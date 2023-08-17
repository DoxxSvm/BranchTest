package com.oogwayapps.branchthreads.api

import android.util.Log
import com.oogwayapps.branchthreads.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request =chain.request().newBuilder()
        val token = tokenManager.getAuthToken()
        if (token != null) {
            request.addHeader("X-Branch-Auth-Token","BCGLnkQaOeMd7RP_yuTDuw")
        }
        return chain.proceed(request.build())
    }
}