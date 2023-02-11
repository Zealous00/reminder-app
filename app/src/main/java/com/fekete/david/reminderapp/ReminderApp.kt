package com.fekete.david.reminderapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fekete.david.reminderapp.ui.home.HomeScreen
import com.fekete.david.reminderapp.ui.login.LoginScreen
import com.fekete.david.reminderapp.ui.profile.ProfileScreen
import com.fekete.david.reminderapp.ui.registration.RegistrationScreen
import com.fekete.david.reminderapp.viewmodel.AuthViewModel

@Composable
fun ReminderApp(
    appState: ReminderAppState = rememberReminderAppState(),
    modifier: Modifier,
    authViewModel: AuthViewModel = viewModel()
) {
    NavHost(
        navController = appState.navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            LoginScreen(
                modifier = modifier,
                navController = appState.navController,
                context = LocalContext.current,
                authViewModel = authViewModel
            )
//            ProfileScreen(modifier = modifier, onBackPress = { appState.navigateBack() })
        }
        composable(route = "home") {
            HomeScreen(modifier = modifier, navController = appState.navController)
        }
        composable(route = "profile") {
            ProfileScreen(
                modifier = modifier,
                navController = appState.navController,
                onBackPress = { appState.navigateBack() })
        }
        composable(route = "registration") {
            RegistrationScreen(
                modifier = modifier,
                navController = appState.navController,
                context = LocalContext.current,
                authViewModel = authViewModel,
                onBackPress = { appState.navigateBack() }
            )
        }

    }
}
