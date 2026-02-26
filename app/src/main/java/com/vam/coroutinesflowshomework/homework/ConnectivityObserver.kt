package com.vam.coroutinesflowshomework.homework

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

    fun observeNetworkReachability(milliseconds: Int = 3000): Flow<Boolean> =
        flow {
            while (currentCoroutineContext().isActive) {
                emit(hasInternetHttp(milliseconds))
                delay(milliseconds.toLong())
            }
        }.cancellable()
            .flowOn(Dispatchers.IO)


    private suspend fun hasInternetHttp(timeout: Int = 3000): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val url = java.net.URL("https://clients3.google.com/generate_204")
                (url.openConnection() as java.net.HttpURLConnection).run {
                    connectTimeout = timeout
                    readTimeout = timeout
                    instanceFollowRedirects = false
                    useCaches = false
                    requestMethod = "GET"
                    connect()
                    responseCode == 204
                }
            } catch (e: Exception) {
                ensureActive()
                false
            }
        }
}

