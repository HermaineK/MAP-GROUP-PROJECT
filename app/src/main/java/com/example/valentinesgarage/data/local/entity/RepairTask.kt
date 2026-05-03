package com.example.valentinesgarage.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
    indices = [Index("truckId")]
)
data class RepairTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val truckId: Int,

    val description: String,

    val assignedMechanicId: Int? = null,

    val mechanicName: String = "",

    val isCompleted: Boolean = false,

    val notes: String = ""
)