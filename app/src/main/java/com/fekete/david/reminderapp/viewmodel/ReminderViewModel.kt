package com.fekete.david.reminderapp.viewmodel

import androidx.lifecycle.ViewModel
import com.fekete.david.reminderapp.data.entitiy.Reminder
import com.fekete.david.reminderapp.repository.StorageRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReminderViewModel(private val repository: StorageRepository) : ViewModel() {
    private val _reminderStatus = MutableStateFlow<ReminderStatus?>(null)
    val reminderStatus = _reminderStatus.asStateFlow()

    private val _reminders = MutableStateFlow<List<Reminder>?>(null)
    val reminders = _reminders.asStateFlow()


    private val hasUser: Boolean
        get() = repository.hasUser()

    private val user: FirebaseUser?
        get() = repository.user

    fun addReminder(reminder: Reminder) {
        if (hasUser) {
            repository.addReminder(reminder) {
                if (it) {
                    _reminderStatus.value = ReminderStatus.Succesful
                    println("SUCCESFULLY ADDED")
                } else {
                    _reminderStatus.value =
                        ReminderStatus.Failure(Exception("Could not add reminder!"))
                    println("SHIT IS GOING ON NOT ADDED")

                }
            }
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
    object Succesful : ReminderStatus()
    class Failure(val exception: Exception?) : ReminderStatus() {
        val exceptionMessage = exception?.localizedMessage
    }
}
