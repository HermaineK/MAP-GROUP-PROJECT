package com.example.valentinesgarage.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.repository.GarageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val totalTrucks: Int = 0,
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val pendingTasks: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        repository.getAllTrucks(),
        repository.getAllRepairTasks()
    ) { trucks, tasks ->
        HomeUiState(
            totalTrucks = trucks.size,
            totalTasks = tasks.size,
            completedTasks = tasks.count { it.isCompleted },
            pendingTasks = tasks.count { !it.isCompleted }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )
}