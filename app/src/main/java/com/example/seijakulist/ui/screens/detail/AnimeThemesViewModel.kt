package com.example.seijakulist.ui.screens.detail

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.domain.models.AnimeCharactersDetail
import com.example.seijakulist.domain.models.AnimeThemes
import com.example.seijakulist.domain.usecase.GetAnimeThemesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeThemesViewModel @Inject constructor(

    private val getAnimeThemesUseCase: GetAnimeThemesUseCase

) : ViewModel() {

    private val _themes = MutableStateFlow(AnimeThemes(
        openings = emptyList(),
        endings = emptyList()
    ))

    val themes: StateFlow<AnimeThemes> = _themes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    fun openYoutubeSearch(context: Context, query: String) {
        val intent = Intent(Intent.ACTION_SEARCH).apply {
            setPackage("com.google.android.youtube")
            putExtra("query", query)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=$query"))
            context.startActivity(webIntent)
        }
    }


    fun animeThemes(animeId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _themes.value = getAnimeThemesUseCase(animeId)
            } catch (e: Exception) {
                _errorMessage.value = "Ups! Algo salió mal al cargar los opening / ending del anime."
            } finally {
                _isLoading.value = false
            }
        }
    }

}