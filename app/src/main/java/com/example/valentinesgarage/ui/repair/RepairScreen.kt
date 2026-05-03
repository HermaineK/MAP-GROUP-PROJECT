package com.example.valentinesgarage.ui.repair

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.valentinesgarage.data.local.entity.RepairTask
import com.example.valentinesgarage.ui.components.QuickHomeButton
import kotlinx.coroutines.launch

private val RepairBg = Color(0xFFF5F7FB)
private val RepairBlueEnd = Color(0xFF2563EB)
private val RepairOrange = Color(0xFFFF8C42)
private val RepairGreen = Color(0xFF10B981)
private val RepairPurple = Color(0xFF7C3AED)

enum class TaskFilter {
    ALL,
    PENDING,
    COMPLETED
}

@Composable
fun RepairScreen(
    truckId: Int = 1,
    onGoHome: () -> Unit,
    viewModel: RepairViewModel = hiltViewModel()
) {
    LaunchedEffect(truckId) {
        viewModel.setTruckId(truckId)
    }

    val tasks by viewModel.tasks.collectAsState()

    var selectedFilter by remember { mutableStateOf(TaskFilter.ALL) }
    var newTaskDescription by remember { mutableStateOf("") }
    var mechanicName by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val filteredTasks = when (selectedFilter) {
        TaskFilter.ALL -> tasks
        TaskFilter.PENDING -> tasks.filter { !it.isCompleted }
        TaskFilter.COMPLETED -> tasks.filter { it.isCompleted }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(RepairBg)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RepairHeaderCard()

            QuickHomeButton(onGoHome = onGoHome)

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Add Repair Task",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Create and manage tasks for the selected truck.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = newTaskDescription,
                        onValueChange = { newTaskDescription = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Task description") },
                        shape = RoundedCornerShape(16.dp)
                    )

                    OutlinedTextField(
                        value = mechanicName,
                        onValueChange = { mechanicName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Mechanic name") },
                        shape = RoundedCornerShape(16.dp)
                    )

                    Button(
                        onClick = {
                            when {
                                newTaskDescription.isBlank() -> {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Task description is required"
                                        )
                                    }
                                }

                                mechanicName.isBlank() -> {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Mechanic name is required"
                                        )
                                    }
                                }

                                else -> {
                                    viewModel.addTask(
                                        description = newTaskDescription,
                                        mechanicName = mechanicName
                                    )

                                    newTaskDescription = ""
                                    mechanicName = ""
                                    selectedFilter = TaskFilter.ALL

                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Repair task added successfully"
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RepairBlueEnd
                        )
                    ) {
                        Text("Add Task")
                    }
                }
            }

            Text(
                text = "Repair Tasks",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TaskFilterButton(
                    text = "All",
                    selected = selectedFilter == TaskFilter.ALL,
                    onClick = { selectedFilter = TaskFilter.ALL },
                    modifier = Modifier.weight(1f)
                )

                TaskFilterButton(
                    text = "Pending",
                    selected = selectedFilter == TaskFilter.PENDING,
                    onClick = { selectedFilter = TaskFilter.PENDING },
                    modifier = Modifier.weight(1f)
                )

                TaskFilterButton(
                    text = "Completed",
                    selected = selectedFilter == TaskFilter.COMPLETED,
                    onClick = { selectedFilter = TaskFilter.COMPLETED },
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Showing ${filteredTasks.size} of ${tasks.size} tasks",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (tasks.isEmpty()) {
                EmptyRepairState()
            } else if (filteredTasks.isEmpty()) {
                EmptyFilteredRepairState(selectedFilter)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    filteredTasks.forEach { task ->
                        RepairTaskDashboardCard(
                            task = task,
                            onToggle = {
                                viewModel.toggleTask(task)

                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = if (task.isCompleted) {
                                            "Task marked as pending"
                                        } else {
                                            "Task marked as completed"
                                        }
                                    )
                                }
                            },
                            onSaveNotes = { updatedNotes ->
                                viewModel.updateNotes(task, updatedNotes)

                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Notes saved successfully"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RepairHeaderCard() {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        brush = Brush.linearGradient(listOf(RepairOrange, RepairBlueEnd)),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "RW",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.size(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Repair Workflow",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Track progress, task completion, mechanic name, and repair notes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(Color(0xFFFFF3EC), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🛠️")
            }
        }
    }
}

@Composable
private fun TaskFilterButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) RepairBlueEnd else Color.White
    val textColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
    val elevation = if (selected) 4.dp else 1.dp

    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun EmptyRepairState() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "No repair tasks yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Add a task above to start the mechanic workflow.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyFilteredRepairState(
    selectedFilter: TaskFilter
) {
    val message = when (selectedFilter) {
        TaskFilter.ALL -> "No repair tasks yet."
        TaskFilter.PENDING -> "No pending repair tasks."
        TaskFilter.COMPLETED -> "No completed repair tasks."
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Change the filter or update task completion status.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RepairTaskDashboardCard(
    task: RepairTask,
    onToggle: () -> Unit,
    onSaveNotes: (String) -> Unit
) {
    var localNotes by remember(task.id) { mutableStateOf(task.notes) }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            if (task.isCompleted) {
                                RepairGreen.copy(alpha = 0.14f)
                            } else {
                                RepairPurple.copy(alpha = 0.14f)
                            },
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (task.isCompleted) "✅" else "🔧")
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Mechanic: ${task.mechanicName.ifBlank { "Not assigned" }}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = if (task.isCompleted) "Completed" else "In Progress",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggle() }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RepairMiniInfoCard(
                    title = "Truck ID",
                    value = task.truckId.toString(),
                    modifier = Modifier.weight(1f),
                    accent = RepairBlueEnd
                )

                RepairMiniInfoCard(
                    title = "Status",
                    value = if (task.isCompleted) "Done" else "Open",
                    modifier = Modifier.weight(1f),
                    accent = if (task.isCompleted) RepairGreen else RepairOrange
                )
            }

            OutlinedTextField(
                value = localNotes,
                onValueChange = { localNotes = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Task notes") },
                shape = RoundedCornerShape(16.dp)
            )

            Button(
                onClick = { onSaveNotes(localNotes) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RepairOrange
                )
            ) {
                Text("Save Notes")
            }
        }
    }
}

@Composable
private fun RepairMiniInfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    accent: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = accent.copy(alpha = 0.10f)
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}