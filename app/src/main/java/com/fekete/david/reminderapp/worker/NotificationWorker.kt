package com.fekete.david.reminderapp.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fekete.david.reminderapp.Graph
import com.fekete.david.reminderapp.R
import com.fekete.david.reminderapp.data.entitiy.Priority
import com.fekete.david.reminderapp.data.entitiy.Reminder
import com.fekete.david.reminderapp.repository.StorageRepository
import com.fekete.david.reminderapp.viewmodel.ReminderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class NotificationWorker(private val context: Context, private val workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val scope = CoroutineScope(Dispatchers.Main)
        val reminderViewModel = ReminderViewModel(StorageRepository())
        val notificationId = inputData.getString("id") ?: ""
        val inputData = workerParams.inputData
        val reminder = Reminder(
            id = inputData.getString("id") ?: "",
            message = inputData.getString("message") ?: "",
            locationX = inputData.getString("locationX") ?: "",
            locationY = inputData.getString("locationY") ?: "",
            reminderTime = Date(inputData.getLong("reminderTime", 0)),
            creationTime = Date(inputData.getLong("creationTime", 0)),
            userId = inputData.getString("userId") ?: "",
            reminderSeen = true,
            priority = Priority.valueOf(inputData.getString("priority") ?: Priority.MEDIUM.name),
            hasNotification = inputData.getBoolean("hasNotification", false)
        )

        val builder = NotificationCompat.Builder(
            Graph.appContext,
            "channel_id"
        )
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("${reminder.message}")
            .setContentText("priority: ${reminder.priority}!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        try {
            with(NotificationManagerCompat.from(Graph.appContext)) {
                if (ActivityCompat.checkSelfPermission(
                        Graph.appContext,
                        Manifest.permission.ACCESS_NOTIFICATION_POLICY
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Handle permission denied
                    return Result.failure()
                }
                notify(notificationId.hashCode(), builder.build())
            }
        } catch (e: Exception) {
            Log.e("NotificationWorker", "Failed to send notification", e)
            return Result.failure()
        }

        scope.launch {
            reminderViewModel.updateReminder(
                reminder
            )
        }
        return Result.success()
    }
}
