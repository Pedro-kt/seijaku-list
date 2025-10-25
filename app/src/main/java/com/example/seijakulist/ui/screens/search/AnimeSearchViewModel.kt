package com.example.seijakulist.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.domain.models.AnimeCard
import com.example.seijakulist.domain.usecase.GetAnimeSearchUseCase
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
    private val animeRepository: AnimeRepository
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

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

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

    // FUNCIÓN UNIFICADA DE BÚSQUEDA
    fun performSearchOrFilter() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true

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

            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener datos: ${e.localizedMessage ?: "Error desconocido"}"
                _animeList.value = emptyList()

            } finally {
                _isLoading.value = false
            }
        }
    }
}