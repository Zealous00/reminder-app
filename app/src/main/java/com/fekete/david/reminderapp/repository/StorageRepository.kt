package com.fekete.david.reminderapp.repository

import com.fekete.david.reminderapp.data.entitiy.Reminder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
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
        val reminderList = ArrayList<Reminder>()
//        reminderList
        remindersRef.orderBy("reminderTime").whereEqualTo("userId", userId).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    for (reminder in snapshot.documents) {
                        val r = reminder.toObject(Reminder::class.java)
                        r?.id = reminder.id
                        reminderList.add(r!!)
                    }
                    onSuccess.invoke(reminderList)
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

    suspend fun addReminder(
        reminder: Reminder,
        onComplete: (Boolean) -> Unit
    ) {
        val documentId = remindersRef.document().id
        remindersRef.document(documentId).set(reminder)
            .addOnCompleteListener() { result -> onComplete.invoke(result.isSuccessful) }.await()
    }

    fun deleteReminder(
        reminderId: String,
        onComplete: (Boolean) -> Unit
    ) {
        remindersRef.document(reminderId).delete()
            .addOnCompleteListener { result -> onComplete.invoke(result.isSuccessful) }
    }

    suspend fun updateReminder(
//        reminderId: String,
//        message: String,
//        locationX: String,
//        locationY: String,
//        reminderTime: LocalDate,
//        reminderSeen: Boolean,
        reminder: Reminder,
        onComplete: (Boolean) -> Unit
    ) {
        val updateData = hashMapOf<String, Any>(
            "message" to reminder.message,
            "locationX" to reminder.locationX,
            "locationY" to reminder.locationY,
            "reminderTime" to reminder.reminderTime,
            "reminderSeen" to reminder.reminderSeen
        )

        remindersRef.document(reminder.id).update(updateData)
            .addOnCompleteListener { result -> onComplete.invoke(result.isSuccessful) }.await()
    }
}

//sealed class Resources<T>(
//    val data: T? = null,
//    val throwable: Throwable? = null
//) {
//    class Success<T>(data: T?) : Resources<T>(data = data)
//    class Error<T>(throwable: Throwable?) : Resources<T>(throwable = throwable)
//}