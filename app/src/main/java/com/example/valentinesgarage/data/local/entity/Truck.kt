package com.example.valentinesgarage.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trucks")
data class Truck(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val licensePlate: String,
    val kilometers: Int,
    val condition: String,
    val notes: String = "",

    val ownerName: String,
    val ownerId: String,
    val phoneNumber: String,

    val status: JobStatus = JobStatus.CREATED
)

enum class JobStatus {
    CREATED,
    IN_PROGRESS,
    COMPLETED,
    CLOSED
}