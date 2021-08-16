package com.cornershop.counterstest.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import javax.inject.Inject

class WifiService @Inject constructor(private val context: Context) {

    private lateinit var wifiManager: WifiManager
    private lateinit var connectivityManager: ConnectivityManager

    fun isOnline(): Boolean {
        wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}