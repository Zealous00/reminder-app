package com.fekete.david.reminderapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fekete.david.reminderapp.R
import com.fekete.david.reminderapp.viewmodel.AuthViewModel
import com.fekete.david.reminderapp.viewmodel.ReminderViewModel

@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    reminderViewModel: ReminderViewModel
) {
//    DisposableEffect(viewModel) {
//        viewModel.addListener()
//        onDispose { viewModel.removeListener() }
//    }
    Surface() {
        HomeContent(
            navController = navController,
            authViewModel = authViewModel,
            reminderViewModel = reminderViewModel
        )
    }
}


@Composable
fun HomeContent(
    navController: NavController,
    authViewModel: AuthViewModel,
    reminderViewModel: ReminderViewModel
) {
    Scaffold(
        modifier = Modifier.padding(20.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("createreminder") },
//                modifier = Modifier.padding(all = 10.dp),
                contentColor = Color.Black,
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val appBarColor = MaterialTheme.colors.surface.copy(alpha = 0.87f)
            HomeAppBar(
                backgroundColor = appBarColor,
                navController = navController,
                authViewModel = authViewModel,
            )
            Reminders(
                authViewModel = authViewModel,
                reminderViewModel = reminderViewModel
            )
        }
    }
}

@Composable
private fun HomeAppBar(
    backgroundColor: Color,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .height(24.dp)
            )
        },
        backgroundColor = backgroundColor,
        actions = {
            IconButton(onClick = { navController.navigate("profile") }) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Filled.Person),
                    contentDescription = stringResource(R.string.account),
                    modifier = Modifier.size(35.dp)
                )
            }

            IconButton(onClick = { authViewModel.signOutFromAccount() }) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Filled.ExitToApp),
                    contentDescription = stringResource(R.string.logout),
                    modifier = Modifier.size(35.dp),
                    tint = Color.Red
                )
            }
        }
    )

}
