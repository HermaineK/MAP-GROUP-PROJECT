package com.example.valentinesgarage.ui.truckdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.local.entity.JobStatus
import com.example.valentinesgarage.data.local.entity.RepairTask
import com.example.valentinesgarage.data.local.entity.Truck
import com.example.valentinesgarage.data.repository.GarageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TruckDetailViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    private val _truck = MutableStateFlow<Truck?>(null)
    val truck: StateFlow<Truck?> = _truck

    private val _tasks = MutableStateFlow<List<RepairTask>>(emptyList())
    val tasks: StateFlow<List<RepairTask>> = _tasks

    fun loadTruck(truckId: Int) {
        viewModelScope.launch {
            _truck.value = repository.getTruckById(truckId)
        }
    }

    fun loadTasks(truckId: Int) {
        viewModelScope.launch {
            _tasks.value = repository.getTasksForTruckOnce(truckId)
        }
    }

    fun closeJob(truckId: Int) {
        viewModelScope.launch {
            repository.updateTruckStatus(
                truckId = truckId,
                status = JobStatus.CLOSED
            )

            _truck.value = repository.getTruckById(truckId)
        }
    }
}