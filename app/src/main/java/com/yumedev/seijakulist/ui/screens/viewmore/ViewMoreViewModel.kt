package com.yumedev.seijakulist.ui.screens.viewmore

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.domain.models.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewMoreViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Estados para paginación
    private val _currentPage = MutableStateFlow(1)
    private val _hasMorePages = MutableStateFlow(true)

    private var currentSection: String = ""
    private var currentFilter: String? = null

    fun loadAnimes(section: String, filter: String? = null) {
        currentSection = section
        currentFilter = filter
        _currentPage.value = 1
        _hasMorePages.value = true

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val results = fetchAnimes(section, filter, 1)
                // Filtrar duplicados usando distinctBy con malId
                _animeList.value = results.distinctBy { it.malId }

                // Si recibimos menos de 25 resultados, no hay más páginas
                if (results.size < 25) {
                    _hasMorePages.value = false
                }

            } catch (e: Exception) {
                Log.e("ViewMoreVM", "Error: ${e.message}")
                _errorMessage.value = "Error al cargar animes: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMoreAnimes() {
        if (_isLoadingMore.value || !_hasMorePages.value || _isLoading.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true

            try {
                val nextPage = _currentPage.value + 1
                val results = fetchAnimes(currentSection, currentFilter, nextPage)

                if (results.isNotEmpty()) {
                    _currentPage.value = nextPage
                    // Combinar listas y eliminar duplicados basados en malId
                    val combinedList = (_animeList.value + results).distinctBy { it.malId }
                    _animeList.value = combinedList

                    if (results.size < 25) {
                        _hasMorePages.value = false
                    }
                } else {
                    _hasMorePages.value = false
                }

            } catch (e: Exception) {
                Log.e("ViewMoreVM", "Error loading more: ${e.message}")
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    private suspend fun fetchAnimes(section: String, filter: String?, page: Int): List<Anime> {
        val response = when (section) {
            "season_now" -> {
                if (filter != null) {
                    animeRepository.searchAnimeSchedule(filter, page)
                } else {
                    animeRepository.searchAnimeSeasonNow(page)
                }
            }
            "top_anime" -> {
                if (filter != null) {
                    animeRepository.searchTopAnimeFilter(filter, page)
                } else {
                    animeRepository.searchTopAnimes(page)
                }
            }
            "season_upcoming" -> {
                if (filter != null) {
                    animeRepository.searchAnimeSeasonUpcomingFilter(filter, page)
                } else {
                    animeRepository.searchAnimeSeasonUpcoming(page)
                }
            }
            else -> return emptyList()
        }

        val animeDtoList = response.data ?: emptyList()

        return animeDtoList.map { dto ->
            Anime(
                malId = dto!!.malId,
                title = dto.title ?: "Título predeterminado",
                image = dto.images?.webp?.largeImageUrl
                    ?: dto.images?.jpg?.largeImageUrl
                    ?: "URL de imagen predeterminada",
                score = dto.score ?: 0.0f
            )
        }
    }
}
