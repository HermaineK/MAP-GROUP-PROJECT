package com.example.valentinesgarage.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "repair_tasks",
    foreignKeys = [
        ForeignKey(
            entity = Truck::class,
            parentColumns = ["id"],
            childColumns = ["truckId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
            indices = [Index("truckId")] // <-- add this line
)
data class RepairTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val truckId: Int,
    val task: String,
    val completed: Boolean = false,
    val notes: String? = null
)