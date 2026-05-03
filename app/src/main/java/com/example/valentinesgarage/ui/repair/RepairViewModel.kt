package com.example.valentinesgarage.ui.repair

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.local.entity.JobStatus
import com.example.valentinesgarage.data.local.entity.RepairTask
import com.example.valentinesgarage.data.repository.GarageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RepairViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    private val _selectedTruckId = MutableStateFlow(1)

    val tasks: StateFlow<List<RepairTask>> = _selectedTruckId
        .flatMapLatest { truckId ->
            repository.getTasksForTruck(truckId)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun setTruckId(truckId: Int) {
        _selectedTruckId.value = truckId
    }

    fun addTask(
        description: String,
        mechanicName: String
    ) {
        if (description.isBlank()) return
        if (mechanicName.isBlank()) return

        viewModelScope.launch {
            repository.addRepairTask(
                RepairTask(
                    truckId = _selectedTruckId.value,
                    description = description.trim(),
                    assignedMechanicId = null,
                    mechanicName = mechanicName.trim(),
                    isCompleted = false,
                    notes = ""
                )
            )

            repository.updateTruckStatus(
                truckId = _selectedTruckId.value,
                status = JobStatus.IN_PROGRESS
            )
        }
    }

    fun toggleTask(task: RepairTask) {
        viewModelScope.launch {
            val updatedTask = task.copy(
                isCompleted = !task.isCompleted
            )

            repository.updateRepairTask(updatedTask)

            val allTasks = repository.getTasksForTruckOnce(_selectedTruckId.value)

            if (allTasks.isNotEmpty() && allTasks.all { it.isCompleted }) {
                repository.updateTruckStatus(
                    truckId = _selectedTruckId.value,
                    status = JobStatus.COMPLETED
                )
            } else {
                repository.updateTruckStatus(
                    truckId = _selectedTruckId.value,
                    status = JobStatus.IN_PROGRESS
                )
            }
        }
    }

    fun updateNotes(task: RepairTask, notes: String) {
        viewModelScope.launch {
            repository.updateRepairTask(
                task.copy(notes = notes)
            )
        }
    }

    fun deleteTask(task: RepairTask) {
        viewModelScope.launch {
            repository.deleteRepairTask(task)
        }
    }
}