package com.vam.coroutinesflowshomework.homework

data class BirdState(
    val currentBird: BirdModel? = null,
    val birds: List<BirdModel>
)

data class BirdModel(
    val id: String,
    val imgRes: Int,
    val song: String
)