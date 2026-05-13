package com.example.valentinesgarage.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.local.entity.JobStatus
import com.example.valentinesgarage.data.local.entity.RepairTask
import com.example.valentinesgarage.data.local.entity.Truck
import com.example.valentinesgarage.data.repository.GarageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
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
    val tasks: List<RepairTask> = emptyList(),
    val employeeNames: List<String> = emptyList(),
    val selectedEmployee: String? = null,
    val selectedStatus: JobStatus? = null,
    val searchQuery: String = ""
) {
    
    val filteredTrucks: List<Truck>
        get() = trucks
            .filter { truck ->
                selectedStatus == null || truck.status == selectedStatus
            }
            .filter { truck ->
                searchQuery.isBlank() ||
                    truck.licensePlate.contains(searchQuery, ignoreCase = true)
            }

 
 
    val filteredTasks: List<RepairTask>
        get() = if (selectedEmployee == null) tasks
                else tasks.filter { it.mechanicName == selectedEmployee }
}


@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    // ── Mutable filter / search state ─────────────────────────────────────────

    private val _selectedEmployee = MutableStateFlow<String?>(null)
    private val _selectedStatus   = MutableStateFlow<JobStatus?>(null)
    private val _searchQuery      = MutableStateFlow("")

    
    val uiState: StateFlow<ReportsUiState> = combine(
        repository.getAllTrucks(),
        repository.getAllRepairTasks(),
        _selectedEmployee,
        _selectedStatus,
        _searchQuery
    ) { trucks, tasks, selectedEmployee, selectedStatus, searchQuery ->

        val employeeNames = tasks
            .map { it.mechanicName }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()

        ReportsUiState(
            totalTrucks      = trucks.size,
            totalTasks       = tasks.size,
            completedTasks   = tasks.count { it.isCompleted },
            pendingTasks     = tasks.count { !it.isCompleted },
            trucks           = trucks,
            tasks            = tasks,
            employeeNames    = employeeNames,
            selectedEmployee = selectedEmployee,
            selectedStatus   = selectedStatus,
            searchQuery      = searchQuery
        )
    }.stateIn(
        scope            = viewModelScope,
        started          = SharingStarted.WhileSubscribed(5_000),
        initialValue     = ReportsUiState()
    )

    // ── Filter / search event handlers ────────────────────────────────────────

    fun onStatusSelected(status: JobStatus?) {
        _selectedStatus.value = if (_selectedStatus.value == status) null else status
    }

    fun onEmployeeSelected(name: String?) {
        _selectedEmployee.value = if (_selectedEmployee.value == name) null else name
    }

  
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

   
    fun clearFilters() {
        _selectedEmployee.value = null
        _selectedStatus.value   = null
        _searchQuery.value      = ""
    }
}
