package com.vam.coroutinesflowshomework.homework

data class LeaderboardState(
    val topThree: List<Pair<String, Int>> = listOf(),
    val isFinished: Boolean = false,
    val isRegistered: Boolean= false
)