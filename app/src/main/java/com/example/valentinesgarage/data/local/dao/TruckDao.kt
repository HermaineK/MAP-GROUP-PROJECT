package com.example.valentinesgarage.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.valentinesgarage.data.local.entity.Truck
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckDao {

    @Insert
    suspend fun insertTruck(truck: Truck): Long

    @Query("SELECT * FROM trucks ORDER BY id DESC")
    fun getAllTrucks(): Flow<List<Truck>>

    @Query("SELECT * FROM trucks WHERE id = :id")
    suspend fun getTruckById(id: Int): Truck?

    @Update
    suspend fun updateTruck(truck: Truck)
}