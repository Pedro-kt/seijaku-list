package com.example.seijakulist.ui.screens.local_anime_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.data.local.entities.AnimeEntity
import com.example.seijakulist.data.repository.AnimeLocalRepository
import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.AnimeDetail
import com.example.seijakulist.domain.models.AnimeEntityDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalAnimeDetailViewModel @Inject constructor(
    private val animeLocalRepository: AnimeLocalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeId: Int? = savedStateHandle["animeId"]

    private val _anime = MutableStateFlow<AnimeEntityDomain?>(null)
    val anime: StateFlow<AnimeEntityDomain?> = _anime.asStateFlow()

    init {
        viewModelScope.launch {
            if (animeId != null) {
                val loadedAnime = animeLocalRepository.getAnimeById(animeId)
                _anime.value = loadedAnime
            }
        }
    }
}