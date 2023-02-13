package com.fekete.david.reminderapp.data.entitiy


data class User(
    val id: String = "",
    var profileId: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val pinCode: String = "",
    val imageUri: String = "",
)