package com.cornershop.counterstest.data.datasources.remote

import com.cornershop.counterstest.data.utils.WifiService
import com.cornershop.counterstest.domain.utils.Utils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class ConnectivityInterceptor @Inject constructor ( private var wifiService: WifiService)  : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!wifiService.isOnline()) {
            throw IOException(Utils.INTERNET_CONNECTION_MESSAGE)
        } else {
            return chain.proceed(chain.request())
        }
    }
}