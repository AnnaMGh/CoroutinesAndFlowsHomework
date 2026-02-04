package com.vam.coroutinesflowshomework.homework2


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

fun birdsSingIndefinitely() {
    GlobalScope.launch {
        launch {
            while (isActive) {
                println("Coo")
                delay(1000)
            }
        }

        launch {
            while (isActive) {
                println("Caw")
                delay(2000)
            }
        }

        launch {
            while (isActive) {
                println("Chirp")
                delay(3000)
            }
        }

        delay(10000)

        this.cancel()
    }
}