package com.example.valentinesgarage.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.valentinesgarage.data.local.dao.EmployeeDao
import com.example.valentinesgarage.data.local.dao.RepairTaskDao
import com.example.valentinesgarage.data.local.dao.TruckDao
import com.example.valentinesgarage.data.local.entity.Employee
import com.example.valentinesgarage.data.local.entity.RepairTask
import com.example.valentinesgarage.data.local.entity.Truck

@Database(
    entities = [Truck::class, Employee::class, RepairTask::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun truckDao(): TruckDao
    abstract fun repairTaskDao(): RepairTaskDao
    abstract fun employeeDao(): EmployeeDao
}