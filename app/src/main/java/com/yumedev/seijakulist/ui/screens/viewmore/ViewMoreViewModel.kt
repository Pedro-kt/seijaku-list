package com.yumedev.seijakulist.ui.screens.viewmore

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.usecase.anilist.GetTopAnimeFilterAniListUseCase
import com.yumedev.seijakulist.domain.usecase.anilist.GetAnimeSeasonUpcomingFilterAniListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewMoreViewModel @Inject constructor(
    private val animeAniListRepository: AnimeAniListRepository,
    private val getTopAnimeFilterAniListUseCase: GetTopAnimeFilterAniListUseCase,
    private val getAnimeSeasonUpcomingFilterAniListUseCase: GetAnimeSeasonUpcomingFilterAniListUseCase
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
        val animeCards = when (section) {
            "season_now" -> {
                if (filter != null) {
                    // Filtrar temporada actual por formato
                    val format = mapFilterToMediaFormat(filter)
                    animeAniListRepository.searchAnimeAdvanced(
                        format = format,
                        sort = listOf(
                            com.yumedev.seijakulist.data.remote.graphql.type.MediaSort.POPULARITY_DESC
                        ),
                        page = page,
                        perPage = 25
                    )
                } else {
                    animeAniListRepository.getCurrentSeasonAnime(page = page)
                }
            }
            "top_anime" -> {
                if (filter != null) {
                    // Usar UseCase para top anime con filtro
                    return getTopAnimeFilterAniListUseCase.invoke(filter, page)
                } else {
                    animeAniListRepository.getTopAnime(page = page)
                }
            }
            "season_upcoming" -> {
                if (filter != null) {
                    // Usar UseCase para upcoming anime con filtro
                    return getAnimeSeasonUpcomingFilterAniListUseCase.invoke(filter, page)
                } else {
                    animeAniListRepository.getUpcomingAnime(page = page)
                }
            }
            else -> return emptyList()
        }

        // Convertir AnimeCard a Anime
        return animeCards.map { card ->
            Anime(
                malId = card.malId,
                title = card.title,
                image = card.images,
                score = card.score
            )
        }
    }

    /**
     * Mapea el filtro string a MediaFormat de AniList
     */
    private fun mapFilterToMediaFormat(filter: String): com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat? {
        return when (filter.lowercase()) {
            "tv" -> com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat.TV
            "movie" -> com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat.MOVIE
            "ova" -> com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat.OVA
            "ona" -> com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat.ONA
            "special" -> com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat.SPECIAL
            "music" -> com.yumedev.seijakulist.data.remote.graphql.type.MediaFormat.MUSIC
            else -> null
        }
    }
}
