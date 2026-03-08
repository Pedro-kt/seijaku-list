package com.yumedev.seijakulist.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LocalAnimeIdsViewModel @Inject constructor(
    animeLocalRepository: AnimeLocalRepository
) : ViewModel() {

    // malId -> statusUser para todos los animes en la lista del usuario
    val localAnimeStatuses: StateFlow<Map<Int, String>> = animeLocalRepository
        .getAllAnimes()
        .map { list -> list.associate { it.malId to it.statusUser } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
}