package com.vam.coroutinesflowshomework.homework3

sealed interface BirdAction {
    data class Sing(val id: String) : BirdAction
}