package com.example.valentinesgarage.data.repository

import com.example.valentinesgarage.data.local.dao.EmployeeDao
import com.example.valentinesgarage.data.local.dao.RepairTaskDao
import com.example.valentinesgarage.data.local.dao.TruckDao
import com.example.valentinesgarage.data.local.entity.Employee
import com.example.valentinesgarage.data.local.entity.JobStatus
import com.example.valentinesgarage.data.local.entity.RepairTask
import com.example.valentinesgarage.data.local.entity.Truck
import kotlinx.coroutines.flow.Flow

class GarageRepository(
    private val truckDao: TruckDao,
    private val repairTaskDao: RepairTaskDao,
    private val employeeDao: EmployeeDao
) {

    fun getAllTrucks(): Flow<List<Truck>> {
        return truckDao.getAllTrucks()
    }

    suspend fun getTruckById(id: Int): Truck? {
        return truckDao.getTruckById(id)
    }

    suspend fun addTruck(truck: Truck): Long {
        return truckDao.insertTruck(truck)
    }

    suspend fun updateTruck(truck: Truck) {
        truckDao.updateTruck(truck)
    }

    suspend fun updateTruckStatus(
        truckId: Int,
        status: JobStatus
    ) {
        val truck = truckDao.getTruckById(truckId)

        if (truck != null) {
            truckDao.updateTruck(
                truck.copy(status = status)
            )
        }
    }

    fun getAllRepairTasks(): Flow<List<RepairTask>> {
        return repairTaskDao.getAllRepairTasks()
    }

    fun getTasksForTruck(truckId: Int): Flow<List<RepairTask>> {
        return repairTaskDao.getTasksForTruck(truckId)
    }

    suspend fun getTasksForTruckOnce(truckId: Int): List<RepairTask> {
        return repairTaskDao.getTasksForTruckOnce(truckId)
    }

    suspend fun addRepairTask(task: RepairTask) {
        repairTaskDao.insertRepairTask(task)
    }

    suspend fun updateRepairTask(task: RepairTask) {
        repairTaskDao.updateRepairTask(task)
    }

    suspend fun deleteRepairTask(task: RepairTask) {
        repairTaskDao.deleteRepairTask(task)
    }

    fun getAllEmployees(): Flow<List<Employee>> {
        return employeeDao.getAllEmployees()
    }

    suspend fun addEmployee(employee: Employee) {
        employeeDao.insertEmployee(employee)
    }
}