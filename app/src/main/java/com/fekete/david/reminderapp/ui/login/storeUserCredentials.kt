package com.fekete.david.reminderapp.ui.login

import android.content.Context
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
    }


    suspend fun saveUserCredentials(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = user.username
            preferences[PASSWORD_KEY] = user.password
            preferences[PHONE_NUMBER_KEY] = user.phoneNumber
        }
    }

    val getUserFromDataStore: Flow<User?> = context.dataStore.data.map { preferences ->
        User(
            username = preferences[USERNAME_KEY] ?: "",
            password = preferences[PASSWORD_KEY] ?: "",
            phoneNumber = preferences[PHONE_NUMBER_KEY] ?: ""
        )
    }

    fun isKeyStored(key: Preferences.Key<String>): Flow<Boolean>  =
        context.dataStore.data.map {
                preference -> preference.contains(key)
        }

}