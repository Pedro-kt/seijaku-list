package com.yumedev.seijakulist.ui.screens.configuration

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.yumedev.seijakulist.ui.components.ViewMode
import com.yumedev.seijakulist.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val prefs: SharedPreferences = context.getSharedPreferences("seijaku_prefs", Context.MODE_PRIVATE)

    // Estado para el modo de tema seleccionado
    private val _themeMode = MutableStateFlow(ThemeMode.DARK)
    val themeMode = _themeMode.asStateFlow()

    // Estado para el modo de vista seleccionado
    private val _viewMode = MutableStateFlow(loadViewMode())
    val viewMode = _viewMode.asStateFlow()

    private fun loadViewMode(): ViewMode {
        val savedValue = prefs.getString("view_mode", ViewMode.LIST.name)
        return try {
            ViewMode.valueOf(savedValue ?: ViewMode.LIST.name)
        } catch (e: Exception) {
            ViewMode.LIST
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        android.util.Log.d("SettingsViewModel", "Modo de tema cambiado a: ${mode.name}")
    }

    fun setViewMode(mode: ViewMode) {
        _viewMode.value = mode
        prefs.edit().putString("view_mode", mode.name).apply()
        android.util.Log.d("SettingsViewModel", "Modo de vista cambiado a: ${mode.name}")
    }
}