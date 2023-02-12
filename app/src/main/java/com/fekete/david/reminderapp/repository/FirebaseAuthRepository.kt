package com.fekete.david.reminderapp.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object FirebaseAuthRepository {
    val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    fun hasUser(): Boolean = FirebaseAuth.getInstance().currentUser != null

    fun getUserId(): String = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    fun login(
        firebaseAuth: FirebaseAuth,
        username: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(it.exception)

                }
            }
    }

    fun signUp(
        firebaseAuth: FirebaseAuth,
        username: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(it.exception)
                }
            }
    }

    fun signOut(firebaseAuth: FirebaseAuth) {
        firebaseAuth.signOut()
    }
}