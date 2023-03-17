package com.luis.aguilar.android.employeelist.data.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val requestBuilder = request.newBuilder()

        requestBuilder.addHeader("Content-Type", "application/json;charset=utf-8'")

        request = requestBuilder.build()

        return chain.proceed(request)
    }
}