package com.yumedev.seijakulist.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.data.local.entities.SearchHistoryEntity
import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
import com.yumedev.seijakulist.data.repository.FirestoreAnimeRepository
import com.yumedev.seijakulist.data.repository.SearchHistoryRepository
import com.yumedev.seijakulist.domain.models.AnimeCard
import com.yumedev.seijakulist.domain.usecase.anilist.GetAnimeSearchAniListUseCase
import com.yumedev.seijakulist.domain.usecase.anilist.GetMangaSearchAniListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Constants
private const val MIN_QUERY_LENGTH = 2
private const val PREVIEW_ITEMS_COUNT = 4
private const val DEBOUNCE_DELAY_MS = 300L
private const val PAGE_SIZE = 25

@HiltViewModel
class AnimeSearchViewModel @Inject constructor(
    private val getAnimeSearchAniListUseCase: GetAnimeSearchAniListUseCase,
    private val getMangaSearchAniListUseCase: GetMangaSearchAniListUseCase,
    private val animeAniListRepository: AnimeAniListRepository,
    private val animeLocalRepository: AnimeLocalRepository,
    private val firestoreAnimeRepository: FirestoreAnimeRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {

    // Single consolidated state
    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    // Set to track loaded anime IDs for efficient deduplication
    private val loadedAnimeIds = mutableSetOf<Int>()

    init {
        // Vista previa de resultados mientras el usuario escribe (debounce 300ms)
        viewModelScope.launch {
            _state
                .debounce(DEBOUNCE_DELAY_MS)
                .collect { state ->
                    if (state.searchQuery.isNotBlank() && state.searchQuery.length >= MIN_QUERY_LENGTH) {
                        fetchPreviewResults(state.searchQuery, state.selectedFormat)
                    } else {
                        _state.update { it.copy(previewResults = emptyList()) }
                    }
                }
        }

        // Cargar búsquedas recientes
        viewModelScope.launch {
            searchHistoryRepository.getRecentSearches().collect { searches ->
                _state.update { it.copy(recentSearches = searches.map { search -> search.query }) }
            }
        }
    }

    /**
     * Lazy load trending animes (called when SearchScreen opens)
     */
    fun loadTrendingAnimes() {
        // Only load if not already loaded
        if (_state.value.trendingAnimes.isNotEmpty()) return

        viewModelScope.launch {
            try {
                val popularAnimes = animeAniListRepository.getAnimePopular(page = 1)
                _state.update { it.copy(trendingAnimes = popularAnimes.take(3).map { anime -> anime.title }) }
            } catch (e: Exception) {
                Log.e("AnimeSearchVM", "Error al cargar tendencias: ${e.message}")
                // Fallback a tendencias estáticas si falla la API
                _state.update { it.copy(trendingAnimes = listOf("Naruto", "Jujutsu Kaisen", "One Piece")) }
            }
        }
    }

    // --- ACCIONES ---

    fun onSearchQueryChanged(newQuery: String) {
        _state.update {
            it.copy(
                searchQuery = newQuery,
                // Mantener el filtro seleccionado actual (Anime, Manga o Personajes)
                selectedFilter = when (it.selectedFilter) {
                    "Anime", "Manga", "Personajes" -> it.selectedFilter
                    else -> "Anime" // Por defecto si no hay filtro válido
                },
                selectedGenreId = null,
                selectedQuickFilter = null,
                animeList = if (newQuery.isBlank()) emptyList() else it.animeList,
                characterList = if (newQuery.isBlank()) emptyList() else it.characterList,
                previewResults = if (newQuery.isBlank()) emptyList() else it.previewResults
            )
        }
    }

    fun onQuickFilterSelected(key: String?) {
        _state.update {
            it.copy(
                selectedQuickFilter = key,
                selectedFormat = null,
                searchQuery = "",
                selectedGenreId = null
            )
        }
    }

    fun onFormatSelected(format: String?) {
        _state.update {
            it.copy(
                selectedFormat = format,
                selectedQuickFilter = null,
                selectedGenreId = null
            )
        }
        // Si hay query activa, re-ejecutar la búsqueda con el nuevo filtro
        if (_state.value.searchQuery.isNotBlank()) {
            performSearchOrFilter()
        }
    }

    fun onFilterSelected(filter: String?) {
        _state.update {
            it.copy(
                selectedFilter = filter,
                searchQuery = "",
                selectedGenreId = null, // Clear genre when changing filter type
                selectedQuickFilter = null,
                selectedFormat = null,
                selectedCharacterSort = if (filter == "Personajes") "Populares" else null, // Set default sort for characters
                // Update mediaType when switching between Anime and Manga
                mediaType = if (filter == "Anime" || filter == "Manga") filter else it.mediaType
            )
        }
    }

    fun onCharacterSortSelected(sort: String?) {
        _state.update {
            it.copy(
                selectedCharacterSort = sort,
                searchQuery = ""
            )
        }
        // Si hay un filtro seleccionado, re-ejecutar la búsqueda
        if (sort != null) {
            performSearchOrFilter()
        }
    }

    fun onGenreSelected(genreId: String) {
        _state.update {
            it.copy(
                searchQuery = "",
                selectedGenreId = if (it.selectedGenreId == genreId) null else genreId,
                selectedQuickFilter = null,
                selectedFormat = null
            )
        }
    }

    fun clearSearch() {
        _state.value = SearchState(
            recentSearches = _state.value.recentSearches,
            trendingAnimes = _state.value.trendingAnimes,
            selectedFilter = _state.value.selectedFilter // Maintain current filter tab
        )
        loadedAnimeIds.clear()
    }

    fun clearAllFilters() {
        clearSearch()
    }

    fun clearGenreFilter() {
        _state.update {
            it.copy(
                selectedGenreId = null,
                selectedFilter = if (it.selectedFilter == "Géneros") "Anime" else it.selectedFilter
            )
        }
    }

    // FUNCIÓN UNIFICADA DE BÚSQUEDA (reinicia la paginación)
    fun performSearchOrFilter() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null, currentPage = 1, hasMorePages = true) }
            loadedAnimeIds.clear()

            try {
                val results = fetchSearchResults(page = 1)

                // Add IDs to the set for deduplication
                results.forEach { loadedAnimeIds.add(it.malId) }

                _state.update {
                    it.copy(
                        animeList = results,
                        hasMorePages = results.size >= PAGE_SIZE,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        errorMessage = "Error al obtener datos: ${e.localizedMessage ?: "Error desconocido"}",
                        animeList = emptyList(),
                        isLoading = false
                    )
                }
            }
        }
    }

    // FUNCIÓN PARA CARGAR MÁS RESULTADOS (siguiente página)
    fun loadMoreAnimes() {
        val currentState = _state.value

        // Solo cargar más si no estamos ya cargando y hay más páginas
        if (currentState.isLoadingMore || !currentState.hasMorePages || currentState.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoadingMore = true) }

            try {
                val nextPage = currentState.currentPage + 1
                val results = fetchSearchResults(page = nextPage)

                if (results.isNotEmpty()) {
                    // Efficient deduplication using Set
                    val newResults = results.filter { loadedAnimeIds.add(it.malId) }

                    _state.update {
                        it.copy(
                            currentPage = nextPage,
                            animeList = it.animeList + newResults,
                            hasMorePages = results.size >= PAGE_SIZE,
                            isLoadingMore = false
                        )
                    }
                } else {
                    _state.update { it.copy(hasMorePages = false, isLoadingMore = false) }
                }
            } catch (e: Exception) {
                Log.e("AnimeSearchVM", "Error al cargar más animes: ${e.message}")
                _state.update { it.copy(hasMorePages = false, isLoadingMore = false) }
            }
        }
    }

    /**
     * Centralized search logic - used by both performSearchOrFilter and loadMoreAnimes
     * Now supports characters in addition to anime/manga
     */
    private suspend fun fetchSearchResults(page: Int): List<AnimeCard> {
        val currentState = _state.value

        // Special case: Character search returns empty AnimeCard list (characters are fetched separately)
        if (currentState.selectedFilter == "Personajes") {
            fetchCharacterResults(page)
            return emptyList()
        }

        return when {
            // 1. Si hay un quick filter seleccionado
            currentState.selectedQuickFilter != null -> {
                when (currentState.selectedQuickFilter) {
                    "airing" -> animeAniListRepository.getAiringAnime(page = page)
                    "trending" -> animeAniListRepository.getTrendingAnime(page = page)
                    "top" -> animeAniListRepository.getTopAnime(page = page)
                    "upcoming" -> animeAniListRepository.getUpcomingAnime(page = page)
                    "season" -> animeAniListRepository.getCurrentSeasonAnime(page = page)
                    "new" -> animeAniListRepository.getAnimeNew(page = page)
                    "popular" -> animeAniListRepository.getAnimePopular(page = page)
                    else -> emptyList()
                }
            }
            // 2. Si hay búsqueda de texto de ANIME (con o sin formato)
            currentState.searchQuery.isNotBlank() && currentState.selectedFilter == "Anime" -> {
                // Guardar la búsqueda en el historial solo en la primera página
                if (page == 1) {
                    saveSearchToHistory(currentState.searchQuery)
                }
                val formatType = currentState.selectedFormat?.let { mapFormatToType(it) }
                getAnimeSearchAniListUseCase(query = currentState.searchQuery, page = page, type = formatType)
            }
            // 2b. Si hay búsqueda de texto de MANGA
            currentState.searchQuery.isNotBlank() && currentState.selectedFilter == "Manga" -> {
                // Guardar la búsqueda en el historial solo en la primera página
                if (page == 1) {
                    saveSearchToHistory(currentState.searchQuery)
                }
                val formatType = currentState.selectedFormat?.let { mapMangaFormatToType(it) }
                getMangaSearchAniListUseCase(query = currentState.searchQuery, page = page, type = formatType)
            }
            // 3. Si hay un género seleccionado (sin búsqueda de texto)
            currentState.selectedGenreId != null && currentState.searchQuery.isBlank() -> {
                // Convertir malId a nombre de género para AniList
                val genreName = com.yumedev.seijakulist.domain.models.PopularGenres.getGenreById(
                    currentState.selectedGenreId.toIntOrNull() ?: 0
                )?.name ?: currentState.selectedGenreId

                // Use mediaType to determine if we should search anime or manga by genre
                if (currentState.mediaType == "Manga") {
                    animeAniListRepository.searchMangaAdvanced(genre = genreName, page = page)
                } else {
                    animeAniListRepository.getAnimeByGenre(genreName, page = page)
                }
            }
            else -> emptyList()
        }
    }

    /**
     * Fetches character search results based on current filters
     */
    private suspend fun fetchCharacterResults(page: Int) {
        val currentState = _state.value

        try {
            val sort = when (currentState.selectedCharacterSort) {
                "Populares" -> com.yumedev.seijakulist.data.remote.graphql.type.CharacterSort.FAVOURITES_DESC
                "Relevancia" -> com.yumedev.seijakulist.data.remote.graphql.type.CharacterSort.RELEVANCE
                "Favoritos" -> com.yumedev.seijakulist.data.remote.graphql.type.CharacterSort.FAVOURITES_DESC
                "Cumpleaños" -> com.yumedev.seijakulist.data.remote.graphql.type.CharacterSort.FAVOURITES_DESC
                else -> com.yumedev.seijakulist.data.remote.graphql.type.CharacterSort.FAVOURITES_DESC
            }

            val isBirthday = currentState.selectedCharacterSort == "Cumpleaños"

            val characters = animeAniListRepository.searchCharacters(
                query = if (currentState.searchQuery.isNotBlank()) currentState.searchQuery else null,
                page = page,
                sort = sort,
                isBirthday = if (isBirthday) true else null
            )

            // Update character list in state
            if (page == 1) {
                _state.update {
                    it.copy(
                        characterList = characters,
                        hasMorePages = characters.size >= PAGE_SIZE
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        characterList = it.characterList + characters,
                        hasMorePages = characters.size >= PAGE_SIZE
                    )
                }
            }

            // Save search to history if searching by name
            if (currentState.searchQuery.isNotBlank() && page == 1) {
                saveSearchToHistory(currentState.searchQuery)
            }
        } catch (e: Exception) {
            Log.e("AnimeSearchVM", "Error fetching characters: ${e.message}")
            throw e
        }
    }

    // Mapea los formatos de UI a los tipos de la API (para anime)
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

    // Mapea los formatos de UI a los tipos de la API (para manga)
    private fun mapMangaFormatToType(format: String): String {
        return when (format) {
            "Manga" -> "manga"
            "Novel" -> "novel"
            "One Shot" -> "one_shot"
            "Manhwa" -> "manga"  // Manhwa es técnicamente manga coreano, se busca como manga
            "Manhua" -> "manga"  // Manhua es manga chino, se busca como manga
            else -> format.lowercase()
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
        _state.update {
            it.copy(
                searchQuery = query,
                selectedFilter = "Anime",
                selectedGenreId = null,
                selectedQuickFilter = null,
                selectedFormat = null
            )
        }
    }

    // Función para eliminar una búsqueda del historial
    fun deleteRecentSearch(query: String) {
        viewModelScope.launch {
            try {
                searchHistoryRepository.deleteSearchByQuery(query)
            } catch (e: Exception) {
                Log.e("AnimeSearchVM", "Error al eliminar búsqueda: ${e.message}")
            }
        }
    }

    // Función para obtener vista previa de resultados (solo 4 items)
    private fun fetchPreviewResults(query: String, selectedFormat: String?) {
        viewModelScope.launch {
            try {
                val currentFilter = _state.value.selectedFilter

                when (currentFilter) {
                    "Personajes" -> {
                        // Vista previa de personajes
                        val characterResults = animeAniListRepository.searchCharacters(
                            query = query,
                            page = 1,
                            perPage = PREVIEW_ITEMS_COUNT
                        )
                        _state.update { it.copy(characterPreviewResults = characterResults) }
                    }
                    "Manga" -> {
                        val formatType = selectedFormat?.let { mapMangaFormatToType(it) }
                        val results = getMangaSearchAniListUseCase(query = query, page = 1, type = formatType)
                        _state.update { it.copy(previewResults = results.take(PREVIEW_ITEMS_COUNT)) }
                    }
                    else -> { // "Anime" o cualquier otro
                        val formatType = selectedFormat?.let { mapFormatToType(it) }
                        val results = getAnimeSearchAniListUseCase(query = query, page = 1, type = formatType)
                        _state.update { it.copy(previewResults = results.take(PREVIEW_ITEMS_COUNT)) }
                    }
                }
            } catch (e: Exception) {
                Log.e("AnimeSearchVM", "Error en vista previa: ${e.message}")
                _state.update { it.copy(previewResults = emptyList(), characterPreviewResults = emptyList()) }
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

    // Backward compatibility properties - delegates to state
    val searchQuery: StateFlow<String> get() = MutableStateFlow("").also {
        viewModelScope.launch { state.collect { s -> it.value = s.searchQuery } }
    }
    val selectedFilter: StateFlow<String?> get() = MutableStateFlow<String?>(null).also {
        viewModelScope.launch { state.collect { s -> it.value = s.selectedFilter } }
    }
    val selectedGenreId: StateFlow<String?> get() = MutableStateFlow<String?>(null).also {
        viewModelScope.launch { state.collect { s -> it.value = s.selectedGenreId } }
    }
    val animeList: StateFlow<List<AnimeCard>> get() = MutableStateFlow<List<AnimeCard>>(emptyList()).also {
        viewModelScope.launch { state.collect { s -> it.value = s.animeList } }
    }
    val isLoading: StateFlow<Boolean> get() = MutableStateFlow(false).also {
        viewModelScope.launch { state.collect { s -> it.value = s.isLoading } }
    }
    val isLoadingMore: StateFlow<Boolean> get() = MutableStateFlow(false).also {
        viewModelScope.launch { state.collect { s -> it.value = s.isLoadingMore } }
    }
    val errorMessage: StateFlow<String?> get() = MutableStateFlow<String?>(null).also {
        viewModelScope.launch { state.collect { s -> it.value = s.errorMessage } }
    }
    val selectedQuickFilter: MutableStateFlow<String?> get() = MutableStateFlow<String?>(null).also {
        viewModelScope.launch { state.collect { s -> it.value = s.selectedQuickFilter } }
    }
    val selectedFormat: StateFlow<String?> get() = MutableStateFlow<String?>(null).also {
        viewModelScope.launch { state.collect { s -> it.value = s.selectedFormat } }
    }
    val recentSearches: StateFlow<List<String>> get() = MutableStateFlow<List<String>>(emptyList()).also {
        viewModelScope.launch { state.collect { s -> it.value = s.recentSearches } }
    }
    val trendingAnimes: StateFlow<List<String>> get() = MutableStateFlow<List<String>>(emptyList()).also {
        viewModelScope.launch { state.collect { s -> it.value = s.trendingAnimes } }
    }
    val previewResults: StateFlow<List<AnimeCard>> get() = MutableStateFlow<List<AnimeCard>>(emptyList()).also {
        viewModelScope.launch { state.collect { s -> it.value = s.previewResults } }
    }
}
