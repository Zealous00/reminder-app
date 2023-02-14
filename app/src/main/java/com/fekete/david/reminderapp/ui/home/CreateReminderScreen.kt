package com.fekete.david.reminderapp.ui.home

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fekete.david.reminderapp.data.entitiy.Reminder
import com.fekete.david.reminderapp.repository.StorageRepository
import com.fekete.david.reminderapp.ui.login.shortToast
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
    val creationTime = remember { mutableStateOf("") }

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
            Button(
                onClick = {
                    val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
                    if (reminderMessage.value.isEmpty()) {
                        shortToast(context, "Please fill out the message!")
                    } else if (reminderTime.value.isEmpty() || reminderDate.value.isEmpty()) {
                        shortToast(context, "Please choose date and time!")
                    } else {
                        scope.launch {
                            reminderViewModel.addReminder(
                                Reminder(
                                    message = reminderMessage.value,
                                    locationX = "locationX",
                                    locationY = "locationY",
                                    reminderTime = formatter.parse(reminderDate.value + " " + reminderTime.value) as Date,
                                    creationTime = Timestamp(Date().time),
                                    userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty(),
                                    reminderSeen = false
                                )
                            )
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

