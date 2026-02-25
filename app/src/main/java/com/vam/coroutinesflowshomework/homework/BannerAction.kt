package com.vam.coroutinesflowshomework.homework

sealed interface BannerAction {
    data class OnLocationPermissionResponse(val isGranted: Boolean) : BannerAction
}