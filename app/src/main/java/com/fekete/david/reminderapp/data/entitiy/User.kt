package com.fekete.david.reminderapp.data.entitiy

import java.util.*

data class User(
    val username: String,
    val password: String,
    val phoneNumber: String,
    val pincode: String,
    val imageUri: String,
)