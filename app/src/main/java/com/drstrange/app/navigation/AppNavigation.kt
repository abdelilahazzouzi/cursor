package com.drstrange.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.drstrange.app.ui.screens.courses.CoursesScreen
import com.drstrange.app.ui.screens.dashboard.DashboardScreen
import com.drstrange.app.ui.screens.progress.ProgressScreen
import com.drstrange.app.ui.screens.simulation.SimulationScreen

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen()
        }
        composable(Screen.Courses.route) {
            CoursesScreen()
        }
        composable(Screen.Simulation.route) {
            SimulationScreen()
        }
        composable(Screen.Progress.route) {
            ProgressScreen()
        }
    }
}
