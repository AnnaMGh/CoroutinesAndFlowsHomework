package com.vam.coroutinesflowshomework.homework

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BirdSingViewModel : ViewModel() {

    private val _state = MutableStateFlow(BirdState(birds = DummyData.birdsList))
    val state = _state.asStateFlow()

    fun onAction(action: BirdAction) {
        when (action) {
            is BirdAction.Sing -> {

                val currentBird = _state.value.birds.firstOrNull { it.id == action.id }

                _state.update {
                    it.copy(currentBird = currentBird)
                }
            }
        }
    }
}