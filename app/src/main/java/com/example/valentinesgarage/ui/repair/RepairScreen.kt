package com.example.valentinesgarage.ui.repair

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.valentinesgarage.ui.checkin.CheckInViewModel

@Composable
fun RepairScreen(
    viewModel: CheckInViewModel = hiltViewModel()
) {
    val trucks by viewModel.trucks.collectAsState()

    // Your UI here using `trucks` list
}