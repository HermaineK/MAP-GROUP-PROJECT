package com.example.valentinesgarage.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CheckIn : Screen("checkin")
    object TruckDetail : Screen("truck_detail/{truckId}") {
        fun createRoute(truckId: Int) = "truck_detail/$truckId"
    }
    object Repair : Screen("repair/{truckId}") {
        fun createRoute(truckId: Int) = "repair/$truckId"
    }
    object Report : Screen("report")
}