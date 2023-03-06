package com.fekete.david.reminderapp.worker

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.fekete.david.reminderapp.data.entitiy.Priority
import com.fekete.david.reminderapp.data.entitiy.Reminder
import com.fekete.david.reminderapp.service.LocationReminderBroadcastReceiver
import com.fekete.david.reminderapp.service.TimeReminderBroadcastReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import java.util.*

class LocationReminderWorker(
    private val context: Context, private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

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
            priority = Priority.valueOf(inputData.getString("priority") ?: Priority.MEDIUM.name),
            hasNotification = inputData.getBoolean("hasNotification", false)
        )
        println(reminder)
        if (reminder.locationX.isNotEmpty() && reminder.locationY.isNotEmpty()) {
            scheduleNotification(reminder)
        }

        return Result.success()
    }

    private fun scheduleNotification(reminder: Reminder) {

        println("inside scheduling")
        println(reminder)
        val reminderBundle = Bundle().apply {
            putString("id", reminder.id)
            putString("message", reminder.message)
            putString("locationX", reminder.locationX)
            putString("locationY", reminder.locationY)
            putLong("reminderTime", reminder.reminderTime.time)
            putLong("creationTime", reminder.creationTime.time)
            putString("userId", reminder.userId)
            putBoolean("reminderSeen", reminder.reminderSeen)
            putString("priority", reminder.priority.name)
            putBoolean("hasNotification", reminder.hasNotification)
        }

        val reminderIntent = Intent(context, LocationReminderBroadcastReceiver::class.java)
            .putExtra("reminderBundle", reminderBundle)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id.hashCode(),
            reminderIntent,
            PendingIntent.FLAG_MUTABLE
        )

        val latitude = reminder.locationX.toDouble()
        val longitude = reminder.locationY.toDouble()
        val radiusInMeters = 100f

        println(latitude)
        println(longitude)
        println(radiusInMeters)

        val geofence = Geofence.Builder()
            .setRequestId(reminder.id)
            .setCircularRegion(
                latitude,
                longitude,
                radiusInMeters
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        println(geofence)

        val geofencingClient = LocationServices.getGeofencingClient(context)
        println(geofencingClient)
        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        println(geofencingRequest)

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("Not granted!")
        } else {
            println("All granted!")
        }

        println("In the end of worker")
        geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener {
            Log.d(TAG, "Geofence successfully added.")
        }.addOnFailureListener {
            println(it.message)
        }
    }
}


