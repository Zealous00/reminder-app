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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.fekete.david.reminderapp.data.entitiy.Priority
import com.fekete.david.reminderapp.data.entitiy.Reminder
import com.fekete.david.reminderapp.repository.StorageRepository
import com.fekete.david.reminderapp.ui.login.shortToast
import com.fekete.david.reminderapp.viewmodel.ReminderStatus
import com.fekete.david.reminderapp.viewmodel.ReminderViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@Composable
fun CreateReminderScreen(
    navController: NavHostController,
    context: Context,
    onBackPress: () -> Unit
) {
    Surface() {
        Column(
        ) {
            ReminderTopBar(onBackPress = onBackPress)
            ReminderCreationPart(
                context = context,
                navController = navController,
                onBackPress = onBackPress
            )
        }
    }
}

@Composable
fun ReminderCreationPart(
    context: Context,
    navController: NavHostController,
    onBackPress: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val reminderViewModel = ReminderViewModel(StorageRepository())

    calendar.time = Date()

    val reminderMessage = remember { mutableStateOf("") }
    val reminderDate = remember { mutableStateOf("") }
//    val reminderDate by remember { mutableStateOf(LocalDate.now()) }
//    val formattedReminderDate by remember {
//        derivedStateOf { DateTimeFormatter.ofPattern("MMMM dd yyyy").format(reminderDate) }
//    }
    val reminderTime = remember { mutableStateOf("") }
//    val reminderTime = remember { mutableStateOf(LocalTime.NOON) }
//    val formattedReminderTime by remember {
//        derivedStateOf { DateTimeFormatter.ofPattern("hh:mm").format(reminderTime) }
//    }
    val locationX = remember { mutableStateOf("") }
    val locationY = remember { mutableStateOf("") }
    val priority = remember { mutableStateOf(Priority.MEDIUM) }

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

    val scope = CoroutineScope(Dispatchers.Main)

//Message, location_x, location_y, reminder_time, creation_time, creator_id, reminder_seen

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create reminder",
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
                Text(text = "Select reminder date", color = Color.White)
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
                Text(text = "Select Time", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            var timeWithoutSeconds = ""
            if (reminderTime.value != "") {
                timeWithoutSeconds = reminderTime.value.substring(0, reminderTime.value.length - 3)
            }

            Text(
                text = "Selected Time: $timeWithoutSeconds",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Select priority:")
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
                        val newReminder = Reminder(
                            message = reminderMessage.value,
                            locationX = "locationX",
                            locationY = "locationY",
                            reminderTime = formatter.parse(reminderDate.value + " " + reminderTime.value) as Date,
                            creationTime = Timestamp(Date().time),
                            userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty(),
                            reminderSeen = false,
                            priority = priority.value
                        )
                        scope.launch {
                            val result = reminderViewModel.addReminder(
                                newReminder
                            )
                            when (result.first) {
                                ReminderStatus.Successful -> {
                                    val uid = result.second
                                    val reminderData = Data.Builder()
                                        .putString("id", uid)
                                        .putString("message", newReminder.message)
                                        .putString("locationX", newReminder.locationX)
                                        .putString("locationY", newReminder.locationY)
                                        .putLong("reminderTime", newReminder.reminderTime.time)
                                        .putLong("creationTime", newReminder.creationTime.time)
                                        .putString("userId", newReminder.userId)
                                        .putBoolean("reminderSeen", newReminder.reminderSeen)
                                        .putString("priority", newReminder.priority.name)
                                        .build()
                                    val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                                        .setInputData(reminderData)
                                        .build()
                                    WorkManager.getInstance(context).enqueue(workRequest)
                                }
                            }
                        }


                        shortToast(context, "Reminder added successfully!")
                        Thread.sleep(200L)
                        navController.navigate("home")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(corner = CornerSize(50.dp))
            ) {
                Text(text = "Create reminder")
            }
        }

    }

}

@Composable
fun ReminderTopBar(onBackPress: () -> Unit) {
    TopAppBar() {
        IconButton(
            onClick = onBackPress
        ) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Default.ArrowBack),
                contentDescription = ""
            )
        }
        Text(text = "Create reminder")
    }
}

