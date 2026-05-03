package com.example.valentinesgarage

import androidx.lifecycle.ViewModel
import com.example.valentinesgarage.data.local.entity.Employee
import com.example.valentinesgarage.data.local.entity.RepairTask
import com.example.valentinesgarage.data.local.entity.Truck
import com.example.valentinesgarage.data.repository.GarageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    // Truck
    suspend fun addTestTruck() {
        val truck = Truck(
            licensePlate = "ABC123",
            condition = "Good",
            kilometers = 1000,
            ownerName = "John Doe",
            ownerId = "123456789",
            phoneNumber = "0812345678"
        )
        repository.addTruck(truck)
    }

    fun getAllTrucks(): Flow<List<Truck>> = repository.getAllTrucks()

    // Employee
    suspend fun addTestEmployee() {
        val employee = Employee(
            id = 0,
            name = "John Doe",
            role = "Mechanic"
        )
        repository.addEmployee(employee)
    }

    // RepairTask
    suspend fun addTestRepairTask() {
        val task = RepairTask(
            truckId = 1,
            description = "Oil change",
            assignedMechanicId = null,
            isCompleted = false,
            notes = ""
        )
        repository.addRepairTask(task)
    }
}