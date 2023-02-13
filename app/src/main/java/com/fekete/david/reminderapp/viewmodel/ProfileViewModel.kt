package com.fekete.david.reminderapp.viewmodel

import androidx.lifecycle.ViewModel
import com.fekete.david.reminderapp.data.entitiy.Reminder
import com.fekete.david.reminderapp.data.entitiy.User
import com.fekete.david.reminderapp.repository.StorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(private val repository: StorageRepository) : ViewModel() {
    private val _userProfileStatus = MutableStateFlow<ReminderStatus?>(null)
    val userProfileStatus = _userProfileStatus.asStateFlow()

    private val hasUser: Boolean
        get() = repository.hasUser()

    suspend fun addUserProfile(user: User) {
        if (hasUser) {
            repository.addUserProfile(user) {
                if (it) {
                    _userProfileStatus.value = ReminderStatus.Succesful
                } else {
                    _userProfileStatus.value =
                        ReminderStatus.Failure(Exception("Could not add reminder!"))

                }
            }
        }
    }

}