package com.example.seijakulist.ui.screens.configuration

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel : ViewModel() {
    private val _isDarkThemeEnabled = MutableStateFlow(true)
    val isDarkThemeEnabled = _isDarkThemeEnabled.asStateFlow()

    fun toggleTheme() {
        _isDarkThemeEnabled.value = !_isDarkThemeEnabled.value
    }
}