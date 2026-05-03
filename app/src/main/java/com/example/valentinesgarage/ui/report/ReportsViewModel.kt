package com.example.valentinesgarage.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.local.entity.RepairTask
import com.example.valentinesgarage.data.local.entity.Truck
import com.example.valentinesgarage.data.repository.GarageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ReportsUiState(
    val totalTrucks: Int = 0,
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val pendingTasks: Int = 0,
    val trucks: List<Truck> = emptyList(),
    val tasks: List<RepairTask> = emptyList()
)

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    val uiState: StateFlow<ReportsUiState> = combine(
        repository.getAllTrucks(),
        repository.getAllRepairTasks()
    ) { trucks, tasks ->
        ReportsUiState(
            totalTrucks = trucks.size,
            totalTasks = tasks.size,
            completedTasks = tasks.count { it.isCompleted },
            pendingTasks = tasks.count { !it.isCompleted },
            trucks = trucks,
            tasks = tasks
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ReportsUiState()
    )
}