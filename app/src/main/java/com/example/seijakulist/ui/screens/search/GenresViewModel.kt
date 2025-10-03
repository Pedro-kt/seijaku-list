package com.example.seijakulist.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.AnimeCard
import com.example.seijakulist.domain.models.Genre
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

    private val _animeList = MutableStateFlow<List<AnimeCard>>(emptyList())
    val animeList: StateFlow<List<AnimeCard>> = _animeList

    fun fetchGenres() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val genres = repository.getGenresAnime()
                _genres.value = genres
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.message
            }
        }
    }

    fun fetchAnimeByGenre(genre: String) {
        viewModelScope.launch {
            try {
                val anime = repository.getAnimeByGenre(genre)
                _animeList.value = anime
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}