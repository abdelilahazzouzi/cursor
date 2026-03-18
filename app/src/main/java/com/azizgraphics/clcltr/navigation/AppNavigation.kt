package com.azizgraphics.clcltr.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.azizgraphics.clcltr.ui.screens.calculate.CalculateScreen
import com.azizgraphics.clcltr.ui.screens.history.HistoryScreen
import com.azizgraphics.clcltr.ui.screens.settings.SettingsScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onThemeChanged: (Int) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Calculate.route,
        modifier = modifier
    ) {
        composable(Screen.Calculate.route) {
            CalculateScreen(snackbarHostState = snackbarHostState)
        }
        composable(Screen.History.route) {
            HistoryScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onThemeChanged = onThemeChanged)
        }
    }
}
