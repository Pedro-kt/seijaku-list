package com.example.seijakulist.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.domain.models.AnimeCharactersDetail
import com.example.seijakulist.domain.models.AnimeThemes
import com.example.seijakulist.domain.usecase.GetAnimeThemesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeThemesViewModel @Inject constructor(

    private val getAnimeThemesUseCase: GetAnimeThemesUseCase

) : ViewModel() {

    private val _themes = MutableStateFlow(AnimeThemes(
        openings = emptyList(),
        endings = emptyList()
    ))

    val themes: StateFlow<AnimeThemes> = _themes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun animeThemes(animeId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _themes.value = getAnimeThemesUseCase(animeId)
            } catch (e: Exception) {
                _errorMessage.value = "Ups! Algo salió mal al cargar los opening / ending del anime."
            } finally {
                _isLoading.value = false
            }
        }
    }

}