package com.example.seijakulist.ui.screens.detail

import android.util.Log
import androidx.compose.runtime.MutableFloatState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.data.local.entities.AnimeEntity
import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.AnimeDetail
import com.example.seijakulist.domain.usecase.GetAnimeDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase,
    private val repository: AnimeRepository
) : ViewModel() {

    private val _animeDetail: MutableStateFlow<AnimeDetail?> = MutableStateFlow(null)
    val animeDetail: StateFlow<AnimeDetail?> = _animeDetail.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isAdded = MutableStateFlow(false)
    val isAdded: StateFlow<Boolean> = _isAdded.asStateFlow()

    fun loadAnimeDetail(animeId: Int) {
        _errorMessage.value = null
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val detail = getAnimeDetailUseCase(animeId)
                _animeDetail.value = detail
            } catch (e: Exception) {
                _errorMessage.value = "Error al buscar detalles del anime: ${e.localizedMessage ?: "Error desconocido"}"
                _animeDetail.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addAnimeToList() {
        val current = _animeDetail.value ?: return

        viewModelScope.launch {
            try {
                val entity = AnimeEntity(
                    malId = current.malId,
                    title = current.title,
                    imageUrl = current.images,
                    score = current.score,
                )
                repository.insertAnime(entity)
                _isAdded.value = true
            } catch (e: Exception) {
                Log.e("AnimeDetailVM", "Error al guardar anime: ${e.message}")
            }
        }
    }

    fun deleteAnimeToList(animeId: Int) {

        viewModelScope.launch {
            try {

                repository.deleteAnimeById(animeId)

            } catch (e: Exception) {
                Log.e("AnimeDetailVM", "Error al eliminar el anime: ${e.message}")
            }
        }

    }
}