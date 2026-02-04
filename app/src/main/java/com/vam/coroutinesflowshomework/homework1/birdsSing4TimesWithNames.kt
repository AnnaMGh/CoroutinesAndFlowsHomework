package com.vam.coroutinesflowshomework.homework1

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun birdsSing4TimesWithNames() {
    GlobalScope.launch {
        val jobCoo = launch(CoroutineName("Tweety")) {
            repeat(4) {
                println("${coroutineContext[CoroutineName]?.name} sings Coo")
                delay(1000)
            }
        }

        val jobCaw = launch(CoroutineName("Zazu")) {
            repeat(4) {
                println("${coroutineContext[CoroutineName]?.name} sings Caw")
                delay(2000)
            }
        }

        val jobChirp = launch(CoroutineName("Woodstock")) {
            repeat(4) {
                println("${coroutineContext[CoroutineName]?.name} sings Chirp")
                delay(3000)
            }
        }

        jobCoo.join()
        jobCaw.join()
        jobChirp.join()
    }
}