package com.fekete.david.reminderapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fekete.david.reminderapp.data.entitiy.Reminder
import com.fekete.david.reminderapp.repository.StorageRepository
import com.fekete.david.reminderapp.ui.home.CreateReminderScreen
import com.fekete.david.reminderapp.ui.home.EditReminderScreen
import com.fekete.david.reminderapp.ui.home.HomeScreen
import com.fekete.david.reminderapp.ui.login.LoginScreen
import com.fekete.david.reminderapp.ui.profile.ProfileScreen
import com.fekete.david.reminderapp.ui.registration.RegistrationScreen
import com.fekete.david.reminderapp.viewmodel.AuthViewModel
import com.fekete.david.reminderapp.viewmodel.ProfileViewModel
import com.fekete.david.reminderapp.viewmodel.ReminderViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking

@Composable
fun ReminderApp(
    appState: ReminderAppState = rememberReminderAppState(),
    modifier: Modifier,
    authViewModel: AuthViewModel = AuthViewModel(),
    reminderViewModel: ReminderViewModel = ReminderViewModel(StorageRepository()),
    profileViewModel: ProfileViewModel = ProfileViewModel(StorageRepository())
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    var startlocation = ""
    if (currentUser != null) {
        startlocation = "home"
    } else {
        startlocation = "login"
    }

    NavHost(
        navController = appState.navController,
        startDestination = startlocation
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
            HomeScreen(
                modifier = modifier,
                navController = appState.navController,
                authViewModel = authViewModel,
                reminderViewModel = reminderViewModel
            )
        }
        composable(route = "profile") {
            ProfileScreen(
                modifier = modifier,
                navController = appState.navController,
                onBackPress = { appState.navigateBack() },
                authViewModel = authViewModel,
                profileViewModel = profileViewModel
            )
        }
        composable(route = "registration") {
            RegistrationScreen(
                modifier = modifier,
                navController = appState.navController,
                context = LocalContext.current,
                authViewModel = authViewModel,
                profileViewModel = profileViewModel,
                onBackPress = { appState.navigateBack() }
            )
        }
        composable(route = "createreminder") {
            CreateReminderScreen(
                navController = appState.navController,
                context = LocalContext.current,
                onBackPress = { appState.navigateBack() }
            )
        }
        composable(
            route = "editreminder/{serializedReminder}", arguments = listOf(
                navArgument("serializedReminder") {
                    type = NavType.StringType
                    defaultValue = ""
                })
        ) {
            EditReminderScreen(
                context = LocalContext.current,
                navController = appState.navController,
                reminderViewModel = reminderViewModel,
                onBackPress = { appState.navigateBack() },
            )
        }

    }
}
