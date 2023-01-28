package com.fekete.david.reminderapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class ReminderAppState(
    val navController: NavHostController
) {
    fun navigateBack() {
        navController.popBackStack()
    }
}

@Composable
fun rememberReminderAppState(
    navController: NavHostController = rememberNavController()
) = remember(navController) {
    ReminderAppState(navController)
}