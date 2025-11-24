package com.yumedev.seijakulist.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.domain.models.Genre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenresViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {
    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        // Cargar géneros automáticamente al inicializar el ViewModel
        fetchGenres()
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val genres = repository.getGenresAnime()
                _genres.value = genres
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}