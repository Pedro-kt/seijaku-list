package com.example.seijakulist.ui.screens.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.domain.models.CharacterDetail
import com.example.seijakulist.domain.models.CharacterPictures
import com.example.seijakulist.domain.usecase.GetCharacterPicturesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterPictureViewModel @Inject constructor(

    private val getCharacterPicturesUseCase: GetCharacterPicturesUseCase

) : ViewModel() {

    private val _characterPictures = MutableStateFlow<List<CharacterPictures>>(emptyList())
    val characterPictures: StateFlow<List<CharacterPictures>> = _characterPictures.asStateFlow()

    private val _isLoadingPicture = MutableStateFlow(false)
    val isLoadingPicture: StateFlow<Boolean> = _isLoadingPicture.asStateFlow()

    private val _errorMessagePicture = MutableStateFlow<String?>(null)
    val errorMessagePicture: StateFlow<String?> = _errorMessagePicture.asStateFlow()

    fun loadCharacterPictures(characterId: Int) {

        viewModelScope.launch {

            _errorMessagePicture.value = null
            _isLoadingPicture.value = true

            try {

                val results = getCharacterPicturesUseCase(characterId)
                _characterPictures.value = results

            } catch (e: Exception) {

                _errorMessagePicture.value = "Error al buscar las fotos de los personaje: ${e.localizedMessage ?: "Error desconocido"}"
                _characterPictures.value = emptyList()

            } finally {

                _isLoadingPicture.value = false

            }
        }
    }

}