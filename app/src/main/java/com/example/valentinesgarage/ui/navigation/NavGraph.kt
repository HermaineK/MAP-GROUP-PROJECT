package com.example.valentinesgarage.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.valentinesgarage.ui.checkin.CheckInScreen
import com.example.valentinesgarage.ui.home.HomeScreen
import com.example.valentinesgarage.ui.repair.RepairScreen
import com.example.valentinesgarage.ui.report.ReportScreen
import com.example.valentinesgarage.ui.truckdetail.TruckDetailScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    fun goHome() {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onGoToCheckIn = {
                    navController.navigate(Screen.CheckIn.route)
                },
                onGoToRepairs = {
                    navController.navigate(Screen.CheckIn.route)
                },
                onGoToReports = {
                    navController.navigate(Screen.Report.route)
                }
            )
        }

        composable(Screen.CheckIn.route) {
            CheckInScreen(
                onGoToRepair = { truckId ->
                    navController.navigate(Screen.TruckDetail.createRoute(truckId))
                },
                onGoHome = {
                    goHome()
                }
            )
        }

        composable(
            route = Screen.TruckDetail.route,
            arguments = listOf(
                navArgument("truckId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val truckId = backStackEntry.arguments?.getInt("truckId") ?: 1

            TruckDetailScreen(
                truckId = truckId,
                onGoToRepair = {
                    navController.navigate(Screen.Repair.createRoute(it))
                },
                onGoHome = {
                    goHome()
                }
            )
        }

        composable(
            route = Screen.Repair.route,
            arguments = listOf(
                navArgument("truckId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val truckId = backStackEntry.arguments?.getInt("truckId") ?: 1

            RepairScreen(
                truckId = truckId,
                onGoHome = {
                    goHome()
                }
            )
        }

        composable(Screen.Report.route) {
            ReportScreen(
                onGoHome = {
                    goHome()
                }
            )
        }
    }
}