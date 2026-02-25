package com.vam.coroutinesflowshomework.homework

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class BannerViewModel(appContext: Context) : ViewModel() {

    private val connectivityManager = AppConnectivityManager(appContext.applicationContext)

    val isInternet = connectivityManager
        .listenInternetConnection()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )
}