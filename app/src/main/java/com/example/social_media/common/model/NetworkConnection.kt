package com.example.social_media.common.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import io.reactivex.rxjava3.core.Observable

class NetworkConnection {

    fun networkObservable(context: Context) : Observable<String>{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        return Observable.create{ emitter ->
            cm.registerNetworkCallback(networkRequest, object : NetworkCallback(){
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    emitter.onNext("CONNECTED")
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    emitter.onNext("NO CONNECTION")
                }
            })
        }
    }

    companion object{
        @RequiresApi(Build.VERSION_CODES.M)
        fun isOnline(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetwork
            val capabilities = cm.getNetworkCapabilities(activeNetwork)
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    }
}