package com.fekete.david.reminderapp.ui.login

import android.content.Context
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fekete.david.reminderapp.data.entitiy.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class StoreUserCredentials(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val PHONE_NUMBER_KEY = stringPreferencesKey("phonenumber")
        private val PIN_CODE_KEY = stringPreferencesKey("pincode")
        private val IMAGE_URI_KEY = stringPreferencesKey("imageuri")
    }


    suspend fun saveUserCredentials(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = user.username
            preferences[PASSWORD_KEY] = user.password
            preferences[PHONE_NUMBER_KEY] = user.phoneNumber
            preferences[PIN_CODE_KEY] = user.pinCode
            preferences[IMAGE_URI_KEY] = user.imageUri
        }
    }

    val getUserName: Flow<String>
        get() = context.dataStore.data.map {
            it[USERNAME_KEY] ?: ""
        }

    suspend fun setUserName(value: String) {
        context.dataStore.edit { it[PASSWORD_KEY] = value }
    }

    val getPassword: Flow<String>
        get() = context.dataStore.data.map {
            it[USERNAME_KEY] ?: ""
        }

    suspend fun setPassword(value: String) {
        context.dataStore.edit { it[PASSWORD_KEY] = value }
    }

    val getPhoneNumber: Flow<String>
        get() = context.dataStore.data.map {
            it[PHONE_NUMBER_KEY] ?: ""
        }

    suspend fun setPhoneNumber(value: String) {
        context.dataStore.edit { it[PHONE_NUMBER_KEY] = value }
    }

    val getPinCode: Flow<String>
        get() = context.dataStore.data.map {
            it[PIN_CODE_KEY] ?: ""
        }

    suspend fun setPinCode(value: String) {
        context.dataStore.edit { it[PIN_CODE_KEY] = value }
    }

    val getImageUri: Flow<String>
        get() = context.dataStore.data.map {
            it[IMAGE_URI_KEY] ?: ""
        }

    suspend fun setImageUri(value: String) {
        context.dataStore.edit { it[IMAGE_URI_KEY] = value }
    }

    val getUserFromDataStore: Flow<User?> = context.dataStore.data.map { preferences ->
        User(
            username = preferences[USERNAME_KEY] ?: "",
            password = preferences[PASSWORD_KEY] ?: "",
            phoneNumber = preferences[PHONE_NUMBER_KEY] ?: "",
            pinCode = preferences[PIN_CODE_KEY] ?: "",
            imageUri = preferences[IMAGE_URI_KEY] ?: ""
        )
    }

    fun isKeyStored(key: Preferences.Key<String>): Flow<Boolean> =
        context.dataStore.data.map { preference ->
            preference.contains(key)
        }
}