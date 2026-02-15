package com.yumedev.seijakulist.ui.screens.configuration

import androidx.lifecycle.ViewModel
import com.yumedev.seijakulist.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsViewModel @Inject constructor() : ViewModel() {
    // Estado para el modo de tema seleccionado
    private val _themeMode = MutableStateFlow(ThemeMode.DARK)
    val themeMode = _themeMode.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        android.util.Log.d("SettingsViewModel", "Modo de tema cambiado a: ${mode.name}")
    }
}