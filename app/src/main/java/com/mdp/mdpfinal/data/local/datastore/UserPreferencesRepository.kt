package com.mdp.mdpfinal.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {
    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val SAVED_JOKE_SETUP = stringPreferencesKey("saved_joke_setup")
        private val SAVED_JOKE_PUNCHLINE = stringPreferencesKey("saved_joke_punchline")

    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    // Expose the saved joke as a Flow of a Pair (Setup, Punchline)
    val savedJoke: Flow<Pair<String, String>> = context.dataStore.data
        .map { preferences ->
            val setup = preferences[SAVED_JOKE_SETUP] ?: "No joke loaded yet!"
            val punchline = preferences[SAVED_JOKE_PUNCHLINE] ?: "Run the background task."
            Pair(setup, punchline)
        }

    // Save a new joke
    suspend fun saveJoke(setup: String, punchline: String) {
        context.dataStore.edit { preferences ->
            preferences[SAVED_JOKE_SETUP] = setup
            preferences[SAVED_JOKE_PUNCHLINE] = punchline
        }
    }
}
    