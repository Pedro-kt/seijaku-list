package com.yumedev.seijakulist.ui.screens.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.data.local.preferences.OnboardingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para el Onboarding
 *
 * Responsabilidades:
 * - Proveer el estado de si el usuario ya vio el onboarding
 * - Marcar el onboarding como visto cuando el usuario completa el flujo
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val onboardingPreferences = OnboardingPreferences(application)

    /**
     * Estado que indica si el usuario ya vio el onboarding
     */
    val hasSeenOnboarding: StateFlow<Boolean> = onboardingPreferences.hasSeenOnboarding
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    /**
     * Marca el onboarding como completado
     * Se llama cuando el usuario:
     * - Elige "crear cuenta"
     * - Elige "iniciar sesión"
     * - Elige "explorar sin cuenta"
     */
    fun markOnboardingAsCompleted() {
        viewModelScope.launch {
            onboardingPreferences.markOnboardingAsSeen()
        }
    }

    /**
     * Resetea el onboarding (solo para desarrollo/testing)
     */
    fun resetOnboarding() {
        viewModelScope.launch {
            onboardingPreferences.resetOnboarding()
        }
    }
}
