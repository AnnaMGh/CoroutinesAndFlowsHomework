package com.vam.coroutinesflowshomework.homework3

import  com.vam.coroutinesflowshomework.R
import java.util.UUID

object DummyData {
    val birdsList = listOf(
        BirdModel(id = UUID.randomUUID().toString(), imgRes = R.drawable.caw, song = "Caw"),
        BirdModel(id = UUID.randomUUID().toString(), imgRes = R.drawable.coo, song = "Coo"),
        BirdModel(id = UUID.randomUUID().toString(), imgRes = R.drawable.chirp, song = "Chirp")
    )
}