package com.fekete.david.reminderapp.viewmodel


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.fekete.david.reminderapp.Graph
import com.fekete.david.reminderapp.data.entitiy.Reminder
import com.fekete.david.reminderapp.repository.StorageRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.app.NotificationManagerCompat.from
import com.fekete.david.reminderapp.R

class ReminderViewModel(private val repository: StorageRepository) : ViewModel() {


    private val _reminderStatus = MutableStateFlow<ReminderStatus?>(null)
    val reminderStatus = _reminderStatus.asStateFlow()

    private val _addedReminder = MutableStateFlow<ReminderStatus?>(null)
    val addedReminder = _reminderStatus.asStateFlow()

    private val _reminders = MutableStateFlow<List<Reminder>?>(null)
    val reminders = _reminders.asStateFlow()

    private val _updateStatus = MutableStateFlow<ReminderStatus?>(null)
    val updateStatus = _updateStatus.asStateFlow()


    private val hasUser: Boolean
        get() = repository.hasUser()

    private val user: FirebaseUser?
        get() = repository.user

    //    suspend fun addReminder(reminder: Reminder) {
//        if (hasUser) {
//            repository.addReminder(reminder) {
//                if (it) {
//                    _reminderStatus.value = ReminderStatus.Succesful
//                } else {
//                    _reminderStatus.value =
//                        ReminderStatus.Failure(Exception("Could not add reminder!"))
//
//                }
//            }
////            notifyUserOfReminder(reminder)
//        }
    suspend fun addReminder(reminder: Reminder): Pair<ReminderStatus, String?> {
        if (hasUser) {
            return try {
                val uid = repository.addReminder(reminder)
                Pair(ReminderStatus.Successful, uid)
            } catch (e: Exception) {
                Pair(ReminderStatus.Failure(Exception("Could not add reminder!")), null)
            }
        } else {
            return Pair(ReminderStatus.Failure(Exception("User not logged in!")), null)
        }
    }



    fun getUserReminders(userId: String?) {
        if (hasUser) {
            repository.getUserReminders(
                userId!!,
                onSuccess = { reminders ->
                    _reminders.value = reminders
                },
                onError = { error -> println(error) })
        }
    }

    suspend fun updateReminder(reminder: Reminder) {
        repository.updateReminder(reminder) {
            if (it) {
                _updateStatus.value = ReminderStatus.Successful
            } else {
                _updateStatus.value =
                    ReminderStatus.Failure(Exception("Could not update reminder!"))
            }
        }
    }

    suspend fun deleteReminder(reminderId: String) {
        repository.deleteReminder(reminderId) {
            if (it) {
                println("Reminder deleted!")
            } else {
                println("Reminder wasnt deleted!")
            }
        }
    }
//    fun getReminder(reminderId: String) {
//        repository.getReminder(reminderId = reminderId, onError = {}) {
//
//        }
//    }

//    fun getReminders(): Flow<Resources<List<Reminder>>> {
//        if (hasUser) {
//            return repository.getUserReminders(user!!.uid)
//        }
//    }


}

sealed class ReminderStatus {
    object Successful : ReminderStatus()
    class Failure(val exception: Exception?) : ReminderStatus() {
        val exceptionMessage = exception?.localizedMessage
    }
}
