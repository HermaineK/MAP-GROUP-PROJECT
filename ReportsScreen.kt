package com.example.valentinesgarage.ui.report

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.valentinesgarage.data.local.entity.JobStatus
import com.example.valentinesgarage.data.local.entity.RepairTask
import com.example.valentinesgarage.data.local.entity.Truck
import com.example.valentinesgarage.ui.components.QuickHomeButton

// ── Colour palette ────────────────────────────────────────────────────────────

private val ReportsBg          = Color(0xFFF5F7FB)
private val ReportsBlueStart   = Color(0xFF1DA1F2)
private val ReportsBlueEnd     = Color(0xFF2563EB)
private val ReportsOrangeStart = Color(0xFFFFB347)
private val ReportsOrangeEnd   = Color(0xFFFF8C42)
private val ReportsGreenStart  = Color(0xFF34D399)
private val ReportsGreenEnd    = Color(0xFF10B981)
private val ReportsPurpleStart = Color(0xFFA78BFA)
private val ReportsPurpleEnd   = Color(0xFF7C3AED)
private val ReportsRed         = Color(0xFFEF4444)

// ── Entry point ───────────────────────────────────────────────────────────────


@Composable
fun ReportScreen(
    onGoHome: () -> Unit,
    viewModel: ReportsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ReportsBg)
            .verticalScroll(rememberScrollState())
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ── Header ────────────────────────────────────────────────────────────
        ReportsHeaderCard()
        QuickHomeButton(onGoHome = onGoHome)

        // ── Metrics ───────────────────────────────────────────────────────────
        Text(
            text = "Overview",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ReportsMetricCard(
                modifier    = Modifier.weight(1f),
                title       = "Total Trucks",
                value       = state.totalTrucks.toString(),
                subtitle    = "Checked in",
                startColor  = ReportsBlueStart,
                endColor    = ReportsBlueEnd
            )
            ReportsMetricCard(
                modifier    = Modifier.weight(1f),
                title       = "Total Tasks",
                value       = state.totalTasks.toString(),
                subtitle    = "Logged",
                startColor  = ReportsOrangeStart,
                endColor    = ReportsOrangeEnd
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ReportsMetricCard(
                modifier    = Modifier.weight(1f),
                title       = "Completed",
                value       = state.completedTasks.toString(),
                subtitle    = "Finished",
                startColor  = ReportsGreenStart,
                endColor    = ReportsGreenEnd
            )
            ReportsMetricCard(
                modifier    = Modifier.weight(1f),
                title       = "Pending",
                value       = state.pendingTasks.toString(),
                subtitle    = "Open",
                startColor  = ReportsPurpleStart,
                endColor    = ReportsPurpleEnd
            )
        }

        // ── Search ────────────────────────────────────────────────────────────
        OutlinedTextField(
            value         = state.searchQuery,
            onValueChange = viewModel::onSearchQueryChanged,
            modifier      = Modifier.fillMaxWidth(),
            label         = { Text("Search by licence plate") },
            shape         = RoundedCornerShape(16.dp)
        )

        // ── Status filter ─────────────────────────────────────────────────────
        Text(
            text       = "Filter by Job Status",
            style      = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier                = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement   = Arrangement.spacedBy(8.dp)
        ) {
            StatusFilterChip(
                text     = "All",
                selected = state.selectedStatus == null,
                onClick  = { viewModel.onStatusSelected(null) }
            )
            StatusFilterChip(
                text     = "Created",
                selected = state.selectedStatus == JobStatus.CREATED,
                onClick  = { viewModel.onStatusSelected(JobStatus.CREATED) }
            )
            StatusFilterChip(
                text     = "In Progress",
                selected = state.selectedStatus == JobStatus.IN_PROGRESS,
                onClick  = { viewModel.onStatusSelected(JobStatus.IN_PROGRESS) }
            )
            StatusFilterChip(
                text     = "Completed",
                selected = state.selectedStatus == JobStatus.COMPLETED,
                onClick  = { viewModel.onStatusSelected(JobStatus.COMPLETED) }
            )
            StatusFilterChip(
                text     = "Closed",
                selected = state.selectedStatus == JobStatus.CLOSED,
                onClick  = { viewModel.onStatusSelected(JobStatus.CLOSED) }
            )
        }

        // ── Employee filter ───────────────────────────────────────────────────
        AnimatedVisibility(
            visible = state.employeeNames.isNotEmpty(),
            enter   = fadeIn() + expandVertically(),
            exit    = fadeOut() + shrinkVertically()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text       = "Filter by Employee",
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EmployeeFilterChip(
                        name     = "All Staff",
                        selected = state.selectedEmployee == null,
                        onClick  = { viewModel.onEmployeeSelected(null) }
                    )
                    state.employeeNames.forEach { name ->
                        EmployeeFilterChip(
                            name     = name,
                            selected = state.selectedEmployee == name,
                            onClick  = { viewModel.onEmployeeSelected(name) }
                        )
                    }
                }
            }
        }

        // ── Active filter summary ─────────────────────────────────────────────
        val filterActive = state.selectedStatus != null ||
            state.selectedEmployee != null ||
            state.searchQuery.isNotBlank()

        AnimatedVisibility(visible = filterActive) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text  = "Showing ${state.filteredTrucks.size} of ${state.trucks.size} trucks",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text     = "Clear filters",
                    style    = MaterialTheme.typography.bodySmall,
                    color    = ReportsBlueEnd,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { viewModel.clearFilters() }
                )
            }
        }

        // ── Truck reports ─────────────────────────────────────────────────────
        Text(
            text       = "Truck Reports",
            style      = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        when {
            state.trucks.isEmpty()         -> EmptyReportsState()
            state.filteredTrucks.isEmpty() -> EmptyFilteredReportsState()
            else -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                state.filteredTrucks.forEach { truck ->
                    val truckTasks = state.filteredTasks.filter { it.truckId == truck.id }
                    TruckReportCard(truck = truck, tasks = truckTasks)
                }
            }
        }

        // ── Employee work report ──────────────────────────────────────────────
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text       = "Employee Work Report",
            style      = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        val groupedByMechanic = state.filteredTasks
            .filter { it.mechanicName.isNotBlank() }
            .groupBy { it.mechanicName }

        if (groupedByMechanic.isEmpty()) {
            EmptyEmployeeReportCard()
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                groupedByMechanic.forEach { (mechanicName, tasks) ->
                    EmployeeReportCard(mechanicName = mechanicName, tasks = tasks)
                }
            }
        }
    }
}

