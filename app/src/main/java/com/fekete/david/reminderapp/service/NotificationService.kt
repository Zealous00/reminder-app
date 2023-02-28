package com.fekete.david.reminderapp.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.fekete.david.reminderapp.Graph
import com.fekete.david.reminderapp.MainActivity
import com.fekete.david.reminderapp.R

class NotificationService : Service() {

    companion object {
        private const val CHANNEL_ID = "com.fekete.david.reminderapp.notification_service.channel_id"
        private const val NOTIFICATION_ID = 2
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
            startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val name = "NotificationChannel"
        val descriptionText = "NotificationChannelDescription"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("channel_id", name, importance).apply {
            description = descriptionText
        }
        val notificationManager = Graph.appContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Reminder app")
            .setContentText("is running in the background")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }
}
