package com.valentinegarage.ui.reports

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.valentinegarage.models.CheckIn
import com.valentinegarage.models.RepairTask
import com.valentinegarage.viewmodel.FilterMode
import com.valentinegarage.viewmodel.ReportsUiState
import com.valentinegarage.viewmodel.ReportsViewModel
import java.text.SimpleDateFormat
import java.util.*

// ── Colour palette ────────────────────────────────────────────────────────────
private val GarageBlue   = Color(0xFF1A3C5E)
private val AccentOrange = Color(0xFFE8741A)
private val CardBg       = Color(0xFFF5F7FA)
private val GreenDone    = Color(0xFF2E7D32)
private val RedPending   = Color(0xFFC62828)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(vm: ReportsViewModel = viewModel()) {

    val uiState      by vm.uiState.collectAsStateWithLifecycle()
    val selectedTab  by vm.selectedTab.collectAsStateWithLifecycle()
    val filterMode   by vm.filterMode.collectAsStateWithLifecycle()
    val selectedFilter by vm.selectedFilter.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Valentine's Garage", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Reports & Insights", fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GarageBlue,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { vm.loadReports() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh",
                            tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(Color(0xFFF0F2F5))
        ) {

            // ── Tab Row ───────────────────────────────────────────────────────
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = GarageBlue,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = AccentOrange,
                        height = 3.dp
                    )
                }
            ) {
                Tab(selected = selectedTab == 0, onClick = { vm.setTab(0) },
                    text = { Text("Repair Tasks") })
                Tab(selected = selectedTab == 1, onClick = { vm.setTab(1) },
                    text = { Text("Check-In History") })
            }

            // ── Filter bar ────────────────────────────────────────────────────
            if (uiState is ReportsUiState.Success) {
                val state = uiState as ReportsUiState.Success
                FilterBar(
                    employeeNames = state.employeeNames,
                    truckPlates   = state.truckPlates,
                    filterMode    = filterMode,
                    selectedFilter = selectedFilter,
                    onFilterEmployee = { vm.applyFilter(FilterMode.BY_EMPLOYEE, it) },
                    onFilterTruck    = { vm.applyFilter(FilterMode.BY_TRUCK, it) },
                    onClearFilter    = { vm.clearFilter() }
                )
            }

            // ── Content ───────────────────────────────────────────────────────
            when (val state = uiState) {
                is ReportsUiState.Loading -> LoadingView()
                is ReportsUiState.Error   -> ErrorView(state.message) { vm.loadReports() }
                is ReportsUiState.Success -> {
                    if (selectedTab == 0) {
                        RepairTasksList(tasks = state.tasks)
                    } else {
                        CheckInList(checkIns = state.checkIns)
                    }
                }
            }
        }
    }
}

// ── Filter Bar ────────────────────────────────────────────────────────────────
@Composable
fun FilterBar(
    employeeNames: List<String>,
    truckPlates: List<String>,
    filterMode: FilterMode,
    selectedFilter: String,
    onFilterEmployee: (String) -> Unit,
    onFilterTruck: (String) -> Unit,
    onClearFilter: () -> Unit
) {
    var showEmployeeMenu by remember { mutableStateOf(false) }
    var showTruckMenu    by remember { mutableStateOf(false) }

    Surface(shadowElevation = 2.dp, color = Color.White) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Filter:", fontWeight = FontWeight.Medium, color = GarageBlue,
                modifier = Modifier.padding(end = 4.dp))

            // Employee dropdown
            Box {
                FilterChip(
                    selected = filterMode == FilterMode.BY_EMPLOYEE,
                    onClick = { showEmployeeMenu = true },
                    label = {
                        Text(if (filterMode == FilterMode.BY_EMPLOYEE) selectedFilter else "Employee")
                    },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
                DropdownMenu(expanded = showEmployeeMenu,
                    onDismissRequest = { showEmployeeMenu = false }) {
                    employeeNames.forEach { name ->
                        DropdownMenuItem(text = { Text(name) }, onClick = {
                            onFilterEmployee(name); showEmployeeMenu = false
                        })
                    }
                }
            }

            // Truck dropdown
            Box {
                FilterChip(
                    selected = filterMode == FilterMode.BY_TRUCK,
                    onClick = { showTruckMenu = true },
                    label = {
                        Text(if (filterMode == FilterMode.BY_TRUCK) selectedFilter else "Vehicle")
                    },
                    leadingIcon = { Icon(Icons.Default.DirectionsCar, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
                DropdownMenu(expanded = showTruckMenu,
                    onDismissRequest = { showTruckMenu = false }) {
                    truckPlates.forEach { plate ->
                        DropdownMenuItem(text = { Text(plate) }, onClick = {
                            onFilterTruck(plate); showTruckMenu = false
                        })
                    }
                }
            }

            if (filterMode != FilterMode.NONE) {
                TextButton(onClick = onClearFilter) {
                    Text("Clear", color = AccentOrange)
                }
            }
        }
    }
}

// ── Repair Tasks List ─────────────────────────────────────────────────────────
@Composable
fun RepairTasksList(tasks: List<RepairTask>) {
    if (tasks.isEmpty()) {
        EmptyView("No repair tasks found")
        return
    }

    // Group tasks by employee
    val grouped = tasks.groupBy { it.completedBy.ifBlank { "Unassigned" } }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        grouped.forEach { (employee, employeeTasks) ->
            item {
                EmployeeTaskGroup(employeeName = employee, tasks = employeeTasks)
            }
        }

        // Summary card at the bottom
        item {
            SummaryCard(
                totalTasks = tasks.size,
                completedTasks = tasks.count { it.isCompleted }
            )
        }
    }
}

