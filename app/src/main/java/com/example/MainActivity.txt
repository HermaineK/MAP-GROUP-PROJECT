package com.example.truckcheckin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.truckcheckin.ui.theme.TruckCheckINTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TruckCheckINTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CheckInScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CheckInScreen(modifier: Modifier = Modifier) {

    var condition by remember { mutableStateOf("Select Condition") }
    var expanded by remember { mutableStateOf(false) }

    val conditionOptions = listOf("Good", "Average", "Damaged")
    var kilometers by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Truck Check-In", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(20.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                value = condition,
                onValueChange = {},
                readOnly = true,
                label = { Text("Truck Condition") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                conditionOptions.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            condition = item
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = kilometers,
            onValueChange = { kilometers = it },
            label = { Text("Kilometers") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (condition.isBlank() || kilometers.isBlank()) {
                    message = "❌ Please fill in all fields"
                } else {
                    message = "✅ Truck checked in successfully!"

                    // Optional: clear inputs after success
                    condition = ""
                    kilometers = ""
                }

            },
            modifier = Modifier.width(200.dp)
        ) {
            Text("Check In Truck")
        }
        Spacer(modifier = Modifier.height(20.dp))

        if (message.isNotEmpty()) {
            Text(text = message)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckInScreenPreview() {
    TruckCheckINTheme {
        CheckInScreen()
    }
}