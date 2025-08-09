package com.example.seijakulist.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.domain.models.CharacterDetail
import com.example.seijakulist.domain.usecase.GetCharacterRandomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterRandomViewModel @Inject constructor(
    private val getCharacterRandomUseCase: GetCharacterRandomUseCase
) : ViewModel() {
    private val _uiCharacterState = MutableStateFlow(CharacterRandomUiState())
    val uiCharacterState: StateFlow<CharacterRandomUiState> = _uiCharacterState.asStateFlow()

    init {
        loadCharacterRandom()
    }

    fun loadCharacterRandom() {

        viewModelScope.launch {

            _uiCharacterState.update { it.copy(isLoading = true, errorMessage = null, isDataLoaded = false) }

            try {

                val detail = getCharacterRandomUseCase()

                _uiCharacterState.update {
                    it.copy(
                        character = detail,
                        isLoading = false,
                        isDataLoaded = true
                    )
                }

            } catch (e: Exception) {

                _uiCharacterState.update {
                    it.copy(
                        errorMessage = "Error al buscar detalles del personaje: ${e.localizedMessage ?: "Error desconocido"}"
                    )
                }

            }
        }
    }
}

data class CharacterRandomUiState(
    val character: CharacterDetail? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isDataLoaded: Boolean = false
)