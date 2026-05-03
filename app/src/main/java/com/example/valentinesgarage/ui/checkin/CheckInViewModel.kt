package com.example.valentinesgarage.ui.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.data.local.entity.Truck
import com.example.valentinesgarage.data.repository.GarageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTruck(
        licensePlate: String,
        condition: String,
        kilometers: Int,
        ownerName: String,
        ownerId: String,
        phoneNumber: String
    ) {
        viewModelScope.launch {
            repository.addTruck(
                Truck(
                    licensePlate = licensePlate,
                    condition = condition,
                    kilometers = kilometers,
                    ownerName = ownerName,
                    ownerId = ownerId,
                    phoneNumber = phoneNumber
                )
            )
        }
    }
}
