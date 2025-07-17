package com.example.seijakulist.ui.screens.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.domain.models.AnimeDetail
import com.example.seijakulist.domain.usecase.GetAnimeCharactersDetailUseCase
import com.example.seijakulist.domain.usecase.GetAnimeDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AnimeDetailViewModel @Inject constructor(

    private val getAnimeDetailUseCase: GetAnimeDetailUseCase

) : ViewModel() {
    private val _animeDetail: MutableStateFlow<AnimeDetail?> = MutableStateFlow(null)
    val animeDetail: StateFlow<AnimeDetail?> = _animeDetail.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadAnimeDetail(animeId: Int) {

        _errorMessage.value = null
        _isLoading.value = true

        Log.d("AnimeDetailVM", "loadAnimeDetail llamado con ID: $animeId")

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
}