package com.vam.coroutinesflowshomework.homework

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import java.time.ZonedDateTime

class BannerViewModel(appContext: Context) : ViewModel() {

    private val connectivityObserver = ConnectivityObserver(appContext.applicationContext)
    private val locationObserver = LocationObserver(appContext.applicationContext)
    private val _isLocationPermissionGranted = MutableStateFlow(false)
    val isLocationPermissionGranted = _isLocationPermissionGranted.asStateFlow()

    val isInternet = combine(
        connectivityObserver.observeInternetConnection(),
        connectivityObserver.observeNetworkReachability(),
    ) { systemConnected, actuallyReachable ->
        systemConnected && actuallyReachable
    }.distinctUntilChanged()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )


    val logs = _isLocationPermissionGranted
        .flatMapLatest { granted ->
            if (!granted) flowOf(emptyList())
            else combine(
                connectivityObserver.observeInternetConnection(),
                connectivityObserver.observeNetworkReachability()
            ) { systemConnected, actuallyReachable ->
                systemConnected && actuallyReachable
            }
                .distinctUntilChanged()
                .combine(
                    locationObserver.observeLocation(1000L)
                ) { hasNetwork, location ->
                    val time = ZonedDateTime.now()
                    NetworkLog(
                        DisplayableTimestamp(time, time.toDisplayableValue()),
                        hasNetwork,
                        location.latitude,
                        location.longitude
                    )
                }
                .distinctUntilChanged { old, new ->  // only log when network status changes
                    old.isNetworkAvailable == new.isNetworkAvailable
                }
                .runningFold(emptyList<NetworkLog>()) { list, current ->
                    list + current
                }
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