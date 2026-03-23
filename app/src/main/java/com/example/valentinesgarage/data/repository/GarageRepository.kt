package com.example.valentinesgarage.data.repository

import com.example.valentinesgarage.data.local.dao.EmployeeDao
import com.example.valentinesgarage.data.local.dao.RepairTaskDao
import com.example.valentinesgarage.data.local.dao.TruckDao
import com.example.valentinesgarage.data.local.entity.Employee
import com.example.valentinesgarage.data.local.entity.RepairTask
import com.example.valentinesgarage.data.local.entity.Truck
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GarageRepository @Inject constructor(
    private val truckDao: TruckDao,
    private val repairTaskDao: RepairTaskDao,
    private val employeeDao: EmployeeDao // <-- add this
) {

    // ========================
    // Truck operations
    // ========================

    val trucks = truckDao.getAllTrucks()
    // Truck operations
    fun getAllTrucks(): Flow<List<Truck>> = truckDao.getAllTrucks()
    suspend fun addTruck(truck: Truck) = truckDao.insertTruck(truck)
    suspend fun updateTruck(truck: Truck) = truckDao.updateTruck(truck)
    suspend fun deleteTruck(truck: Truck) = truckDao.deleteTruck(truck)


    // ========================
    // Repair Task operations
    // ========================
    fun getTasksForTruck(truckId: Int): Flow<List<RepairTask>> =
        repairTaskDao.getTasksForTruck(truckId)

    suspend fun addRepairTask(task: RepairTask) = repairTaskDao.insertTask(task)

    suspend fun updateRepairTask(task: RepairTask) = repairTaskDao.updateTask(task)

    suspend fun deleteRepairTask(task: RepairTask) = repairTaskDao.deleteTask(task)

    suspend fun addEmployee(employee: Employee) = employeeDao.insertEmployee(employee)
    suspend fun getEmployees() = employeeDao.getAllEmployees()

}