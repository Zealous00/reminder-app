package com.fekete.david.reminderapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fekete.david.reminderapp.ui.home.HomeScreen
import com.fekete.david.reminderapp.ui.login.LoginScreen

@Composable
fun ReminderApp(
    appState: ReminderAppState = rememberReminderAppState()
) {
    NavHost(
        navController = appState.navController,
        startDestination = "login"
    ){
        composable(route = "login"){
            LoginScreen(navController = appState.navController, modifier = Modifier.fillMaxSize())
        }
        composable(route = "home"){
            HomeScreen(navController = appState.navController, modifier = Modifier.fillMaxSize())
        }
    }
}