package com.example.valentinesgarage.ui.report

import com.example.valentinesgarage.data.local.entity.JobStatus
import com.example.valentinesgarage.data.local.entity.RepairTask
import com.example.valentinesgarage.data.local.entity.Truck
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ReportsUiStateTest {

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun makeTruck(
        id: Int,
        plate: String = "AAA ${id}AA",
        status: JobStatus = JobStatus.CREATED
    ) = Truck(
        id          = id,
        licensePlate = plate,
        kilometers  = 10_000 * id,
        condition   = "Good",
        ownerName   = "Owner $id",
        ownerId     = "OWN$id",
        phoneNumber = "08100000$id",
        status      = status
    )

    private fun makeTask(
        id: Int,
        truckId: Int = 1,
        mechanic: String = "Alice",
        completed: Boolean = false,
        notes: String = ""
    ) = RepairTask(
        id                 = id,
        truckId            = truckId,
        description        = "Task $id",
        assignedMechanicId = null,
        mechanicName       = mechanic,
        isCompleted        = completed,
        notes              = notes
    )

    private fun buildState(
        trucks: List<Truck>    = emptyList(),
        tasks: List<RepairTask> = emptyList(),
        selectedStatus: JobStatus? = null,
        selectedEmployee: String? = null,
        searchQuery: String = ""
    ) = ReportsUiState(
        totalTrucks      = trucks.size,
        totalTasks       = tasks.size,
        completedTasks   = tasks.count { it.isCompleted },
        pendingTasks     = tasks.count { !it.isCompleted },
        trucks           = trucks,
        tasks            = tasks,
        employeeNames    = tasks.map { it.mechanicName }.filter { it.isNotBlank() }.distinct().sorted(),
        selectedEmployee = selectedEmployee,
        selectedStatus   = selectedStatus,
        searchQuery      = searchQuery
    )

    // ── filteredTrucks – status filter ────────────────────────────────────────

    @Test
    fun `filteredTrucks returns all trucks when no status filter is applied`() {
        val state = buildState(
            trucks = listOf(
                makeTruck(1, status = JobStatus.CREATED),
                makeTruck(2, status = JobStatus.COMPLETED)
            )
        )
        assertEquals(2, state.filteredTrucks.size)
    }

    @Test
    fun `filteredTrucks returns only IN_PROGRESS trucks when that status is selected`() {
        val state = buildState(
            trucks = listOf(
                makeTruck(1, status = JobStatus.CREATED),
                makeTruck(2, status = JobStatus.IN_PROGRESS),
                makeTruck(3, status = JobStatus.IN_PROGRESS)
            ),
            selectedStatus = JobStatus.IN_PROGRESS
        )
        assertEquals(2, state.filteredTrucks.size)
        assertTrue(state.filteredTrucks.all { it.status == JobStatus.IN_PROGRESS })
    }

    @Test
    fun `filteredTrucks returns only COMPLETED trucks when that status is selected`() {
        val state = buildState(
            trucks = listOf(
                makeTruck(1, status = JobStatus.IN_PROGRESS),
                makeTruck(2, status = JobStatus.COMPLETED)
            ),
            selectedStatus = JobStatus.COMPLETED
        )
        assertEquals(1, state.filteredTrucks.size)
        assertEquals(JobStatus.COMPLETED, state.filteredTrucks.first().status)
    }

    @Test
    fun `filteredTrucks returns only CLOSED trucks when that status is selected`() {
        val state = buildState(
            trucks         = listOf(makeTruck(1, status = JobStatus.CLOSED), makeTruck(2, status = JobStatus.CREATED)),
            selectedStatus = JobStatus.CLOSED
        )
        assertEquals(1, state.filteredTrucks.size)
    }

    @Test
    fun `filteredTrucks returns empty list when status filter matches nothing`() {
        val state = buildState(
            trucks         = listOf(makeTruck(1, status = JobStatus.CREATED)),
            selectedStatus = JobStatus.CLOSED
        )
        assertTrue(state.filteredTrucks.isEmpty())
    }

    // ── filteredTrucks – licence-plate search ─────────────────────────────────

    @Test
    fun `filteredTrucks matches partial licence plate case-insensitively`() {
        val state = buildState(
            trucks      = listOf(
                makeTruck(1, plate = "N 123 ABC"),
                makeTruck(2, plate = "N 456 XYZ")
            ),
            searchQuery = "abc"
        )
        assertEquals(1, state.filteredTrucks.size)
        assertEquals("N 123 ABC", state.filteredTrucks.first().licensePlate)
    }

    @Test
    fun `filteredTrucks returns all trucks when search query is blank`() {
        val state = buildState(
            trucks      = listOf(makeTruck(1), makeTruck(2)),
            searchQuery = "   "
        )
        // blank (whitespace) should show all
        assertEquals(2, state.filteredTrucks.size)
    }

    @Test
    fun `filteredTrucks applies both status filter and search simultaneously`() {
        val state = buildState(
            trucks = listOf(
                makeTruck(1, plate = "N 100 AA", status = JobStatus.IN_PROGRESS),
                makeTruck(2, plate = "N 200 BB", status = JobStatus.IN_PROGRESS),
                makeTruck(3, plate = "N 100 CC", status = JobStatus.COMPLETED)
            ),
            selectedStatus = JobStatus.IN_PROGRESS,
            searchQuery    = "100"
        )
        assertEquals(1, state.filteredTrucks.size)
        assertEquals("N 100 AA", state.filteredTrucks.first().licensePlate)
    }

    // ── filteredTasks – employee filter ───────────────────────────────────────

    @Test
    fun `filteredTasks returns all tasks when no employee is selected`() {
        val state = buildState(
            tasks = listOf(makeTask(1, mechanic = "Alice"), makeTask(2, mechanic = "Bob"))
        )
        assertEquals(2, state.filteredTasks.size)
    }

    @Test
    fun `filteredTasks returns only tasks for the selected employee`() {
        val state = buildState(
            tasks            = listOf(
                makeTask(1, mechanic = "Alice"),
                makeTask(2, mechanic = "Alice"),
                makeTask(3, mechanic = "Bob")
            ),
            selectedEmployee = "Alice"
        )
        assertEquals(2, state.filteredTasks.size)
        assertTrue(state.filteredTasks.all { it.mechanicName == "Alice" })
    }

    @Test
    fun `filteredTasks returns empty list when selected employee has no tasks`() {
        val state = buildState(
            tasks            = listOf(makeTask(1, mechanic = "Alice")),
            selectedEmployee = "Charlie"
        )
        assertTrue(state.filteredTasks.isEmpty())
    }

    // ── Metric counts ─────────────────────────────────────────────────────────

    @Test
    fun `completedTasks count is correct`() {
        val tasks = listOf(
            makeTask(1, completed = true),
            makeTask(2, completed = true),
            makeTask(3, completed = false)
        )
        val state = buildState(tasks = tasks)
        assertEquals(2, state.completedTasks)
    }

    @Test
    fun `pendingTasks count is correct`() {
        val tasks = listOf(
            makeTask(1, completed = true),
            makeTask(2, completed = false),
            makeTask(3, completed = false)
        )
        val state = buildState(tasks = tasks)
        assertEquals(2, state.pendingTasks)
    }

    @Test
    fun `totalTrucks equals trucks list size`() {
        val state = buildState(trucks = listOf(makeTruck(1), makeTruck(2), makeTruck(3)))
        assertEquals(3, state.totalTrucks)
    }

    @Test
    fun `totalTasks equals tasks list size`() {
        val state = buildState(tasks = listOf(makeTask(1), makeTask(2)))
        assertEquals(2, state.totalTasks)
    }

    // ── employeeNames derivation ───────────────────────────────────────────────

    @Test
    fun `employeeNames excludes blank mechanic names`() {
        val state = buildState(
            tasks = listOf(
                makeTask(1, mechanic = "Alice"),
                makeTask(2, mechanic = ""),
                makeTask(3, mechanic = "Bob")
            )
        )
        assertTrue(state.employeeNames.none { it.isBlank() })
    }

    @Test
    fun `employeeNames contains only distinct names`() {
        val state = buildState(
            tasks = listOf(
                makeTask(1, mechanic = "Alice"),
                makeTask(2, mechanic = "Alice"),
                makeTask(3, mechanic = "Bob")
            )
        )
        assertEquals(2, state.employeeNames.size)
    }

    @Test
    fun `employeeNames is sorted alphabetically`() {
        val state = buildState(
            tasks = listOf(makeTask(1, mechanic = "Zara"), makeTask(2, mechanic = "Alice"))
        )
        assertEquals(listOf("Alice", "Zara"), state.employeeNames)
    }

    // ── Edge cases ────────────────────────────────────────────────────────────

    @Test
    fun `default state has zero totals and empty lists`() {
        val state = ReportsUiState()
        assertEquals(0, state.totalTrucks)
        assertEquals(0, state.totalTasks)
        assertEquals(0, state.completedTasks)
        assertEquals(0, state.pendingTasks)
        assertTrue(state.trucks.isEmpty())
        assertTrue(state.tasks.isEmpty())
        assertNull(state.selectedEmployee)
        assertNull(state.selectedStatus)
        assertEquals("", state.searchQuery)
    }

    @Test
    fun `filteredTrucks is empty when trucks list is empty regardless of filters`() {
        val state = buildState(
            trucks         = emptyList(),
            selectedStatus = JobStatus.IN_PROGRESS,
            searchQuery    = "N 123"
        )
        assertTrue(state.filteredTrucks.isEmpty())
    }

    @Test
    fun `task with notes stores notes correctly`() {
        val task = makeTask(1, notes = "Brake pads worn heavily")
        assertEquals("Brake pads worn heavily", task.notes)
    }

    @Test
    fun `task isCompleted toggle behaviour`() {
        val task      = makeTask(1, completed = false)
        val toggled   = task.copy(isCompleted = true)
        val unToggled = toggled.copy(isCompleted = false)
        assertEquals(false, task.isCompleted)
        assertEquals(true,  toggled.isCompleted)
        assertEquals(false, unToggled.isCompleted)
    }
}
