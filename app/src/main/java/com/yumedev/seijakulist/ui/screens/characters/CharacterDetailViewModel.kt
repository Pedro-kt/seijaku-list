package com.yumedev.seijakulist.ui.screens.characters

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.common.RequestThrottler
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.domain.models.CharacterDetail
import com.yumedev.seijakulist.domain.usecase.anilist.GetCharacterDetailAniListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CharacterDetailViewModel @Inject constructor(

    private val getCharacterDetailAniListUseCase: GetCharacterDetailAniListUseCase,
    private val requestThrottler: RequestThrottler

) : ViewModel() {
    private val _characterDetail: MutableStateFlow<CharacterDetail> = MutableStateFlow(CharacterDetail())
    val characterDetail: StateFlow<CharacterDetail> = _characterDetail.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadCharacterDetail(characterId: Int) {

        _errorMessage.value = null
        _isLoading.value = true

        Log.d("CharDetailVM", "loadCharacterDetail llamado con ID: $characterId")

        viewModelScope.launch {
            try {
                Log.d("CharDetailVM", "Fetching character from AniList...")
                val detail = requestThrottler.throttle {
                    getCharacterDetailAniListUseCase(characterId)
                }

                if (detail != null) {
                    Log.d("CharDetailVM", "Character loaded successfully: ${detail.nameCharacter}")
                    _characterDetail.value = detail
                } else {
                    Log.e("CharDetailVM", "Detail returned null")
                    _characterDetail.value = CharacterDetail()
                }

            } catch (e: Exception) {

                Log.e("CharDetailVM", "Error al cargar detalles del personaje: ${e.message}", e)
                Log.e("CharDetailVM", "Stack trace: ${e.stackTraceToString()}")
                _errorMessage.value = "Lo sentimos, parece que algo salió mal al buscar informacion del personaje, posiblemente no contenga información"
                _characterDetail.value = CharacterDetail()

            } finally {

                _isLoading.value = false
                Log.d("CharDetailVM", "Loading finished. isLoading: false")

            }
        }
    }
}
