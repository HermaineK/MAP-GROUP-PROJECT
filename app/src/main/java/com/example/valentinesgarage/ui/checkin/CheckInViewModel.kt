package com.example.valentinesgarage.ui.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.repository.GarageRepository
import com.example.valentinesgarage.data.local.entity.Truck
import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckInViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    val trucks: StateFlow<List<Truck>> = repository.getAllTrucks()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addTestTruck() {
        viewModelScope.launch {
            val truck = Truck(
                id = 0, // Room will auto-generate ID
                licensePlate = "TEST-123",
                condition = "Good",
                kilometers = 100
            )
            repository.addTruck(truck)
        }
    }
}