package com.example.valentinesgarage.mechanicworkflow.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.valentinesgarage.mechanicworkflow.data.model.TaskUpdateNote
import com.example.valentinesgarage.mechanicworkflow.ui.viewmodel.MechanicWorkflowViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: String,
    viewModel: MechanicWorkflowViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val task = state.tasks.find { it.id == taskId } ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("<")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(task.description, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(4.dp))
            Text("Truck: ${task.truckId}", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            FilterChip(
                selected = task.isCompleted,
                onClick = { viewModel.toggleTask(task.id) },
                label = { Text(if (task.isCompleted) "Completed" else "In Progress") }
            )
            Spacer(Modifier.height(16.dp))
            Text("Notes", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(task.notes, key = { it.id }) { note ->
                    NoteItem(note)
                }
            }
            Spacer(Modifier.height(8.dp))
            NoteInputRow(
                value = state.noteInput,
                onValueChange = viewModel::onNoteInputChange,
                onSubmit = { viewModel.submitNote(task.id) }
            )
        }
    }
}

@Composable
private fun NoteItem(note: TaskUpdateNote) {
    val fmt = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault())
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(note.content, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Text(
                fmt.format(Date(note.timestamp)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NoteInputRow(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Add a note...") },
            singleLine = true
        )
        Spacer(Modifier.width(8.dp))
        Button(onClick = onSubmit, enabled = value.isNotBlank()) {
            Text("Add")
        }
    }
}
