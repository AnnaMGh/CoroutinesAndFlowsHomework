package com.vam.coroutinesflowshomework.homework

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch


class ConnectivityObserver(appContext: Context) {
    private val connectivityManager =
        appContext.applicationContext.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    fun observeInternetConnection(): Flow<Boolean> {
        return callbackFlow {

            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(true) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(false) }
                }
            }

            connectivityManager.requestNetwork(networkRequest, networkCallback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
        }
    }
}