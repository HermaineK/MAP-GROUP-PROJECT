package com.example.valentinesgarage.ui.checkin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.valentinesgarage.data.local.entity.Truck
import com.example.valentinesgarage.ui.components.QuickHomeButton
import kotlinx.coroutines.launch

private val CheckInBg = Color(0xFFF5F7FB)
private val CheckBlueStart = Color(0xFF1DA1F2)
private val CheckBlueEnd = Color(0xFF2563EB)
private val CheckOrange = Color(0xFFFF8C42)
private val CheckGreen = Color(0xFF10B981)

@Composable
fun CheckInScreen(
    onGoToRepair: (Int) -> Unit,
    onGoHome: () -> Unit,
    viewModel: CheckInViewModel = hiltViewModel()
) {
    val trucks by viewModel.trucks.collectAsState()

    var licensePlate by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("") }
    var kilometers by remember { mutableStateOf("") }

    var ownerName by remember { mutableStateOf("") }
    var ownerId by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CheckInBg)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CheckInHeaderCard()

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
                        text = "Truck Check-In",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Capture truck, owner, condition, and kilometre details at arrival.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = licensePlate,
                        onValueChange = { licensePlate = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("License Plate") },
                        shape = RoundedCornerShape(16.dp)
                    )

                    OutlinedTextField(
                        value = ownerName,
                        onValueChange = { ownerName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Owner Name") },
                        shape = RoundedCornerShape(16.dp)
                    )

                    OutlinedTextField(
                        value = ownerId,
                        onValueChange = { ownerId = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Owner ID") },
                        shape = RoundedCornerShape(16.dp)
                    )

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Phone Number") },
                        shape = RoundedCornerShape(16.dp)
                    )

                    OutlinedTextField(
                        value = condition,
                        onValueChange = { condition = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Condition") },
                        shape = RoundedCornerShape(16.dp)
                    )

                    OutlinedTextField(
                        value = kilometers,
                        onValueChange = { kilometers = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Kilometres") },
                        shape = RoundedCornerShape(16.dp)
                    )

                    Button(
                        onClick = {
                            when {
                                licensePlate.isBlank() -> {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("License plate is required")
                                    }
                                }

                                ownerName.isBlank() -> {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Owner name is required")
                                    }
                                }

                                ownerId.isBlank() -> {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Owner ID is required")
                                    }
                                }

                                phoneNumber.isBlank() -> {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Phone number is required")
                                    }
                                }

                                condition.isBlank() -> {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Condition is required")
                                    }
                                }

                                kilometers.isBlank() -> {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Kilometres is required")
                                    }
                                }

                                kilometers.toIntOrNull() == null -> {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Kilometres must be a number")
                                    }
                                }

                                kilometers.toInt() < 0 -> {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Kilometres cannot be negative")
                                    }
                                }

                                else -> {
                                    val km = kilometers.toInt()

                                    viewModel.addTruck(
                                        licensePlate = licensePlate.trim(),
                                        condition = condition.trim(),
                                        kilometers = km,
                                        ownerName = ownerName.trim(),
                                        ownerId = ownerId.trim(),
                                        phoneNumber = phoneNumber.trim()
                                    )

                                    licensePlate = ""
                                    ownerName = ""
                                    ownerId = ""
                                    phoneNumber = ""
                                    condition = ""
                                    kilometers = ""

                                    scope.launch {
                                        snackbarHostState.showSnackbar("Truck checked in successfully")
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CheckBlueEnd
                        )
                    ) {
                        Text("Save Truck")
                    }
                }
            }

            Text(
                text = "Registered Trucks",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (trucks.isEmpty()) {
                EmptyTruckState()
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    trucks.forEach { truck ->
                        TruckDashboardCard(
                            truck = truck,
                            onGoToRepair = { onGoToRepair(truck.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CheckInHeaderCard() {
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
                        brush = Brush.linearGradient(listOf(CheckBlueStart, CheckBlueEnd)),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CI",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Check-In Dashboard",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Add incoming trucks and manage their workflow.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(Color(0xFFEFF4FF), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🚚")
            }
        }
    }
}

@Composable
private fun EmptyTruckState() {
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
                text = "No trucks yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Add a truck above to begin the repair workflow.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TruckDashboardCard(
    truck: Truck,
    onGoToRepair: () -> Unit
) {
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
                        .size(48.dp)
                        .background(
                            CheckOrange.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🚛")
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = truck.licensePlate,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Owner: ${truck.ownerName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Phone: ${truck.phoneNumber}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Condition: ${truck.condition}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoMiniCard(
                    title = "Kilometres",
                    value = truck.kilometers.toString(),
                    modifier = Modifier.weight(1f),
                    accent = CheckBlueStart
                )

                InfoMiniCard(
                    title = "Status",
                    value = truck.status.name.replace("_", " "),
                    modifier = Modifier.weight(1f),
                    accent = CheckGreen
                )
            }

            Button(
                onClick = onGoToRepair,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CheckOrange
                )
            ) {
                Text("Open Repair Tasks")
            }
        }
    }
}

@Composable
private fun InfoMiniCard(
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