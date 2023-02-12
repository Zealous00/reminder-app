package com.fekete.david.reminderapp.viewmodel

import androidx.lifecycle.ViewModel
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
    }

}

sealed class UserLoginStatus {
    object Succesful : UserLoginStatus()
    class Failure(val exception: Exception?) : UserLoginStatus() {
        val exceptionMessage = exception?.localizedMessage
    }
}