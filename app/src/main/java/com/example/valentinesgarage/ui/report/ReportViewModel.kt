package com.example.valentinesgarage.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.repository.GarageRepository
import com.example.valentinesgarage.data.local.entity.Truck
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    private val _trucks = MutableStateFlow<List<Truck>>(emptyList())
    val trucks: StateFlow<List<Truck>> = _trucks

    fun loadAllData() {
        viewModelScope.launch {
            repository.getAllTrucks().collect {
                _trucks.value = it
            }
        }
    }

    // You can add functions to filter by employee or truck
}