package com.example.valentinesgarage

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.lifecycle.lifecycleScope
import com.example.valentinesgarage.ui.checkin.CheckInScreen
import com.example.valentinesgarage.ui.theme.ValentinesGarageTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: TestViewModel by viewModels() // Create a simple ViewModel for testing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Compose UI doesn't matter now
            Text("Testing repository...")
        }

        setContent {
            ValentinesGarageTheme {
                CheckInScreen()
            }
        }

        // Launch coroutine to test database
        lifecycleScope.launch {
            // Add a Truck
            viewModel.addTestTruck()

            // Retrieve trucks
            val trucks = viewModel.getAllTrucks().first()
            Log.d("H-TEST", "Trucks in DB: $trucks")

            // Similarly, test Employee and RepairTask
            viewModel.addTestEmployee()
            viewModel.addTestRepairTask()
        }
    }
}