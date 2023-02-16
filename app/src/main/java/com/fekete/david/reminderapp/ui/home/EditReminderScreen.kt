package com.fekete.david.reminderapp.ui.home

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fekete.david.reminderapp.data.entitiy.Priority
import com.fekete.david.reminderapp.data.entitiy.Reminder
import com.fekete.david.reminderapp.repository.StorageRepository
import com.fekete.david.reminderapp.ui.login.shortToast
import com.fekete.david.reminderapp.viewmodel.ReminderViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditReminderScreen(
    context: Context,
    onBackPress: () -> Unit,
    navController: NavHostController,
    reminderViewModel: ReminderViewModel,
//    reminderToEdit: Reminder
) {
    val currentEntry = navController.currentBackStackEntry
    val serializedReminder = currentEntry?.arguments?.get("serializedReminder")

    var reminderToEdit = Reminder()
    if (serializedReminder != null) {
        reminderToEdit = Gson().fromJson(serializedReminder.toString(), Reminder::class.java)
    }
    println(reminderToEdit.id)
    Surface() {
        Column(
        ) {
            EditTopBar(onBackPress = onBackPress)
            ReminderEditionPart(
                context = context,
                reminderToEdit = reminderToEdit,
                navController = navController,
            )
        }
    }
}

@Composable
fun ReminderEditionPart(
    context: Context,
    navController: NavHostController,
    reminderToEdit: Reminder
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val reminderViewModel = ReminderViewModel(StorageRepository())

    calendar.time = Date()

    val reminderMessage = remember { mutableStateOf(reminderToEdit.message) }

    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val reminderDate =
        remember { mutableStateOf(dateFormatter.format(reminderToEdit.reminderTime)) }

    val timeFormatter = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
    val reminderTime =
        remember { mutableStateOf(timeFormatter.format(reminderToEdit.reminderTime)) }

    val locationX = remember { mutableStateOf(reminderToEdit.locationX) }
    val locationY = remember { mutableStateOf(reminderToEdit.locationY) }

    val reminderSeen = remember { mutableStateOf(reminderToEdit.reminderSeen) }
    val priority = remember { mutableStateOf(reminderToEdit.priority) }


    val scope = CoroutineScope(Dispatchers.Main)


    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            var monthString = (month + 1).toString()
            if (month < 11) {
                monthString = "0${month + 1}"
            }
            var dayString = dayOfMonth.toString()
            if (dayOfMonth < 10) {
                dayString = "0$dayOfMonth"
            }
            reminderDate.value = "$year-$monthString-$dayString"
        }, year, month, day
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            var minuteString = minute.toString()
            if (minute < 10) {
                minuteString = "0$minute"
            }
            var hourString = hour.toString()
            if (hour < 10) {
                hourString = "0$hour"
            }
            reminderTime.value = "$hourString:$minuteString:00"
        }, hour, minute, false
    )

//Message, location_x, location_y, reminder_time, creation_time, creator_id, reminder_seen

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .weight(0.9f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Edit reminder",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = reminderMessage.value,
                onValueChange = { text -> reminderMessage.value = text },
                label = { Text(text = "Reminder message") },
                shape = RoundedCornerShape(corner = CornerSize(50.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    datePickerDialog.show()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(corner = CornerSize(50.dp))
            ) {
                Text(text = "Edit reminder date", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Selected Date: ${reminderDate.value}",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { timePickerDialog.show() },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(corner = CornerSize(50.dp))
            ) {
                Text(text = "Edit Time", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            val timeWithoutSeconds = reminderTime.value.substring(0, reminderTime.value.length - 3)
            Text(
                text = "Selected Time: $timeWithoutSeconds",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Edit priority:")
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = priority.value == Priority.LOW,
                    onClick = { priority.value = Priority.LOW },
                )
                Text(text = "Low")
                Spacer(modifier = Modifier.size(20.dp))

                RadioButton(
                    selected = priority.value == Priority.MEDIUM,
                    onClick = { priority.value = Priority.MEDIUM }
                )
                Text(text = "Medium")
                Spacer(modifier = Modifier.size(20.dp))

                RadioButton(
                    selected = priority.value == Priority.HIGH,
                    onClick = { priority.value = Priority.HIGH }
                )
                Text(text = "High")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
                    if (reminderMessage.value.isEmpty()) {
                        shortToast(context, "Please fill out the message!")
                    } else if (reminderTime.value.isEmpty() || reminderDate.value.isEmpty()) {
                        shortToast(context, "Please select date and time!")
                    } else {
                        scope.launch {
                            reminderViewModel.updateReminder(
                                Reminder(
                                    id = reminderToEdit.id,
                                    message = reminderMessage.value,
                                    locationX = locationX.value,
                                    locationY = locationY.value,
                                    reminderTime = formatter.parse(reminderDate.value + " " + reminderTime.value) as Date,
                                    creationTime = reminderToEdit.creationTime,
                                    userId = reminderToEdit.userId,
                                    reminderSeen = reminderSeen.value,
                                    priority = priority.value
                                )
                            )
                        }
                        shortToast(context, "Reminder updated successfully!")

                        navController.navigate("home")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(corner = CornerSize(50.dp))
            ) {
                Text(text = "Save reminder")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .weight(0.1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    scope.launch {
                        reminderViewModel.deleteReminder(reminderToEdit.id)
                    }
                    shortToast(context, "Reminder deleted successfully!")

                    navController.navigate("home")

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(corner = CornerSize(50.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Text(text = "Delete reminder")
            }
        }
    }
}


@Composable
fun EditTopBar(onBackPress: () -> Unit) {
    TopAppBar() {
        IconButton(
            onClick = onBackPress
        ) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Default.ArrowBack),
                contentDescription = ""
            )
        }
        Text(text = "Edit reminder")
    }
}