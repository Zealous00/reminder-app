package com.fekete.david.reminderapp.repository

import com.fekete.david.reminderapp.data.entitiy.Reminder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

const val REMINDERS_COLLECTION_REF = "reminders"

class StorageRepository() {
    val user = FirebaseAuth.getInstance().currentUser

    fun hasUser(): Boolean = FirebaseAuth.getInstance().currentUser != null

    fun getUserId(): String = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    private val remindersRef = Firebase.firestore.collection(REMINDERS_COLLECTION_REF)

    fun getUserReminders(
        userId: String,
        onSuccess: (List<Reminder>?) -> Unit,
        onError: (Throwable?) -> Unit,

        ) {
        remindersRef.orderBy("reminderTime").whereEqualTo("userId", userId).get()
            .addOnSuccessListener {
                if (it != null) {
                    onSuccess.invoke(it.toObjects(Reminder::class.java))
                } else {
                    onError.invoke(Throwable("No reminders in database!"))
                }
            }.addOnFailureListener { result ->
                onError.invoke(result.cause)
            }
    }

    fun getReminder(
        reminderId: String,
        onSuccess: (Reminder?) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        remindersRef.document(reminderId).get()
            .addOnSuccessListener { onSuccess.invoke(it?.toObject(Reminder::class.java)) }
            .addOnFailureListener { result -> onError.invoke(result.cause) }
    }

    fun addReminder(
        reminder: Reminder,
        onComplete: (Boolean) -> Unit
    ) {
        val documentId = remindersRef.document().id
        remindersRef.document(documentId).set(reminder)
            .addOnCompleteListener() { result -> onComplete.invoke(result.isSuccessful) }
        println(reminder)
    }

    fun deleteReminder(
        reminderId: String,
        onComplete: (Boolean) -> Unit
    ) {
        remindersRef.document(reminderId).delete()
            .addOnCompleteListener { result -> onComplete.invoke(result.isSuccessful) }
    }

    fun updateReminder(
        reminderId: String,
        message: String,
        locationX: String,
        locationY: String,
        reminderTime: LocalDate,
        reminderSeen: Boolean,
        onComplete: (Boolean) -> Unit
    ) {
        val updateData = hashMapOf<String, Any>(
            "message" to message,
            "locationX" to locationX,
            "locationY" to locationY,
            "reminderTime" to reminderTime,
            "reminderSeen" to reminderSeen
        )

        remindersRef.document(reminderId).update(updateData)
            .addOnCompleteListener { result -> onComplete.invoke(result.isSuccessful) }
    }
}

//sealed class Resources<T>(
//    val data: T? = null,
//    val throwable: Throwable? = null
//) {
//    class Success<T>(data: T?) : Resources<T>(data = data)
//    class Error<T>(throwable: Throwable?) : Resources<T>(throwable = throwable)
//}