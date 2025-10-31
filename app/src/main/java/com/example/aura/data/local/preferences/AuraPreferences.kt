package com.example.aura.data.local.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "aura_prefs")

class AuraPreferences(private val context: Context) {

    companion object {
        val KEY_THEME = booleanPreferencesKey("dark_mode")
        val KEY_BACKUP = booleanPreferencesKey("cloud_backup")
        val KEY_LAST_SYNC = stringPreferencesKey("last_sync")
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { it[KEY_THEME] ?: false }
    val isCloudBackupEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_BACKUP] ?: false }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[KEY_THEME] = enabled }
    }

    suspend fun setCloudBackup(enabled: Boolean) {
        context.dataStore.edit { it[KEY_BACKUP] = enabled }
    }

    suspend fun saveLastSync(date: String) {
        context.dataStore.edit { it[KEY_LAST_SYNC] = date }
    }
}
