package com.fekete.david.reminderapp.data.entitiy

import java.util.*

//Message, location_x, location_y, reminder_time, creation_time, creator_id, reminder_seen

data class Reminder(
    val id: Long,
    val message: String,
    val location_x: String,
    val location_y: String,
    val reminder_time: Date,
    val creation_time: Date,
    val creator_id: Long,
    val reminder_seen: Boolean
)
