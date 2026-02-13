package com.vam.coroutinesflowshomework.homework

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Leaderboard() {
    val allScores: HashMap<String, Int> = hashMapOf()
    val listeners: ArrayList<(List<Pair<String, Int>>) -> Unit> = arrayListOf()

    val mutex = Mutex()

    suspend fun addListener(listener: (List<Pair<String, Int>>) -> Unit) {
        mutex.withLock {
            listeners.add(listener)
        }
    }

    suspend fun removeListener(listener: (List<Pair<String, Int>>) -> Unit) {
        mutex.withLock {
            listeners.remove(listener)
        }
    }

    suspend fun updateScore(playerName: String, playerScore: Int) {
        val topThree: List<Pair<String, Int>>
        val currentListeners: List<(List<Pair<String, Int>>) -> Unit>

        mutex.withLock {
            val currentScore = allScores[playerName] ?: 0
            allScores[playerName] = currentScore + playerScore
            topThree = allScores.entries
                .sortedByDescending { it.value }
                .take(3)
                .map { it.key to it.value }
            currentListeners = listeners.toList()
        }

        currentListeners.forEach { listener ->
            listener.invoke(topThree)
        }
    }
}