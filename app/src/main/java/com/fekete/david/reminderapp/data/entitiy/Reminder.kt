package com.fekete.david.reminderapp.data.entitiy


import java.util.*

//Message, location_x, location_y, reminder_time, creation_time, creator_id, reminder_seen

data class Reminder(
    var id: String = "",
    val message: String = "",
    val locationX: String = "",
    val locationY: String = "",
    val reminderTime: Date = Date(),
    val creationTime: Date = Date(),
    val userId: String = "",
    val reminderSeen: Boolean = false
) : java.io.Serializable
