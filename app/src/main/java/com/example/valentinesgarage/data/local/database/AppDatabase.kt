package com.example.valentinesgarage.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.valentinesgarage.data.local.dao.EmployeeDao
import com.example.valentinesgarage.data.local.dao.TruckDao
import com.example.valentinesgarage.data.local.dao.RepairTaskDao
import com.example.valentinesgarage.data.local.entity.Truck
import com.example.valentinesgarage.data.local.entity.RepairTask
import com.example.valentinesgarage.data.local.entity.Employee

@Database(
    entities = [Truck::class, Employee::class, RepairTask::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun truckDao(): TruckDao
    abstract fun repairTaskDao(): RepairTaskDao
    // You can add EmployeeDao later if needed
    abstract fun employeeDao(): EmployeeDao
}