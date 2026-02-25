package com.vam.coroutinesflowshomework.homework

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

suspend fun hasInternetHttp(timeout: Int = 3000): Boolean = withContext(Dispatchers.IO) {
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