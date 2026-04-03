package com.example.valentinesgarage.mechanicworkflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.mechanicworkflow.data.model.RepairTask
import com.example.valentinesgarage.mechanicworkflow.data.repository.FakeMechanicTaskRepository
import com.example.valentinesgarage.mechanicworkflow.data.repository.MechanicTaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MechanicWorkflowUiState(
    val tasks: List<RepairTask> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTaskId: String? = null,
    val noteInput: String = ""
)

class MechanicWorkflowViewModel(
    private val repository: MechanicTaskRepository = FakeMechanicTaskRepository(),
    private val mechanicId: String = "m1"
) : ViewModel() {

    private val _uiState = MutableStateFlow(MechanicWorkflowUiState(isLoading = true))
    val uiState: StateFlow<MechanicWorkflowUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getTasksForMechanic(mechanicId).collect { tasks ->
                _uiState.update { it.copy(tasks = tasks, isLoading = false) }
            }
        }
    }

    fun toggleTask(taskId: String) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(taskId)
        }
    }

    fun onNoteInputChange(value: String) {
        _uiState.update { it.copy(noteInput = value) }
    }

    fun submitNote(taskId: String) {
        val note = _uiState.value.noteInput.trim()
        if (note.isEmpty()) return
        viewModelScope.launch {
            repository.addNoteToTask(taskId, note)
            _uiState.update { it.copy(noteInput = "") }
        }
    }

    fun selectTask(taskId: String?) {
        _uiState.update { it.copy(selectedTaskId = taskId) }
    }
}
