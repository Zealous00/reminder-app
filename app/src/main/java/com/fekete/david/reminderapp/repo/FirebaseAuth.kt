package com.fekete.david.reminderapp.repo

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow

object FirebaseAuthRepo {
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

//    fun getAuthState(viewModelScope: CoroutineScope) = callbackFlow {
//        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
//            trySend(auth.currentUser == null)
//        }
//        auth.addAuthStateListener(authStateListener)
//        awaitClose {
//            auth.removeAuthStateListener(authStateListener)
//        }
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), auth.currentUser == null)
}