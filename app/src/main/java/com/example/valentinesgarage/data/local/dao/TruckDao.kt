package com.example.valentinesgarage.data.local.dao

import androidx.room.*
import com.example.valentinesgarage.data.local.entity.Truck
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckDao {

    @Query("SELECT * FROM trucks")
    fun getAllTrucks(): Flow<List<Truck>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTruck(truck: Truck)

    @Update
    suspend fun updateTruck(truck: Truck)

    @Delete
    suspend fun deleteTruck(truck: Truck)
}