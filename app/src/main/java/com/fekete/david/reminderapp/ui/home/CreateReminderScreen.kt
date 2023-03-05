package com.fekete.david.reminderapp.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
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
import com.fekete.david.reminderapp.worker.ReminderWorker
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.SimpleDateFormat
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

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )

    var backReminder = Reminder()
    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Reminder>("reminder")
        ?.observe(
            navController.currentBackStackEntry!!
        ) { reminder ->
            backReminder = reminder
        }


    val reminderViewModel = ReminderViewModel(StorageRepository())

    calendar.time = Date()

    val reminderMessage = remember { mutableStateOf(backReminder.message) }


    val reminderDate =
        remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd").format(backReminder.reminderTime)) }

    val reminderTime =
        remember { mutableStateOf(SimpleDateFormat("HH:mm:ss").format(backReminder.reminderTime)) }

    val priority = remember { mutableStateOf(backReminder.priority) }
    val hasNotification = remember { mutableStateOf(backReminder.hasNotification) }

    val locationX = remember { mutableStateOf(backReminder.locationX) }
    val locationY = remember { mutableStateOf(backReminder.locationY) }

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
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = hasNotification.value,
                    onCheckedChange = { hasNotification.value = it }
                )
                Text(text = "Send notification")
                OutlinedButton(
                    onClick = {
                        requestPermission(
                            context = context,
                            permission = Manifest.permission.ACCESS_FINE_LOCATION,
                            requestPermission = { launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }
                        ).apply {
                            val formatter =
                                SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
                            var reminderSeen = true
                            if (hasNotification.value) {
                                reminderSeen = false
                            }
                            val serializedReminder = Gson().toJson(
                                Reminder(
                                    message = reminderMessage.value,
                                    locationX = locationX.value,
                                    locationY = locationY.value,
                                    reminderTime = if (!reminderDate.value.isEmpty() && !reminderTime.value.isEmpty()) formatter.parse(
                                        reminderDate.value + " " + reminderTime.value
                                    ) as Date else Timestamp(Date().time),
                                    creationTime = Timestamp(Date().time),
                                    userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty(),
                                    reminderSeen = reminderSeen,
                                    priority = priority.value,
                                    hasNotification = hasNotification.value
                                )
                            )
                            navController.navigate("reminderlocation/$serializedReminder")
                        }
                    },
                    modifier = Modifier.height(55.dp)
                ) {
                    Text(text = "Location")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
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
                        var reminderSeen = true
                        if (hasNotification.value) {
                            reminderSeen = false
                        }
                        val newReminder = Reminder(
                            message = reminderMessage.value,
                            locationX = locationX.value,
                            locationY = locationY.value,
                            reminderTime = formatter.parse(reminderDate.value + " " + reminderTime.value) as Date,
                            creationTime = Timestamp(Date().time),
                            userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty(),
                            reminderSeen = reminderSeen,
                            priority = priority.value,
                            hasNotification = hasNotification.value
                        )
                        scope.launch {
                            val result = reminderViewModel.addReminder(
                                newReminder
                            )
                            when (result.first) {
                                ReminderStatus.Successful -> {
                                    if (newReminder.hasNotification) {
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
                                            .putBoolean(
                                                "hasNotification",
                                                newReminder.hasNotification
                                            )
                                            .build()
                                        val workRequest =
                                            OneTimeWorkRequestBuilder<ReminderWorker>()
                                                .setInputData(reminderData)
                                                .build()
                                        WorkManager.getInstance(context).enqueue(workRequest)
                                    }
                                }
                                else -> {}
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

private fun requestPermission(
    context: Context,
    permission: String,
    requestPermission: () -> Unit
) {
    if (ContextCompat.checkSelfPermission(
            context,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        requestPermission()
    }
}

