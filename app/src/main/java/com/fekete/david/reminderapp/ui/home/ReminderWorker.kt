package com.fekete.david.reminderapp.ui.home

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.fekete.david.reminderapp.Graph
import com.fekete.david.reminderapp.R
import com.fekete.david.reminderapp.data.entitiy.Priority
import com.fekete.david.reminderapp.data.entitiy.Reminder
import java.util.*
import java.util.concurrent.TimeUnit

class ReminderWorker(
    private val context: Context, private val workerParams: WorkerParameters
) :
    CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result {

        val inputData = workerParams.inputData
        val reminder = Reminder(
            id = inputData.getString("id") ?: "",
            message = inputData.getString("message") ?: "",
            locationX = inputData.getString("locationX") ?: "",
            locationY = inputData.getString("locationY") ?: "",
            reminderTime = Date(inputData.getLong("reminderTime", 0)),
            creationTime = Date(inputData.getLong("creationTime", 0)),
            userId = inputData.getString("userId") ?: "",
            reminderSeen = inputData.getBoolean("reminderSeen", false),
            priority = Priority.valueOf(inputData.getString("priority") ?: Priority.MEDIUM.name)
        )

//        val timeDiff = reminder.reminderTime.time - System.currentTimeMillis()
//        if (timeDiff > 0) {
//        scheduleNotification(timeDiff, reminder)
//        }

        scheduleNotification(reminder)
        return Result.success()
    }

    private fun scheduleNotification(reminder: Reminder) {
//        val workManager = WorkManager.getInstance(Graph.appContext)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val reminderData = Data.Builder()
            .putString("id", reminder.id)
            .putString("message", reminder.message)
            .putString("locationX", reminder.locationX)
            .putString("locationY", reminder.locationY)
            .putLong("reminderTime", reminder.reminderTime.time)
            .putLong("creationTime", reminder.creationTime.time)
            .putString("userId", reminder.userId)
            .putBoolean("reminderSeen", reminder.reminderSeen)
            .putString("priority", reminder.priority.name)
            .build()

//        val currentTimeMillis = System.currentTimeMillis()
//        val reminderTimeMillis = reminder.reminderTime.time
//        val delay = reminderTimeMillis - currentTimeMillis

//        val notificationWorkRequest: OneTimeWorkRequest = if (delay <= 0) {
//            OneTimeWorkRequestBuilder<NotificationWorker>()
//                .setInitialDelay(0, TimeUnit.MILLISECONDS)
//                .setInputData(reminderData)
//                .build()
//        } else {
//            OneTimeWorkRequestBuilder<NotificationWorker>()
//                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
//                .setInputData(reminderData)
//                .build()
//        }
//        workManager.enqueue(notificationWorkRequest)


        val reminderBundle = Bundle().apply {
            putString("id", reminder.id)
            putString("message", reminder.message)
            putString("locationX", reminder.locationX)
            putString("locationY", reminder.locationY)
            putSerializable("reminderTime", reminder.reminderTime)
            putSerializable("creationTime", reminder.creationTime)
            putString("userId", reminder.userId)
            putBoolean("reminderSeen", reminder.reminderSeen)
            putSerializable("priority", reminder.priority)
        }
        val reminderIntent = Intent(context, ReminderBroadcastReceiver::class.java)
            .putExtra("reminderBundle", reminderBundle)
        val pendingIntent = PendingIntent.getBroadcast(
            context, reminder.id.hashCode(), reminderIntent,  PendingIntent.FLAG_MUTABLE
        )

//        val notificationRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
//            .setInputData(reminderData)
//            .build()

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            reminder.reminderTime.time,
            pendingIntent,
        )
//
//        val notificationRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
//            .setInputData(reminderData)
//            .setInitialDelay(reminder.reminderTime.time - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
//            .build()
//
//        WorkManager.getInstance(context).enqueue(notificationRequest)
    }
}
