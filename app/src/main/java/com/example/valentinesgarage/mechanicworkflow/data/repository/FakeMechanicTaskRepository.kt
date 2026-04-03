package com.example.valentinesgarage.mechanicworkflow.data.repository

import com.example.valentinesgarage.mechanicworkflow.data.model.RepairTask
import com.example.valentinesgarage.mechanicworkflow.data.model.TaskUpdateNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

class FakeMechanicTaskRepository : MechanicTaskRepository {

    private val mutex = Mutex()
    private val _tasks = MutableStateFlow(sampleTasks())

    override fun getTasksForMechanic(mechanicId: String): Flow<List<RepairTask>> =
        _tasks.map { list -> list.filter { it.assignedMechanicId == mechanicId } }

    override suspend fun toggleTaskCompletion(taskId: String) {
        mutex.withLock {
            _tasks.value = _tasks.value.map { task ->
                if (task.id == taskId) task.copy(isCompleted = !task.isCompleted) else task
            }
        }
    }

    override suspend fun addNoteToTask(taskId: String, noteContent: String) {
        if (noteContent.isBlank()) return
        mutex.withLock {
            _tasks.value = _tasks.value.map { task ->
                if (task.id == taskId) {
                    val note = TaskUpdateNote(id = UUID.randomUUID().toString(), content = noteContent)
                    task.copy(notes = task.notes + note)
                } else task
            }
        }
    }

    private fun sampleTasks(): List<RepairTask> = listOf(
        RepairTask("t1", "truck-01", "Replace front brake pads", "m1"),
        RepairTask("t2", "truck-01", "Oil filter change", "m1"),
        RepairTask("t3", "truck-02", "Check engine light diagnosis", "m2"),
        RepairTask("t4", "truck-03", "Tire rotation", "m1")
    )
}
