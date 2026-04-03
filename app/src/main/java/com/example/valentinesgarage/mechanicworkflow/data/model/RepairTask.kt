package com.example.valentinesgarage.mechanicworkflow.data.model

data class RepairTask(
    val id: String,
    val truckId: String,
    val description: String,
    val assignedMechanicId: String,
    val isCompleted: Boolean = false,
    val notes: List<TaskUpdateNote> = emptyList()
)
