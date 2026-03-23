package com.example.valentinesgarage.ui.checkin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.foundation.lazy.items



@Composable
fun CheckInScreen(viewModel: CheckInViewModel = hiltViewModel()) {
    val trucks by viewModel.trucks.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        Button(onClick = { viewModel.addTestTruck() }) {
            Text("Add Test Truck")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Trucks in Repository:", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(trucks) { truck ->
                Text("${truck.licensePlate} - ${truck.condition} - ${truck.kilometers} km")
            }
        }
    }
}