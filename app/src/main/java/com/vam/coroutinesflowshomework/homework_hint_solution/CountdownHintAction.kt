package com.vam.coroutinesflowshomework.homework_hint_solution

sealed interface CountdownHintAction {
    object OnActionStartCountdown : CountdownHintAction
    data class OnActionSetCounter(val counter: String) : CountdownHintAction
}