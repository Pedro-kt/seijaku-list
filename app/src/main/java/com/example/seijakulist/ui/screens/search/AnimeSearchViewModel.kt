package com.example.seijakulist.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.domain.models.AnimeCard
import com.example.seijakulist.domain.usecase.GetAnimeSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeSearchViewModel @Inject constructor(

    private val getAnimeSearchUseCase: GetAnimeSearchUseCase

) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _animeList = MutableStateFlow<List<AnimeCard>>(emptyList())
    val animeList: StateFlow<List<AnimeCard>> = _animeList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onSearchQueryChanged(newQuery: String) {

        _searchQuery.value = newQuery

    }

    fun searchAnimes() {

        viewModelScope.launch {

            _errorMessage.value = null
            _isLoading.value = true

            try {

                val results = getAnimeSearchUseCase(query = _searchQuery.value, page = 1)
                val filtered = results.distinctBy { it.malId }
                _animeList.value = filtered

            } catch (e: Exception) {

                _errorMessage.value = "Error al buscar animes: ${e.localizedMessage ?: "Error desconocido"}"
                _animeList.value = emptyList()

            } finally {

                _isLoading.value = false

            }
        }
    }
}