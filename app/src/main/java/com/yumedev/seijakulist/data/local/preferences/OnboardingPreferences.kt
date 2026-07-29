package com.yumedev.seijakulist.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Gestiona las preferencias relacionadas con el onboarding
 * usando DataStore para persistencia asíncrona
 */
class OnboardingPreferences(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "onboarding_preferences"
        )
        private val HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
    }

    /**
     * Flow que emite true si el usuario ya vio el onboarding
     */
    val hasSeenOnboarding: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[HAS_SEEN_ONBOARDING] ?: false
    }

    /**
     * Marca el onboarding como visto
     */
    suspend fun markOnboardingAsSeen() {
        context.dataStore.edit { preferences ->
            preferences[HAS_SEEN_ONBOARDING] = true
        }
    }

    /**
     * Resetea el flag del onboarding (útil para desarrollo/testing)
     */
    suspend fun resetOnboarding() {
        context.dataStore.edit { preferences ->
            preferences[HAS_SEEN_ONBOARDING] = false
        }
    }
}
