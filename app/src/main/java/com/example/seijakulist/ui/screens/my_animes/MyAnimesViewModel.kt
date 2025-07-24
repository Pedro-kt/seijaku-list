package com.example.seijakulist.ui.screens.my_animes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.data.local.entities.AnimeEntity
import com.example.seijakulist.data.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAnimeListViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {

    val savedAnimes: StateFlow<List<AnimeEntity>> =
        animeRepository.getAllAnimes()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
}

