package com.fekete.david.reminderapp.ui.home

import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.os.bundleOf
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.fekete.david.reminderapp.data.entitiy.Reminder
import com.fekete.david.reminderapp.viewmodel.AuthViewModel
import com.fekete.david.reminderapp.viewmodel.ReminderViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Reminders(
    authViewModel: AuthViewModel,
    reminderViewModel: ReminderViewModel,
    navController: NavController
) {

    reminderViewModel.getUserReminders(FirebaseAuth.getInstance().currentUser?.uid)
    val reminderList by reminderViewModel.reminders.collectAsState()


    Column(modifier = Modifier.fillMaxWidth()) {
        if (reminderList == null || reminderList!!.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No reminders to display! Create one!")
            }
        } else {
            RemindersList(list = reminderList!!, navController = navController)
        }

    }
}

@Composable
fun RemindersList(list: List<Reminder>, navController: NavController) {
    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items(list) { item ->
            val serializedReminder = Gson().toJson(item)
            ReminderListItem(
                reminder = item,
                onClick = {
                    navController.navigate(
                        route = "editreminder/$serializedReminder",
                    )
                }
            )
        }
    }
}

@Composable
private fun ReminderListItem(
    reminder: Reminder,
    onClick: () -> Unit
) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }) {
        val (divider, text, date) = createRefs()
        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )
        Text(
            text = reminder.message,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(text) {
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    startMargin = 0.dp,
                    endMargin = 0.dp
                )
                top.linkTo(parent.top, margin = 10.dp)
                width = Dimension.preferredWrapContent
            }
        )
        Text(
            text = reminder.reminderTime.formatToString(),
            maxLines = 1,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.constrainAs(date) {
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    startMargin = 0.dp,
                    endMargin = 0.dp
                )
                top.linkTo(text.bottom, margin = 8.dp)
                bottom.linkTo(parent.bottom, margin = 10.dp)
                width = Dimension.preferredWrapContent
            }
        )
    }
}

private fun Date.formatToString(): String {
    return SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(this)

}