package com.example.seijakulist.ui.screens.my_animes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.data.local.entities.AnimeEntity
import com.example.seijakulist.data.mapper.local.toAnimeEntity
import com.example.seijakulist.data.repository.AnimeLocalRepository
import com.example.seijakulist.util.UserAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAnimeListViewModel @Inject constructor(
    private val animeLocalRepository: AnimeLocalRepository
) : ViewModel() {

    val savedAnimes: StateFlow<List<AnimeEntity>> =
        animeLocalRepository.getAllAnimes()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun handleUserAction(animeId: Int, action: UserAction) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentAnime = animeLocalRepository.getAnimeById(animeId)

            val updatedAnime = when (action) {

                is UserAction.UpdateProgress -> {
                    val willBeCompleted = action.newProgress >= currentAnime.totalEpisodes
                    val newRewatchCount = if (willBeCompleted && currentAnime.userStatus != "Completado") {
                        if (currentAnime.rewatchCount == 0) 1 else currentAnime.rewatchCount + 1
                    } else {
                        currentAnime.rewatchCount
                    }

                    currentAnime.copy(
                        episodesWatched = action.newProgress,
                        userStatus = if (willBeCompleted) "Completado" else currentAnime.userStatus,
                        rewatchCount = newRewatchCount
                    )
                }

                is UserAction.MarkAsCompleted -> {
                    currentAnime.copy(
                        userStatus = "Completado",
                        episodesWatched = currentAnime.totalEpisodes,
                        rewatchCount = currentAnime.rewatchCount + 1
                    )
                }

                is UserAction.MarkAsPlanned -> {
                    currentAnime.copy(
                        userStatus = "Planeado",
                        episodesWatched = 0
                    )
                }

                is UserAction.MarkAsWatching -> {
                    currentAnime.copy(
                        userStatus = "Viendo",
                        episodesWatched = if (currentAnime.userStatus == "Completado") 0 else currentAnime.episodesWatched
                    )
                }

                is UserAction.MarkAsDropped -> {
                    currentAnime.copy(
                        userStatus = "Abandonado",
                        episodesWatched = if (currentAnime.userStatus == "Completado") 0 else currentAnime.episodesWatched
                    )
                }

                is UserAction.MarkAsPending -> {
                    currentAnime.copy(
                        userStatus = "Pendiente",
                        episodesWatched = if (currentAnime.userStatus == "Completado") 0 else currentAnime.episodesWatched
                    )
                }

                is UserAction.None -> currentAnime
            }

            animeLocalRepository.updateAnime(updatedAnime.toAnimeEntity())
        }
    }

    fun updateEpisodesWatched(animeId: Int, newEpisodesWatched: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentAnime = animeLocalRepository.getAnimeById(animeId)

            // Verifica si el anime se completará con la nueva cantidad de episodios vistos
            val newStatus = if (newEpisodesWatched == currentAnime.totalEpisodes) {
                "Completado"
            } else {
                currentAnime.userStatus
            }

            // Incrementa rewatchCount solo si el anime se completa y no estaba completado antes
            val newRewatchCount = if (newStatus == "Completado" && currentAnime.userStatus != "Completado") {
                currentAnime.rewatchCount + 1
            } else {
                currentAnime.rewatchCount
            }

            val updatedAnime = currentAnime.copy(
                episodesWatched = newEpisodesWatched,
                userStatus = newStatus,
                rewatchCount = newRewatchCount
            )

            val anime = updatedAnime.toAnimeEntity()

            animeLocalRepository.updateAnime(anime)
        }
    }

    fun deleteAnimeToList(animeId: Int) {

        viewModelScope.launch {
            try {

                animeLocalRepository.deleteAnimeById(animeId)

            } catch (e: Exception) {
                Log.e("AnimeDetailVM", "Error al eliminar el anime: ${e.message}")
            }
        }

    }

    val savedAnimeStatusComplete: StateFlow<List<AnimeEntity>> =
        animeLocalRepository.getAnimesStatusComplete()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val savedAnimeStatusWatching: StateFlow<List<AnimeEntity>> =
        animeLocalRepository.getAnimesStatusWatching()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val savedAnimeStatusPending: StateFlow<List<AnimeEntity>> =
        animeLocalRepository.getAnimesStatusPending()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val savedAnimeStatusAbandoned: StateFlow<List<AnimeEntity>> =
        animeLocalRepository.getAnimesStatusAbandoned()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val savedAnimeStatusPlanned: StateFlow<List<AnimeEntity>> =
        animeLocalRepository.getAnimesStatusPlanned()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

}

