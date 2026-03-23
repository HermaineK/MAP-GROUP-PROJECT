package com.example.valentinesgarage.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.valentinesgarage.ui.checkin.CheckInScreen
import com.example.valentinesgarage.ui.repair.RepairScreen
import com.example.valentinesgarage.ui.report.ReportScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "checkin") {

        composable("checkin") {
            CheckInScreen()
        }

        composable("repair") {
            RepairScreen()
        }

        composable("report") {
            ReportScreen()
        }
    }
}