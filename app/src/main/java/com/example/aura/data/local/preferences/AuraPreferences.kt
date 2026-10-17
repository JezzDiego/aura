package com.example.aura.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "aura_prefs")

/**
 * Preferências de acessibilidade da interface (salvas localmente com DataStore).
 */
class AuraPreferences(private val context: Context) {

    companion object {
        val KEY_THEME = booleanPreferencesKey("dark_mode")
        val KEY_LARGE_TEXT = booleanPreferencesKey("large_text")
        val KEY_REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { it[KEY_THEME] ?: false }
    val isLargeText: Flow<Boolean> = context.dataStore.data.map { it[KEY_LARGE_TEXT] ?: false }
    val isReduceMotion: Flow<Boolean> = context.dataStore.data.map { it[KEY_REDUCE_MOTION] ?: false }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[KEY_THEME] = enabled }
    }

    suspend fun setLargeText(enabled: Boolean) {
        context.dataStore.edit { it[KEY_LARGE_TEXT] = enabled }
    }

    suspend fun setReduceMotion(enabled: Boolean) {
        context.dataStore.edit { it[KEY_REDUCE_MOTION] = enabled }
    }
}
