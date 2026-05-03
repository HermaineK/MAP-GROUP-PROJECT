package com.example.valentinesgarage

import com.example.valentinesgarage.data.local.entity.RepairTask
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class GarageModelTest {

    @Test
    fun repairTaskStoresMechanicAndStatus() {
        val task = RepairTask(
            id = 1,
            truckId = 1,
            description = "Brake check",
            assignedMechanicId = null,
            mechanicName = "Sarah",
            isCompleted = false,
            notes = "Pads worn"
        )

        assertEquals("Sarah", task.mechanicName)
        assertFalse(task.isCompleted)

        val completedTask = task.copy(isCompleted = true)

        assertTrue(completedTask.isCompleted)
    }
}