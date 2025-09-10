package com.example.seijakulist.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.domain.usecase.GetTopAnimeFilterUseCase
import com.example.seijakulist.domain.usecase.GetTopAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopAnimeViewModel @Inject constructor(

    private val getTopAnimeUseCase: GetTopAnimeUseCase,

) : ViewModel() {

    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val isError: StateFlow<Boolean> = _errorMessage.map { it != null }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private var isDataLoaded = false

    init {
        if (!isDataLoaded) {
            topAnime()
        }
    }

    fun topAnime() {

        viewModelScope.launch {

            _errorMessage.value = null
            _isLoading.value = true

            try {

                val results = getTopAnimeUseCase()
                val filtered = results.distinctBy { it.malId }
                _animeList.value = filtered
                isDataLoaded = true

            } catch (e: Exception) {

                _errorMessage.value = "Error al buscar animes: ${e.localizedMessage ?: "Error desconocido"}"
                _animeList.value = emptyList()

            } finally {

                _isLoading.value = false

            }
        }
    }

}