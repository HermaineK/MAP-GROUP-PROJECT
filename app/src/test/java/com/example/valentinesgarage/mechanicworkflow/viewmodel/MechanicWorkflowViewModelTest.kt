package com.example.valentinesgarage.mechanicworkflow.viewmodel

import com.example.valentinesgarage.mechanicworkflow.data.repository.FakeMechanicTaskRepository
import com.example.valentinesgarage.mechanicworkflow.ui.viewmodel.MechanicWorkflowViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MechanicWorkflowViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeMechanicTaskRepository
    private lateinit var viewModel: MechanicWorkflowViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeMechanicTaskRepository()
        viewModel = MechanicWorkflowViewModel(repository, mechanicId = "m1")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `tasks load for mechanic on init`() = runTest {
        advanceUntilIdle()
        val tasks = viewModel.uiState.value.tasks
        assertTrue(tasks.isNotEmpty())
        assertTrue(tasks.all { it.assignedMechanicId == "m1" })
    }

    @Test
    fun `toggleTask flips completion status`() = runTest {
        advanceUntilIdle()
        val taskId = viewModel.uiState.value.tasks.first().id
        val before = viewModel.uiState.value.tasks.first().isCompleted

        viewModel.toggleTask(taskId)
        advanceUntilIdle()

        val after = viewModel.uiState.value.tasks.first { it.id == taskId }.isCompleted
        assertEquals(!before, after)
    }

    @Test
    fun `toggleTask twice restores original state`() = runTest {
        advanceUntilIdle()
        val taskId = viewModel.uiState.value.tasks.first().id
        val before = viewModel.uiState.value.tasks.first().isCompleted

        viewModel.toggleTask(taskId)
        advanceUntilIdle()
        viewModel.toggleTask(taskId)
        advanceUntilIdle()

        val restored = viewModel.uiState.value.tasks.first { it.id == taskId }.isCompleted
        assertEquals(before, restored)
    }

    @Test
    fun `submitNote appends note to task`() = runTest {
        advanceUntilIdle()
        val taskId = viewModel.uiState.value.tasks.first().id

        viewModel.onNoteInputChange("Checked oil level")
        viewModel.submitNote(taskId)
        advanceUntilIdle()

        val task = viewModel.uiState.value.tasks.first { it.id == taskId }
        assertTrue(task.notes.any { it.content == "Checked oil level" })
    }

    @Test
    fun `submitNote clears noteInput after submission`() = runTest {
        advanceUntilIdle()
        val taskId = viewModel.uiState.value.tasks.first().id

        viewModel.onNoteInputChange("Some note")
        viewModel.submitNote(taskId)
        advanceUntilIdle()

        assertEquals("", viewModel.uiState.value.noteInput)
    }

    @Test
    fun `submitNote with blank input does nothing`() = runTest {
        advanceUntilIdle()
        val taskId = viewModel.uiState.value.tasks.first().id

        viewModel.onNoteInputChange("   ")
        viewModel.submitNote(taskId)
        advanceUntilIdle()

        val task = viewModel.uiState.value.tasks.first { it.id == taskId }
        assertTrue(task.notes.isEmpty())
    }
}
