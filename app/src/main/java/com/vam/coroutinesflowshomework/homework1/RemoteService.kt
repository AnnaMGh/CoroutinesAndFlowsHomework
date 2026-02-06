package com.vam.coroutinesflowshomework.homework1


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

object RemoteService {
    suspend fun uploadFile() {
        // File uploading - Dispatchers.IO
        withContext(Dispatchers.IO) {
            val chunks = List(400) { it }
            println("Uploading File")
            var index = 0

            // Check if is still active
            while (isActive && index < chunks.size) {
                try {
                    // Simulate uploading a single chunk of data
                    delay(5)
                    index++
                    println("Progress: ${index * 100 / chunks.size}%")
                } catch (e: Exception) {
                    // Handle cancellation exception
                    ensureActive()
                    println("Error uploading file: ${e.message}")
                }
            }
            println("Upload Complete")
        }
    }
}