@Composable
fun EmployeeTaskGroup(employeeName: String, tasks: List<RepairTask>) {
    val completedCount = tasks.count { it.isCompleted }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Employee header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GarageBlue.copy(alpha = 0.1f),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = null,
                            tint = GarageBlue, modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(employeeName, fontWeight = FontWeight.Bold,
                        fontSize = 16.sp, color = GarageBlue)
                    Text("$completedCount / ${tasks.size} tasks completed",
                        fontSize = 12.sp, color = Color.Gray)
                }
                // Progress pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (completedCount == tasks.size) GreenDone.copy(alpha = 0.15f)
                            else AccentOrange.copy(alpha = 0.15f)
                ) {
                    Text(
                        "${(completedCount.toFloat() / tasks.size * 100).toInt()}%",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = if (completedCount == tasks.size) GreenDone else AccentOrange,
                        fontWeight = FontWeight.Bold, fontSize = 13.sp
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))

            // Task rows
            tasks.forEach { task ->
                TaskRow(task = task)
                if (task != tasks.last()) {
                    Divider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

@Composable
fun TaskRow(task: RepairTask) {
    val fmt = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (task.isCompleted) GreenDone else Color.LightGray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(task.taskName, fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textDecoration = if (task.isCompleted)
                    androidx.compose.ui.text.style.TextDecoration.LineThrough else null,
                color = if (task.isCompleted) Color.Gray else Color.Black
            )
            if (task.notes.isNotBlank()) {
                Text("📝 ${task.notes}", fontSize = 12.sp, color = Color.Gray,
                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp))
            }
            Row(modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (task.truckPlate.isNotBlank()) {
                    InfoChip(icon = Icons.Default.DirectionsCar, text = task.truckPlate)
                }
                if (task.isCompleted && task.completedAt != null) {
                    InfoChip(
                        icon = Icons.Default.Schedule,
                        text = fmt.format(task.completedAt.toDate())
                    )
                }
            }
        }
    }
}

@Composable
fun InfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = CardBg
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(10.dp),
                tint = Color.Gray)
            Text(text, fontSize = 10.sp, color = Color.Gray)
        }
    }
}

// ── Check-In List ─────────────────────────────────────────────────────────────
@Composable
fun CheckInList(checkIns: List<CheckIn>) {
    if (checkIns.isEmpty()) {
        EmptyView("No check-in records found")
        return
    }

    val fmt = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(checkIns.sortedByDescending { it.checkedInAt.seconds }) { checkIn ->
            CheckInCard(checkIn = checkIn, fmt = fmt)
        }
    }
}

@Composable
fun CheckInCard(checkIn: CheckIn, fmt: SimpleDateFormat) {
    val conditionColor = when (checkIn.conditionRating) {
        5    -> GreenDone
        4    -> Color(0xFF66BB6A)
        3    -> Color(0xFFFFA726)
        2    -> Color(0xFFEF5350)
        else -> RedPending
    }
    val conditionLabel = when (checkIn.conditionRating) {
        5 -> "Excellent"; 4 -> "Good"; 3 -> "Fair"; 2 -> "Poor"; else -> "Critical"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            // Condition rating circle
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = conditionColor.copy(alpha = 0.15f),
                modifier = Modifier.size(52.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "${checkIn.conditionRating}/5",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = conditionColor
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(checkIn.truckPlate.ifBlank { "Unknown Truck" },
                    fontWeight = FontWeight.Bold, fontSize = 16.sp, color = GarageBlue)
                Text(fmt.format(checkIn.checkedInAt.toDate()),
                    fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoChip(Icons.Default.Speed, "${checkIn.kilometersDriven} km")
                    InfoChip(Icons.Default.Build, conditionLabel)
                }
                if (checkIn.conditionDescription.isNotBlank()) {
                    Text(checkIn.conditionDescription,
                        fontSize = 12.sp, color = Color.DarkGray,
                        modifier = Modifier.padding(top = 6.dp),
                        maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

// ── Summary Card ──────────────────────────────────────────────────────────────
@Composable
fun SummaryCard(totalTasks: Int, completedTasks: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GarageBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("📊 Summary", fontWeight = FontWeight.Bold,
                color = Color.White, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                SummaryItem(label = "Total Tasks", value = "$totalTasks")
                SummaryItem(label = "Completed",   value = "$completedTasks", color = Color(0xFF81C784))
                SummaryItem(label = "Pending",
                    value = "${totalTasks - completedTasks}",
                    color = Color(0xFFFFB74D))
                SummaryItem(
                    label = "Rate",
                    value = if (totalTasks > 0)
                        "${(completedTasks.toFloat() / totalTasks * 100).toInt()}%" else "0%",
                    color = Color(0xFF4FC3F7)
                )
            }
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String, color: Color = Color.White) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = color)
        Text(label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────
@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = GarageBlue)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Loading reports...", color = Color.Gray)
        }
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)) {
            Icon(Icons.Default.ErrorOutline, contentDescription = null,
                tint = RedPending, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text("Failed to load reports", fontWeight = FontWeight.Bold)
            Text(message, fontSize = 12.sp, color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = GarageBlue)) {
                Text("Try Again")
            }
        }
    }
}

@Composable
fun EmptyView(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.SearchOff, contentDescription = null,
                tint = Color.LightGray, modifier = Modifier.size(52.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(message, color = Color.Gray)
        }
    }
}
