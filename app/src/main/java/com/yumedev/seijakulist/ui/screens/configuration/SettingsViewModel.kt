package com.yumedev.seijakulist.ui.screens.configuration

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsViewModel @Inject constructor() : ViewModel() {
    private val _isDarkThemeEnabled = MutableStateFlow(true)
    val isDarkThemeEnabled = _isDarkThemeEnabled.asStateFlow()

    // Estado para el tema japonés
    private val _isJapaneseThemeEnabled = MutableStateFlow(false)
    val isJapaneseThemeEnabled = _isJapaneseThemeEnabled.asStateFlow()

    fun toggleTheme() {
        _isDarkThemeEnabled.value = !_isDarkThemeEnabled.value
    }

    fun toggleJapaneseTheme() {
        _isJapaneseThemeEnabled.value = !_isJapaneseThemeEnabled.value
        android.util.Log.d("SettingsViewModel", "Tema japonés cambiado a: ${_isJapaneseThemeEnabled.value}")
    }
}