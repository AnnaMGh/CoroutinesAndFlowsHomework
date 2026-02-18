@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vam.coroutinesflowshomework.homework_hint_solution

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


class CountdownHintViewModel() : ViewModel() {

    private val _state = MutableStateFlow(CountdownHintState())
    val state = _state.asStateFlow()

    private val restartTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val countFlow: StateFlow<Int> = restartTrigger.flatMapLatest {
        flow {
            var count = state.value.counter
            emit(count)
            while (count > 0) {
                delay(1000)
                count--
                emit(count)
            }
        }
    }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            state.value.counter
        )


    fun onAction(action: CountdownHintAction) {
        when (action) {
            CountdownHintAction.OnActionStartCountdown -> {
                countFlow
                    .onEach { countdown ->
                        _state.update { it.copy(countdown = countdown, isStarted = countdown != 0) }
                    }
                    .launchIn(viewModelScope)
                restartTrigger.tryEmit(Unit)
            }

            is CountdownHintAction.OnActionSetCounter -> {
                val counterDisplay = (action.counter.toIntOrNull() ?: "").toString().take(3)
                val counter = if (counterDisplay == "") 0 else counterDisplay.toInt()
                _state.update { it.copy(counter = counter, countdown = counter) }
            }
        }
    }
}
