package com.example.valentinesgarage.data.local.dao

import androidx.room.*
import com.example.valentinesgarage.data.local.entity.RepairTask
import kotlinx.coroutines.flow.Flow

@Dao
interface RepairTaskDao {

    @Query("SELECT * FROM repair_tasks WHERE truckId = :truckId")
    fun getTasksForTruck(truckId: Int): Flow<List<RepairTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: RepairTask)

    @Update
    suspend fun updateTask(task: RepairTask)

    @Delete
    suspend fun deleteTask(task: RepairTask)
}