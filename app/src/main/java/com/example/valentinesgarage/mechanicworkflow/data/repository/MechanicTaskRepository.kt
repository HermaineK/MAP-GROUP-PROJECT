package com.example.valentinesgarage.mechanicworkflow.data.repository

import com.example.valentinesgarage.mechanicworkflow.data.model.RepairTask
import kotlinx.coroutines.flow.Flow

interface MechanicTaskRepository {
    fun getTasksForMechanic(mechanicId: String): Flow<List<RepairTask>>
    suspend fun toggleTaskCompletion(taskId: String)
    suspend fun addNoteToTask(taskId: String, noteContent: String)
}
