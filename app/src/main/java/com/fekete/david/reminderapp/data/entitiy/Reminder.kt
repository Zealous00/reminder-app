package com.fekete.david.reminderapp.data.entitiy


import java.util.*

//Message, location_x, location_y, reminder_time, creation_time, creator_id, reminder_seen

data class Reminder(
    var id: String = "",
    val message: String = "",
    var locationX: String = "",
    var locationY: String = "",
    val reminderTime: Date = Date(),
    val creationTime: Date = Date(),
    val userId: String = "",
    val reminderSeen: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val hasNotification: Boolean = true,
) : java.io.Serializable

enum class Priority {
    LOW, MEDIUM, HIGH
}
