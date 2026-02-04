package com.vam.coroutinesflowshomework.homework1

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun birdsSing4Times() {
    GlobalScope.launch {

        val jobCoo = launch {
            repeat(4){
                println("Coo")
                delay(1000)
            }
        }

        val jobCaw = launch {
            repeat(4) {
                println("Caw")
                delay(2000)
            }
        }

        val jobChirp = launch {
            repeat(4){
                println("Chirp")
                delay(3000)
            }
        }

        jobCoo.join()
        jobCaw.join()
        jobChirp.join()
    }
}
