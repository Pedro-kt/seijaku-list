package com.yumedev.seijakulist.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.usecase.GetTopAnimeFilterUseCase
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
class TopAnimeFilterViewModel @Inject constructor(
    private val getTopAnimeFilterUseCase: GetTopAnimeFilterUseCase
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
    fun TopAnimeFilter(filter: String) {

        viewModelScope.launch {

            _errorMessage.value = null
            _isLoading.value = true

            try {

                val results = getTopAnimeFilterUseCase.invoke(filter)
                val filtered = results.distinctBy { it.malId }
                _animeList.value = filtered
                //isDataLoaded = true

            } catch (e: Exception) {

                _errorMessage.value = "Error al cargar los datos ${e.localizedMessage}"
                _animeList.value = emptyList()

            } finally {

                _isLoading.value = false

            }
        }
    }
}