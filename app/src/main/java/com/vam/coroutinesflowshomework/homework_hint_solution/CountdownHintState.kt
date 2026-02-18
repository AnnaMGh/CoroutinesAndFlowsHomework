package com.vam.coroutinesflowshomework.homework_hint_solution

data class CountdownHintState(
    val counter: Int = 10,
    val countdown: Int = 10,
    val isStarted: Boolean = false
) {
    val counterDisplay: String
        get() = if(counter == 0) "" else counter.toString()
}