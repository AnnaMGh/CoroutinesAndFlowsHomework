package com.vam.coroutinesflowshomework.homework

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class NetworkLog(
    val timestamp: DisplayableTimestamp,
    val isNetworkAvailable: Boolean,
    val latitude: Double,
    val longitude: Double
)

data class DisplayableTimestamp(
    val value: ZonedDateTime,
    val formattedValue: String
)

fun ZonedDateTime.toDisplayableValue(): String {
    return DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy").format(this)
}
