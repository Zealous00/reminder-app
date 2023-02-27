package com.fekete.david.reminderapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.fekete.david.reminderapp.worker.NotificationWorker

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
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

        val notificationRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(reminderData)
            .build()
        WorkManager.getInstance(context!!).enqueue(notificationRequest)
    }
}
