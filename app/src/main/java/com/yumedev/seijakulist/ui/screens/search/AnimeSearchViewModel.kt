package com.yumedev.seijakulist.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.models.AnimeCard
import com.yumedev.seijakulist.domain.usecase.GetAnimeSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyList
import kotlin.collections.distinctBy

@HiltViewModel
class AnimeSearchViewModel @Inject constructor(
    private val getAnimeSearchUseCase: GetAnimeSearchUseCase,
    private val animeRepository: AnimeRepository,
    private val animeLocalRepository: AnimeLocalRepository
) : ViewModel() {

    // --- ESTADOS DE ENTRADA (Filtros) ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow<String?>("Anime") // Valor inicial: Anime
    val selectedFilter: StateFlow<String?> = _selectedFilter.asStateFlow()

    private val _selectedGenreId = MutableStateFlow<String?>(null)
    val selectedGenreId: StateFlow<String?> = _selectedGenreId.asStateFlow()

    // --- ESTADOS DE SALIDA (Resultados) ---
    private val _animeList = MutableStateFlow<List<AnimeCard>>(emptyList())
    val animeList: StateFlow<List<AnimeCard>> = _animeList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Estados para paginación
    private val _currentPage = MutableStateFlow(1)
    private val _hasMorePages = MutableStateFlow(true)

    // --- ACCIONES ---

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        //forzamos el filtro a "Anime" y limpiamos el género.
        _selectedFilter.value = "Anime"
        _selectedGenreId.value = null
    }

    fun onFilterSelected(filter: String?) {
        _selectedFilter.value = filter
        // Si cambiamos el filtro, limpiamos la query de texto.
        _searchQuery.value = ""
        // Si el nuevo filtro NO es "Generos", limpiamos el ID del género.
        if (filter != "Generos") {
            _selectedGenreId.value = null
        }
    }

    fun onGenreSelected(genreId: String) {
        // Al seleccionar un género, limpiamos la query de texto.
        _searchQuery.value = ""
        // Si ya está seleccionado, lo deselecciona; si no, lo selecciona.
        _selectedGenreId.value = if (_selectedGenreId.value == genreId) null else genreId
    }

    // FUNCIÓN UNIFICADA DE BÚSQUEDA (reinicia la paginación)
    fun performSearchOrFilter() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            _currentPage.value = 1
            _hasMorePages.value = true

            try {
                // Especificamos explícitamente que 'results' será List<AnimeCard>
                val results: List<AnimeCard> = when (_selectedFilter.value) {
                    "Anime" -> {
                        if (_searchQuery.value.isNotBlank()) {
                            getAnimeSearchUseCase(query = _searchQuery.value, page = 1)
                        } else {
                            emptyList()
                        }
                    }
                    "Generos" -> {
                        val id = _selectedGenreId.value
                        if (id != null) {
                            animeRepository.getAnimeByGenre(id)
                        } else {
                            emptyList()
                        }
                    }
                    else -> emptyList()
                }
                _animeList.value = results.distinctBy { it.malId }

                // Si recibimos menos de 25 resultados, no hay más páginas
                if (results.size < 25) {
                    _hasMorePages.value = false
                }

            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener datos: ${e.localizedMessage ?: "Error desconocido"}"
                _animeList.value = emptyList()

            } finally {
                _isLoading.value = false
            }
        }
    }

    // FUNCIÓN PARA CARGAR MÁS RESULTADOS (siguiente página)
    fun loadMoreAnimes() {
        // Solo cargar más si no estamos ya cargando y hay más páginas
        if (_isLoadingMore.value || !_hasMorePages.value || _isLoading.value) return

        // Solo funciona para búsqueda de anime por ahora
        if (_selectedFilter.value != "Anime" || _searchQuery.value.isBlank()) return

        viewModelScope.launch {
            _isLoadingMore.value = true

            try {
                val nextPage = _currentPage.value + 1
                val results = getAnimeSearchUseCase(query = _searchQuery.value, page = nextPage)

                if (results.isNotEmpty()) {
                    _currentPage.value = nextPage
                    // Agregar los nuevos resultados a la lista existente, eliminando duplicados
                    _animeList.value = (_animeList.value + results).distinctBy { it.malId }

                    // Si recibimos menos de 25 resultados, no hay más páginas
                    if (results.size < 25) {
                        _hasMorePages.value = false
                    }
                } else {
                    _hasMorePages.value = false
                }

            } catch (e: Exception) {
                Log.e("AnimeSearchVM", "Error al cargar más animes: ${e.message}")
                // No mostramos error en pantalla, solo dejamos de cargar
                _hasMorePages.value = false

            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    // --- FUNCIÓN PARA AGREGAR ANIME A LA LISTA LOCAL ---
    fun addAnimeToList(
        anime: AnimeCard,
        userScore: Float,
        userStatus: String,
        userOpinion: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                // Crear AnimeEntity desde AnimeCard
                val entity = AnimeEntity(
                    malId = anime.malId,
                    title = anime.title,
                    imageUrl = anime.images,
                    userScore = userScore,
                    statusUser = userStatus,
                    userOpiniun = userOpinion,
                    totalEpisodes = anime.episodes.toIntOrNull() ?: 0,
                    episodesWatched = if (userStatus == "Completado") anime.episodes.toIntOrNull() ?: 0 else 0,
                    rewatchCount = if (userStatus == "Completado") 1 else 0
                )

                // Insertar en la base de datos local
                animeLocalRepository.insertAnime(entity)
                onSuccess()

            } catch (e: Exception) {
                Log.e("AnimeSearchVM", "Error al guardar anime: ${e.message}")
                onError(e.message ?: "Error desconocido")
            }
        }
    }
}