package com.example.valentinesgarage.ui.repair

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.repository.GarageRepository
import com.example.valentinesgarage.data.local.entity.RepairTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepairViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<RepairTask>>(emptyList())
    val tasks: StateFlow<List<RepairTask>> = _tasks

    fun loadTasksForTruck(truckId: Int) {
        viewModelScope.launch {
            repository.getTasksForTruck(truckId).collect {
                _tasks.value = it
            }
        }
    }

    fun updateTask(task: RepairTask) {
        viewModelScope.launch {
            repository.updateRepairTask(task)
            loadTasksForTruck(task.truckId)
        }
    }
}