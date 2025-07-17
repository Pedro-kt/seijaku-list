package com.example.seijakulist.ui.screens.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.domain.models.AnimeCharactersDetail
import com.example.seijakulist.domain.models.AnimeDetail
import com.example.seijakulist.domain.usecase.GetAnimeCharactersDetailUseCase
import com.example.seijakulist.domain.usecase.GetAnimeDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeCharacterDetailViewModel @Inject constructor(
    private val getAnimeCharactersDetailUseCase: GetAnimeCharactersDetailUseCase
) : ViewModel() {

    private val _characters = MutableStateFlow<List<AnimeCharactersDetail>>(emptyList())
    val characters: StateFlow<List<AnimeCharactersDetail>> = _characters

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadAnimeCharacters(animeId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _characters.value = getAnimeCharactersDetailUseCase(animeId)
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar personajes"
            } finally {
                _isLoading.value = false
            }
        }
    }
}