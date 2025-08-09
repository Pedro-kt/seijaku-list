package com.example.seijakulist.ui.screens.local_anime_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.data.local.entities.AnimeEntity
import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.AnimeDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LocalAnimeDetailViewModel @Inject constructor(
    private val animeRepository: AnimeRepository,
    private val savedStateHandle: SavedStateHandle // Se usa para obtener parámetros de la navegación
) : ViewModel() {

    // Se obtiene el ID del anime de los argumentos de navegación
    private val animeId: Int = savedStateHandle["animeId"] ?: throw IllegalArgumentException("AnimeId es nulo")

    // 1. Define el StateFlow como una propiedad del ViewModel
    // 2. Llama al repositorio para obtener el Flow
    // 3. Usa stateIn para convertir el Flow a StateFlow
    val anime: StateFlow<AnimeEntity?> = animeRepository.getAnimeById(animeId)
        .stateIn(
            scope = viewModelScope,
            // Esto le dice al flujo que permanezca activo mientras la UI lo esté observando
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null // El valor inicial del StateFlow, antes de que lleguen los datos
        )
}