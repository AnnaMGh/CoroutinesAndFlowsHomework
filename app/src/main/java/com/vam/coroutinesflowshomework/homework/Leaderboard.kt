package com.vam.coroutinesflowshomework.homework

class Leaderboard() {
    val allScores: HashMap<String, Int> = hashMapOf()
    val listeners: ArrayList<(List<Pair<String, Int>>) -> Unit> = arrayListOf()

    fun addListener(listener: (List<Pair<String, Int>>) -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: (List<Pair<String, Int>>) -> Unit) {
        listeners.remove(listener)
    }

    fun updateScore(playerName: String, playerScore: Int) {
        val currentScore: Int = allScores[playerName] ?: 0
        allScores[playerName] = currentScore + playerScore

        val topThree = allScores
            .toList()
            .sortedByDescending { it.second }
            .take(3)

        listeners.forEach { listener ->
            listener.invoke(topThree)
        }
    }
}