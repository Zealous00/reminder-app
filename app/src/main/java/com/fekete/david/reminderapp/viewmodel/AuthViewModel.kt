package com.fekete.david.reminderapp.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.fekete.david.reminderapp.MainActivity
import com.fekete.david.reminderapp.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val _userLoginStatus = MutableStateFlow<UserLoginStatus?>(null)
    val userLoginStatus = _userLoginStatus.asStateFlow()

    val currentUser = FirebaseAuthRepository.currentUser
    val hasUser: Boolean
        get() = FirebaseAuthRepository.hasUser()

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun performLogin(username: String, password: String) {
        FirebaseAuthRepository.login(
            firebaseAuth = firebaseAuth,
            username = username,
            password = password,
            onSuccess = {
                _userLoginStatus.value = UserLoginStatus.Succesful
            },
            onFailure = {
                _userLoginStatus.value = UserLoginStatus.Failure(it)
            }
        )
    }

    fun createAccount(username: String, password: String) {
        FirebaseAuthRepository.signUp(
            firebaseAuth = firebaseAuth,
            username = username,
            password = password,
            onSuccess = {
                _userLoginStatus.value = UserLoginStatus.Succesful
            },
            onFailure = {
                _userLoginStatus.value = UserLoginStatus.Failure(it)
            })
    }

    fun signOutFromAccount() {
        FirebaseAuthRepository.signOut(firebaseAuth = firebaseAuth)
//        val intent = Intent(this, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(intent)
//        finish()
    }

    fun restartApp(activity: Activity?) {
        val i = Intent(activity, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity?.startActivity(i)
    }

}

sealed class UserLoginStatus {
    object Succesful : UserLoginStatus()
    class Failure(val exception: Exception?) : UserLoginStatus() {
        val exceptionMessage = exception?.localizedMessage
    }
}