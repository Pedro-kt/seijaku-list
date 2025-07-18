package com.example.seijakulist.ui.screens.characters

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.domain.models.AnimeDetail
import com.example.seijakulist.domain.models.CharacterDetail
import com.example.seijakulist.domain.usecase.GetCharacterDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CharacterDetailViewModel @Inject constructor(

    private val getCharacterDetailUseCase: GetCharacterDetailUseCase

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

                val detail = getCharacterDetailUseCase(characterId)
                _characterDetail.value = detail

            } catch (e: Exception) {

                _errorMessage.value = "Error al buscar detalles del personaje: ${e.localizedMessage ?: "Error desconocido"}"
                _characterDetail.value = CharacterDetail()

            } finally {

                _isLoading.value = false

            }
        }
    }
}
