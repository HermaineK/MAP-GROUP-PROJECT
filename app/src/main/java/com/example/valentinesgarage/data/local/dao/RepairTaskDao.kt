package com.example.valentinesgarage.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.valentinesgarage.data.local.entity.RepairTask
import kotlinx.coroutines.flow.Flow

@Dao
interface RepairTaskDao {

    @Insert
    suspend fun insertRepairTask(task: RepairTask)

    @Query("SELECT * FROM repair_tasks ORDER BY id DESC")
    fun getAllRepairTasks(): Flow<List<RepairTask>>

    @Query("SELECT * FROM repair_tasks WHERE truckId = :truckId ORDER BY id DESC")
    fun getTasksForTruck(truckId: Int): Flow<List<RepairTask>>

    @Query("SELECT * FROM repair_tasks WHERE truckId = :truckId")
    suspend fun getTasksForTruckOnce(truckId: Int): List<RepairTask>

    @Update
    suspend fun updateRepairTask(task: RepairTask)

    @Delete
    suspend fun deleteRepairTask(task: RepairTask)
}