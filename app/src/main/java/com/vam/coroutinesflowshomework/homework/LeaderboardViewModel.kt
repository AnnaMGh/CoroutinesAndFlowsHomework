package com.vam.coroutinesflowshomework.homework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.random.Random

class LeaderboardViewModel : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state = _state.asStateFlow()

    private val _listener: (List<Pair<String, Int>>) -> Unit = { topThree ->
        _state.update {
            it.copy(
                topThree = topThree
            )
        }
    }
    private val _leaderboard = Leaderboard()

    init {

        _leaderboard.addListener(_listener)
        _state.update {
            it.copy(
                isRegistered = true
            )
        }

        viewModelScope.launch(Dispatchers.IO.limitedParallelism(1)) {
            (1..5_000).map { index ->
                launch {
                    val playerName = "Player $index"
                    val playerScore = Random.nextInt(1, 10_0000)
                    _leaderboard.updateScore(playerName, playerScore)
                }
            }.joinAll()
            _state.update {
                it.copy(
                    isFinished = true
                )
            }
        }
    }

    fun onAction(action: LeaderboardAction) {
        when (action) {
            is LeaderboardAction.OnRegister -> {
                _leaderboard.addListener(_listener)
                _state.update {
                    it.copy(
                        isRegistered = true
                    )
                }

            }

            is LeaderboardAction.OnUnregister -> {
                _leaderboard.removeListener(_listener)
                _state.update {
                    it.copy(
                        isRegistered = false
                    )
                }
            }
        }
    }
}