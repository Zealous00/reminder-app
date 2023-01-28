package com.fekete.david.reminderapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fekete.david.reminderapp.ui.home.HomeScreen
import com.fekete.david.reminderapp.ui.login.LoginScreen
import com.fekete.david.reminderapp.ui.profile.ProfileScreen

@Composable
fun ReminderApp(
    appState: ReminderAppState = rememberReminderAppState(),
    modifier: Modifier
) {
    NavHost(
        navController = appState.navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            LoginScreen(modifier = modifier, navController = appState.navController)
//            ProfileScreen(modifier = modifier, onBackPress = { appState.navigateBack() })
        }
        composable(route = "home") {
            HomeScreen(modifier = modifier, navController = appState.navController)
        }
        composable(route = "profile") {
            ProfileScreen(modifier = modifier, onBackPress = { appState.navigateBack() })
        }
        /*
        TODO: - main screen layout
              - checking password
        * */

    }
}