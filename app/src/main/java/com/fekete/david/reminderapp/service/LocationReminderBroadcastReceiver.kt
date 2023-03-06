package com.fekete.david.reminderapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.*
import com.fekete.david.reminderapp.worker.NotificationWorker
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import java.util.concurrent.TimeUnit

class LocationReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("INSIDEEEE")
        val bundle = intent?.getBundleExtra("reminderBundle")
        val reminderData = Data.Builder().apply {
            bundle?.keySet()?.forEach { key ->
                when (val value = bundle.get(key)) {
                    is Boolean -> putBoolean(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Float -> putFloat(key, value)
                    is Double -> putDouble(key, value)
                    is String -> putString(key, value)
                    // add more cases as needed for other types
                }
            }
        }.build()

        println(reminderData)

        val geofencingEvent = GeofencingEvent.fromIntent(intent!!)
        if (geofencingEvent!!.hasError()) {
            println("Error, not good")
            return
        }

        println("Before setting up notification worker")
        if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val notificationRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(reminderData)
                .build()
            WorkManager.getInstance(context!!).enqueue(notificationRequest)
        }
    }
}