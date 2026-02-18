package com.vam.coroutinesflowshomework.homework_my_solution

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountDownViewModel : ViewModel() {

    private val _state = MutableStateFlow(CountdownState())
    val state = _state.asStateFlow()


    fun onAction(action: CountdownAction) {
        when (action) {
            CountdownAction.OnActionStartCountdown -> {
                _state.update { it.copy(isStarted = true) }
                viewModelScope.launch {
                    for (i in 10 downTo 0) {
                        _state.update { it.copy(countdown = i, isStarted = i > 0) }
                        delay(1000)
                    }
                }
            }
        }
    }
}