// ── Private composables ───────────────────────────────────────────────────────

@Composable
private fun ReportsHeaderCard() {
    Card(
        shape     = RoundedCornerShape(24.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier          = Modifier
                    .size(52.dp)
                    .background(
                        brush = Brush.linearGradient(listOf(ReportsGreenStart, ReportsGreenEnd)),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment  = Alignment.Center
            ) {
                Text(text = "RP", color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.size(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = "Reports Dashboard",
                    style      = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text  = "Truck condition history, kilometres, repairs, and employee work.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier         = Modifier
                    .size(38.dp)
                    .background(Color(0xFFEFFAF5), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) { Text(text = "📊") }
        }
    }
}

@Composable
private fun ReportsMetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    startColor: Color,
    endColor: Color
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(22.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(listOf(startColor, endColor)),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = title,   color = Color.White.copy(alpha = 0.95f), style = MaterialTheme.typography.bodyMedium)
                Text(text = value,   color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(text = subtitle, color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


@Composable
private fun StatusFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg   = if (selected) ReportsBlueEnd else Color.White
    val fg   = if (selected) Color.White    else MaterialTheme.colorScheme.onSurface

    Card(
        modifier  = Modifier.clickable(onClick = onClick),
        shape     = RoundedCornerShape(50.dp),
        colors    = CardDefaults.cardColors(containerColor = bg),
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 4.dp else 1.dp)
    ) {
        Text(
            text       = text,
            color      = fg,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            modifier   = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}


@Composable
private fun EmployeeFilterChip(
    name: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (selected) ReportsPurpleEnd else Color.White
    val fg = if (selected) Color.White else MaterialTheme.colorScheme.onSurface

    Card(
        modifier  = Modifier.clickable(onClick = onClick),
        shape     = RoundedCornerShape(50.dp),
        colors    = CardDefaults.cardColors(containerColor = bg),
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 4.dp else 1.dp)
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = "👤", style = MaterialTheme.typography.bodySmall)
            Text(text = name, color = fg,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium)
        }
    }
}

@Composable
private fun EmptyReportsState() {
    Card(
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = "No report data yet", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                text  = "Add trucks and repair tasks to start generating reports.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyFilteredReportsState() {
    Card(
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = "No trucks match your filters", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                text  = "Try clearing the filters or changing your search.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyEmployeeReportCard() {
    Card(
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = "No employee work recorded yet", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                text  = "Add repair tasks with mechanic names to generate employee reports.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TruckReportCard(truck: Truck, tasks: List<RepairTask>) {
    val completedCount = tasks.count { it.isCompleted }
    val pendingCount   = tasks.count { !it.isCompleted }

    Card(
        shape     = RoundedCornerShape(22.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier         = Modifier
                        .size(48.dp)
                        .background(ReportsBlueStart.copy(alpha = 0.14f), shape = RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) { Text("🚛") }

                Spacer(modifier = Modifier.size(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = truck.licensePlate, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(text = "Condition at check-in: ${truck.condition}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "Kilometres at check-in: ${truck.kilometers} km", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "Owner: ${truck.ownerName}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    JobStatusBadge(status = truck.status)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ReportsMiniInfoCard(title = "Tasks",   value = tasks.size.toString(),         modifier = Modifier.weight(1f), accent = ReportsBlueEnd)
                ReportsMiniInfoCard(title = "Done",    value = completedCount.toString(),      modifier = Modifier.weight(1f), accent = ReportsGreenEnd)
                ReportsMiniInfoCard(title = "Pending", value = pendingCount.toString(),        modifier = Modifier.weight(1f), accent = ReportsPurpleEnd)
            }

            if (tasks.isEmpty()) {
                Text(
                    text  = "No repair tasks recorded for this truck yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Repair Task Details", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    tasks.forEach { task -> TaskReportItem(task = task) }
                }
            }
        }
    }
}

@Composable
private fun JobStatusBadge(status: JobStatus) {
    val (label, colour) = when (status) {
        JobStatus.CREATED     -> "Created"     to ReportsBlueEnd
        JobStatus.IN_PROGRESS -> "In Progress" to ReportsOrangeEnd
        JobStatus.COMPLETED   -> "Completed"   to ReportsGreenEnd
        JobStatus.CLOSED      -> "Closed"      to ReportsRed
    }
    Text(text = "Job status: $label", style = MaterialTheme.typography.bodyMedium, color = colour, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun TaskReportItem(task: RepairTask) {
    Card(
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) ReportsGreenEnd.copy(alpha = 0.08f)
                             else ReportsOrangeEnd.copy(alpha = 0.08f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = task.description, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Text(text = "Mechanic: ${task.mechanicName.ifBlank { "Not assigned" }}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Status: ${if (task.isCompleted) "✅ Completed" else "🔧 Pending"}", style = MaterialTheme.typography.bodyMedium)
            if (task.notes.isNotBlank()) {
                Text(text = "Notes: ${task.notes}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun EmployeeReportCard(mechanicName: String, tasks: List<RepairTask>) {
    val completed = tasks.count { it.isCompleted }
    val pending   = tasks.count { !it.isCompleted }

    Card(
        shape     = RoundedCornerShape(22.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier         = Modifier
                        .size(48.dp)
                        .background(ReportsPurpleEnd.copy(alpha = 0.14f), shape = RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) { Text("👤") }

                Spacer(modifier = Modifier.size(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = mechanicName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(text = "Employee repair activity", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ReportsMiniInfoCard(title = "Tasks",   value = tasks.size.toString(), modifier = Modifier.weight(1f), accent = ReportsBlueEnd)
                ReportsMiniInfoCard(title = "Done",    value = completed.toString(),  modifier = Modifier.weight(1f), accent = ReportsGreenEnd)
                ReportsMiniInfoCard(title = "Pending", value = pending.toString(),    modifier = Modifier.weight(1f), accent = ReportsOrangeEnd)
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                tasks.forEach { task ->
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = if (task.isCompleted) "✅" else "🔧")
                        Column {
                            Text(text = task.description, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            if (task.notes.isNotBlank()) {
                                Text(text = task.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportsMiniInfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    accent: Color
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = accent.copy(alpha = 0.10f))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}
