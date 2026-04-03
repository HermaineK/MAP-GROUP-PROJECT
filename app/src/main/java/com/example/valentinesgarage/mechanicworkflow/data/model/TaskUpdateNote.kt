package com.example.valentinesgarage.mechanicworkflow.data.model

data class TaskUpdateNote(
    val id: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
