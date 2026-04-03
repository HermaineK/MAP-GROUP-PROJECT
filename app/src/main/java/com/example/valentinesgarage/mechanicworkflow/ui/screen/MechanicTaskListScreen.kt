package com.example.valentinesgarage.mechanicworkflow.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.valentinesgarage.mechanicworkflow.data.model.RepairTask
import com.example.valentinesgarage.mechanicworkflow.ui.viewmodel.MechanicWorkflowViewModel

@Composable
fun MechanicTaskListScreen(
    viewModel: MechanicWorkflowViewModel,
    onTaskClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.tasks, key = { it.id }) { task ->
            TaskCard(
                task = task,
                onToggle = { viewModel.toggleTask(task.id) },
                onClick = { onTaskClick(task.id) }
            )
        }
    }
}

@Composable
private fun TaskCard(
    task: RepairTask,
    onToggle: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isCompleted, onCheckedChange = { onToggle() })
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(text = task.description, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "Truck: ${task.truckId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (task.notes.isNotEmpty()) {
                Badge { Text("${task.notes.size}") }
            }
        }
    }
}
