package com.vam.coroutinesflowshomework.homework

sealed interface BirdAction {
    data class Sing(val id: String) : BirdAction
}