package com.vam.coroutinesflowshomework.homework

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import java.time.ZonedDateTime

class BannerViewModel(appContext: Context) : ViewModel() {

    private val connectivityObserver = ConnectivityObserver(appContext.applicationContext)
    private val locationObserver = LocationObserver(appContext.applicationContext)
    private val _isLocationPermissionGranted = MutableStateFlow(false)
    val isLocationPermissionGranted = _isLocationPermissionGranted.asStateFlow()

    val isInternet = connectivityObserver
        .observeInternetConnection()
        .onEach { println("isInternet: isInternet $it") }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val logs = isInternet
        .flatMapLatest { isConnected ->
            flow {
                val reachable = if (!isConnected) false else hasInternetHttp()
                emit(reachable)
            }
        }
        .zip(locationObserver.observeLocation(1000L)) { hasNetworkConnection, location ->
            val time = ZonedDateTime.now()
            NetworkLog(
                DisplayableTimestamp(time, time.toDisplayableValue()),
                hasNetworkConnection,
                location.latitude,
                location.longitude
            )
        }
        .runningFold(initial = emptyList<NetworkLog>()) { list, current ->
            list + current
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )


    init {
        _isLocationPermissionGranted.value = ContextCompat.checkSelfPermission(
            appContext.applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            appContext.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun onAction(action: BannerAction) {
        when (action) {
            is BannerAction.OnLocationPermissionResponse -> {
                _isLocationPermissionGranted.value = action.isGranted
            }
        }

    }
}