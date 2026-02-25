package com.vam.coroutinesflowshomework.homework


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationObserver(
    private val appContext: Context
) {
    private val client = LocationServices.getFusedLocationProviderClient(appContext.applicationContext)

    fun observeLocation(interval: Long): Flow<Location> {
        return callbackFlow {
            val locationManager = appContext.applicationContext.getSystemService<LocationManager>()!!

            var isGpsEnabled = false
            var isNetworkEnabled = false

            while(!isGpsEnabled && !isNetworkEnabled) {
                isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                if(!isGpsEnabled && !isNetworkEnabled) {
                    delay(3000L)
                }
            }

            val hasFineLocationPermission = ActivityCompat.checkSelfPermission(
                appContext.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            val hasCoarseLocationPermission = ActivityCompat.checkSelfPermission(
                appContext.applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if(hasFineLocationPermission && hasCoarseLocationPermission) {
                val request = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    interval
                ).build()

                val callback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        super.onLocationResult(result)
                        result.locations.lastOrNull()?.let { location ->
                            trySend(location)
                        }
                    }
                }

                client.requestLocationUpdates(request, callback, appContext.applicationContext.mainLooper)

                awaitClose {
                    client.removeLocationUpdates(callback)
                }
            } else {
                close()
            }
        }
    }
}