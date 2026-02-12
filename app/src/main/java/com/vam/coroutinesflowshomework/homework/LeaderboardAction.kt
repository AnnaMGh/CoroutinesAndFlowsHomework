package com.vam.coroutinesflowshomework.homework

sealed interface LeaderboardAction {
    object OnRegister : LeaderboardAction
    object OnUnregister : LeaderboardAction
}