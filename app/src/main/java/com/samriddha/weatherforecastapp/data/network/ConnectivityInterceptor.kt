package com.samriddha.weatherforecastapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.samriddha.weatherforecastapp.utils.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

interface ConnectivityInterceptor:Interceptor

class ConnectivityInterceptorImpl(context: Context):
    ConnectivityInterceptor {

    private val appContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!isOnline(appContext))
            throw NoInternetException()

        return chain.proceed(chain.request())

    }


    @Suppress("DEPRECATION")
    private fun isOnline(context: Context):Boolean{

        var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_VPN)){
                        return true
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        return true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        return true
                    } else if(type == ConnectivityManager.TYPE_VPN) {
                        return true
                    }
                }
            }
        }
        return false
    }
}