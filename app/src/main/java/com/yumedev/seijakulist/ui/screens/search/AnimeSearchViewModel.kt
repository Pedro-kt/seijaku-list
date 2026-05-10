package com.yumedev.seijakulist.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.data.local.entities.SearchHistoryEntity
import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.data.repository.FirestoreAnimeRepository
import com.yumedev.seijakulist.data.repository.SearchHistoryRepository
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.models.AnimeCard
import com.yumedev.seijakulist.domain.usecase.GetAnimeSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyList
import kotlin.collections.distinctBy

@HiltViewModel
class AnimeSearchViewModel @Inject constructor(
    private val getAnimeSearchUseCase: GetAnimeSearchUseCase,
    private val animeRepository: AnimeRepository,
    private val animeLocalRepository: AnimeLocalRepository,
    private val firestoreAnimeRepository: FirestoreAnimeRepository,
    private val searchHistoryRepository: SearchHistoryRepository
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

    // Estado nuevo
    val selectedQuickFilter = MutableStateFlow<String?>(null)

    private val _selectedFormat = MutableStateFlow<String?>(null)
    val selectedFormat: StateFlow<String?> = _selectedFormat.asStateFlow()

    // Estado para búsquedas recientes
    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    fun onQuickFilterSelected(key: String?) {
        selectedQuickFilter.value = key
        _selectedFormat.value = null // Limpiar formato cuando se selecciona quick filter
    }

    fun onFormatSelected(format: String) {
        _selectedFormat.value = format
        selectedQuickFilter.value = null // Limpiar quick filter cuando se selecciona formato
    }

    init {
        // Búsqueda automática mientras el usuario escribe (debounce 400ms)
        viewModelScope.launch {
            _searchQuery
                .debounce(400L)
                .filter { it.isNotBlank() }
                .collect { performSearchOrFilter() }
        }

        // Cargar búsquedas recientes
        viewModelScope.launch {
            searchHistoryRepository.getRecentSearches().collect { searches ->
                _recentSearches.value = searches.map { it.query }
            }
        }
    }

    // --- ACCIONES ---

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        _selectedFilter.value = "Anime"
        _selectedGenreId.value = null
        selectedQuickFilter.value = null
        _selectedFormat.value = null
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _selectedFilter.value = "Anime"
        _selectedGenreId.value = null
        selectedQuickFilter.value = null
        _selectedFormat.value = null
        _animeList.value = emptyList()
        _errorMessage.value = null
    }

    fun onFilterSelected(filter: String?) {
        _selectedFilter.value = filter
        // Si cambiamos el filtro, limpiamos la query de texto.
        _searchQuery.value = ""
        // Si el nuevo filtro NO es "Generos", limpiamos el ID del género.
        if (filter != "Generos") {
            _selectedGenreId.value = null
        }
        // Limpiar quick filters y formato
        selectedQuickFilter.value = null
        _selectedFormat.value = null
    }

    fun onGenreSelected(genreId: String) {
        // Al seleccionar un género, limpiamos la query de texto.
        _searchQuery.value = ""
        // Si ya está seleccionado, lo deselecciona; si no, lo selecciona.
        _selectedGenreId.value = if (_selectedGenreId.value == genreId) null else genreId
        // Limpiar quick filters y formato
        selectedQuickFilter.value = null
        _selectedFormat.value = null
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
                val results: List<AnimeCard> = when {
                    // 1. Si hay un quick filter seleccionado
                    selectedQuickFilter.value != null -> {
                        when (selectedQuickFilter.value) {
                            "airing" -> animeRepository.getAnimeAiring(page = 1)
                            "trending" -> {
                                val response = animeRepository.searchTopAnimes(page = 1)
                                mapSearchResponseToCards(response)
                            }
                            "top" -> {
                                val response = animeRepository.searchTopAnimes(page = 1)
                                mapSearchResponseToCards(response)
                            }
                            "upcoming" -> {
                                val response = animeRepository.searchAnimeSeasonUpcoming(page = 1)
                                mapSearchResponseToCards(response)
                            }
                            "season" -> {
                                val response = animeRepository.searchAnimeSeasonNow(page = 1)
                                mapSearchResponseToCards(response)
                            }
                            "new" -> animeRepository.getAnimeNew(page = 1)
                            "popular" -> animeRepository.getAnimePopular(page = 1)
                            else -> emptyList()
                        }
                    }
                    // 2. Si hay un formato seleccionado
                    _selectedFormat.value != null -> {
                        val formatType = mapFormatToType(_selectedFormat.value!!)
                        animeRepository.getAnimeByType(formatType, page = 1)
                    }
                    // 3. Si es filtro de Anime con búsqueda de texto
                    _selectedFilter.value == "Anime" -> {
                        if (_searchQuery.value.isNotBlank()) {
                            // Guardar la búsqueda en el historial
                            saveSearchToHistory(_searchQuery.value)
                            getAnimeSearchUseCase(query = _searchQuery.value, page = 1)
                        } else {
                            emptyList()
                        }
                    }
                    // 4. Si es filtro por género
                    _selectedFilter.value == "Generos" -> {
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

    // Mapea los formatos de UI a los tipos de la API
    private fun mapFormatToType(format: String): String {
        return when (format) {
            "TV" -> "tv"
            "Película" -> "movie"
            "OVA" -> "ova"
            "ONA" -> "ona"
            "Especial" -> "special"
            "Música" -> "music"
            else -> format.lowercase()
        }
    }

    // Helper method to map SearchAnimeResponse to List<AnimeCard>
    private fun mapSearchResponseToCards(response: com.yumedev.seijakulist.data.remote.models.SearchAnimeResponse): List<AnimeCard> {
        return response.data.mapNotNull { animeDto ->
            if (animeDto == null) return@mapNotNull null
            AnimeCard(
                malId = animeDto.malId,
                title = animeDto.title ?: "Título predeterminado",
                images = animeDto.images?.webp?.largeImageUrl ?: "URL de imagen predeterminada",
                score = animeDto.score ?: 0.0f,
                status = animeDto.status ?: "Sin estado",
                genres = animeDto.genres ?: emptyList(),
                year = (animeDto.year ?: "N/A").toString(),
                episodes = (animeDto.episodes ?: "N/A").toString()
            )
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

    // --- FUNCIÓN PARA GUARDAR BÚSQUEDA EN HISTORIAL ---
    private fun saveSearchToHistory(query: String) {
        viewModelScope.launch {
            try {
                val searchHistory = SearchHistoryEntity(
                    query = query,
                    timestamp = System.currentTimeMillis(),
                    filterType = "text"
                )
                searchHistoryRepository.insertSearch(searchHistory)
            } catch (e: Exception) {
                Log.e("AnimeSearchVM", "Error al guardar búsqueda: ${e.message}")
            }
        }
    }

    // Función para ejecutar una búsqueda desde el historial
    fun onRecentSearchClicked(query: String) {
        _searchQuery.value = query
        _selectedFilter.value = "Anime"
        _selectedGenreId.value = null
        selectedQuickFilter.value = null
        _selectedFormat.value = null
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
                try {
                    firestoreAnimeRepository.syncAnimeToFirestore(entity)
                } catch (e: Exception) {
                    Log.e("AnimeSearchVM", "Error syncing to Firestore: ${e.message}")
                }
                onSuccess()

            } catch (e: Exception) {
                Log.e("AnimeSearchVM", "Error al guardar anime: ${e.message}")
                onError(e.message ?: "Error desconocido")
            }
        }
    }
}