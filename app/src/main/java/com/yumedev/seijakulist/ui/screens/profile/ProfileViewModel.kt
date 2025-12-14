package com.yumedev.seijakulist.ui.screens.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.data.local.entities.UserProfile
import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
import com.yumedev.seijakulist.data.repository.UserProfileLocalRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.storage
import com.yumedev.seijakulist.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class UserProfile(
    val uid: String,
    val username: String?,
    val profilePictureUrl: String?,
    val bio: String?
)

data class AnimeStats(
    val totalAnimes: Int = 0,
    val completedAnimes: Int = 0,
    val totalEpisodesWatched: Int = 0,
    val genreStats: Map<String, Int> = emptyMap()
)

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isUploadingImage: Boolean = false,
    val userProfile: UserProfile? = null,
    val error: String? = null,
    val profileUpdateSuccess: Boolean = false,
    val top5Animes: List<AnimeEntity> = emptyList(),
    val allSavedAnimes: List<AnimeEntity> = emptyList(),
    val isSavingTop5: Boolean = false,
    val top5UpdateSuccess: Boolean = false,
    val stats: AnimeStats = AnimeStats()
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileLocalRepository: UserProfileLocalRepository,
    private val animeLocalRepository: AnimeLocalRepository
) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = Firebase.storage

    // 1. El StateFlow que unifica todos los estados de la UI
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        // 2. Lógica de sincronización unificada y optimizada
        viewModelScope.launch {
            auth.currentUser?.let { user ->
                userProfileLocalRepository.getUserProfile(user.uid)
                    .flatMapLatest { localProfile ->
                        // Sincroniza desde Firestore solo si el perfil local no existe o está incompleto.
                        if (localProfile == null || localProfile.username.isNullOrEmpty()) {
                            syncProfileFromFirestore(user.uid)
                        }
                        userProfileLocalRepository.getUserProfile(user.uid)
                    }
                    .collect { userProfile ->
                        // 3. Actualiza el estado de la UI con los nuevos datos
                        _uiState.update { it.copy(userProfile = userProfile) }

                        // Cargar los top 5 animes si están configurados
                        if (!userProfile?.top5AnimeIds.isNullOrBlank()) {
                            val ids = userProfile!!.top5AnimeIds!!.split(",").mapNotNull { it.toIntOrNull() }
                            Log.d("ProfileViewModel", "Loading top 5 from profile: $ids")
                            loadTop5Animes(ids)
                        }
                    }
            }
        }

        // Cargar todos los animes guardados
        viewModelScope.launch {
            animeLocalRepository.getAllAnimes().collect { animes ->
                _uiState.update { it.copy(allSavedAnimes = animes) }
            }
        }

        // Cargar estadísticas
        viewModelScope.launch {
            animeLocalRepository.getTotalAnimesCount().collect { total ->
                _uiState.update { it.copy(stats = it.stats.copy(totalAnimes = total)) }
            }
        }

        viewModelScope.launch {
            animeLocalRepository.getCompletedAnimesCount().collect { completed ->
                _uiState.update { it.copy(stats = it.stats.copy(completedAnimes = completed)) }
            }
        }

        viewModelScope.launch {
            animeLocalRepository.getTotalEpisodesWatched().collect { episodes ->
                _uiState.update { it.copy(stats = it.stats.copy(totalEpisodesWatched = episodes)) }
            }
        }

        viewModelScope.launch {
            animeLocalRepository.getAllGenres().collect { genresList ->
                // Procesar los géneros (separados por comas) y contar cada uno
                val genreMap = mutableMapOf<String, Int>()
                genresList.forEach { genresString ->
                    genresString.split(",").forEach { genre ->
                        val trimmedGenre = genre.trim()
                        if (trimmedGenre.isNotEmpty()) {
                            genreMap[trimmedGenre] = genreMap.getOrDefault(trimmedGenre, 0) + 1
                        }
                    }
                }
                _uiState.update { it.copy(stats = it.stats.copy(genreStats = genreMap)) }
            }
        }
    }

    private fun syncProfileFromFirestore(uid: String) = flow<Nothing> {
        try {
            val doc = db.collection("users").document(uid).get().await()
            if (doc.exists()) {
                val data = doc.data
                val remoteProfile = com.yumedev.seijakulist.data.local.entities.UserProfile(
                    uid = uid,
                    username = data?.get("username") as? String,
                    profilePictureUrl = data?.get("profilePictureUrl") as? String,
                    bio = data?.get("bio") as? String
                )
                userProfileLocalRepository.insertUserProfile(remoteProfile)
            }
        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Error al sincronizar desde Firestore", e)
        }
    }

    // 4. Lógica de actualización del perfil simplificada
    fun updateUserProfile(username: String, bio: String, imageUri: Uri?) {
        val user = auth.currentUser ?: return

        _uiState.update { it.copy(isLoading = true, error = null, profileUpdateSuccess = false) }

        viewModelScope.launch {
            runCatching {
                var imageUrl: String? = null
                if (imageUri != null) {
                    _uiState.update { it.copy(isUploadingImage = true) }
                    val imageRef = storage.reference.child("userProfiles/${user.uid}/profile.jpg")
                    val uploadTask = imageRef.putFile(imageUri).await()
                    imageUrl = uploadTask.storage.downloadUrl.await().toString()
                }

                val existingProfile = _uiState.value.userProfile

                val updates = hashMapOf<String, Any>(
                    "username" to username,
                    "bio" to bio,
                    "profilePictureUrl" to (imageUrl ?: existingProfile?.profilePictureUrl ?: "")
                )

                db.collection("users").document(user.uid).set(updates, SetOptions.merge()).await()

                val localProfile = UserProfile(
                    uid = user.uid,
                    username = username,
                    bio = bio,
                    profilePictureUrl = imageUrl ?: existingProfile?.profilePictureUrl
                )
                userProfileLocalRepository.insertUserProfile(localProfile)

            }.onSuccess {
                _uiState.update { it.copy(isLoading = false, isUploadingImage = false, profileUpdateSuccess = true) }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isUploadingImage = false,
                        error = throwable.message,
                        profileUpdateSuccess = false
                    )
                }
                Log.e("ProfileViewModel", "Error al actualizar el perfil", throwable)
            }
        }
    }

    // Resetear el flag de éxito después de navegar
    fun resetUpdateSuccess() {
        _uiState.update { it.copy(profileUpdateSuccess = false) }
    }

    private suspend fun loadTop5Animes(ids: List<Int>) {
        Log.d("ProfileViewModel", "loadTop5Animes called with IDs: $ids")
        val animes = mutableListOf<AnimeEntity>()
        ids.forEach { id ->
            try {
                val anime = animeLocalRepository.getAnimeById(id)
                animes.add(
                    AnimeEntity(
                        malId = anime.malId,
                        title = anime.title,
                        imageUrl = anime.image,
                        userScore = anime.userScore,
                        statusUser = anime.userStatus,
                        userOpiniun = anime.userOpiniun,
                        totalEpisodes = anime.totalEpisodes,
                        episodesWatched = anime.episodesWatched,
                        rewatchCount = anime.rewatchCount,
                        genres = anime.genres
                    )
                )
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading anime with id $id", e)
            }
        }
        Log.d("ProfileViewModel", "Loaded ${animes.size} animes in order: ${animes.map { it.malId }}")
        _uiState.update { it.copy(top5Animes = animes) }
    }

    fun updateTop5Animes(selectedAnimeIds: List<Int>) {
        val user = auth.currentUser ?: return

        _uiState.update { it.copy(isSavingTop5 = true, top5UpdateSuccess = false, error = null) }

        viewModelScope.launch {
            try {
                Log.d("ProfileViewModel", "Updating top 5 with IDs: $selectedAnimeIds")

                // Actualizar en Room primero
                userProfileLocalRepository.updateTop5AnimeIds(user.uid, selectedAnimeIds)

                // Sincronizar con Firestore usando merge para evitar errores si el documento no existe
                val updates = hashMapOf<String, Any>(
                    "top5AnimeIds" to selectedAnimeIds.joinToString(",")
                )
                db.collection("users").document(user.uid)
                    .set(updates, SetOptions.merge())
                    .await()

                // Cargar los animes con el orden correcto
                loadTop5Animes(selectedAnimeIds)

                Log.d("ProfileViewModel", "Top 5 animes updated successfully")

                _uiState.update { it.copy(isSavingTop5 = false, top5UpdateSuccess = true) }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error updating top 5 animes", e)
                _uiState.update { it.copy(isSavingTop5 = false, error = e.message, top5UpdateSuccess = false) }
            }
        }
    }

    // Resetear el flag de éxito de top 5 después de mostrar
    fun resetTop5UpdateSuccess() {
        _uiState.update { it.copy(top5UpdateSuccess = false) }
    }
}