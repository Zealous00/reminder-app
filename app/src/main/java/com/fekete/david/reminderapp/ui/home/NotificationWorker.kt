package com.fekete.david.reminderapp.ui.home

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fekete.david.reminderapp.Graph
import com.fekete.david.reminderapp.R
import com.fekete.david.reminderapp.data.entitiy.Priority
import com.fekete.david.reminderapp.data.entitiy.Reminder
import java.util.*

class NotificationWorker(private val context: Context, private val workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    init {
        createNotificationChannel()
    }

    override fun doWork(): Result {
        val notificationId = 10
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

        val builder = NotificationCompat.Builder(
            Graph.appContext,
            "channel_id"
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Reminder: ${reminder.message}")
            .setContentText("priority: ${reminder.priority}!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(Graph.appContext)) {
            if (ActivityCompat.checkSelfPermission(
                    Graph.appContext,
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return Result.failure()
            }
            notify(notificationId, builder.build())
        }

        return Result.success()
    }

    private fun createNotificationChannel() {
        val name = "NotificationChannel"
        val descriptionText = "NotificationChannelDescription"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("channel_id", name, importance).apply {
            description = descriptionText
        }
        val notificationManager = Graph.appContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
