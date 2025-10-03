package com.example.seijakulist.ui.screens.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.data.local.entities.UserProfile
import com.example.seijakulist.data.repository.UserProfileLocalRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.storage
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
    val profilePictureUrl: String?
)

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isUploadingImage: Boolean = false,
    val userProfile: UserProfile? = null,
    val error: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileLocalRepository: UserProfileLocalRepository
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
                    }
            }
        }
    }

    private fun syncProfileFromFirestore(uid: String) = flow<Nothing> {
        try {
            val doc = db.collection("users").document(uid).get().await()
            if (doc.exists()) {
                val data = doc.data
                val remoteProfile = UserProfile(
                    uid = uid,
                    username = data?.get("username") as? String,
                    profilePictureUrl = data?.get("profilePictureUrl") as? String
                )
                userProfileLocalRepository.insertUserProfile(remoteProfile)
            }
        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Error al sincronizar desde Firestore", e)
        }
    }

    // 4. Lógica de actualización del perfil simplificada
    fun updateUserProfile(username: String, imageUri: Uri?) {
        val user = auth.currentUser ?: return

        _uiState.update { it.copy(isLoading = true, error = null) }

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
                    "profilePictureUrl" to (imageUrl ?: existingProfile?.profilePictureUrl ?: "")
                )

                db.collection("users").document(user.uid).set(updates, SetOptions.merge()).await()

                val localProfile = UserProfile(
                    uid = user.uid,
                    username = username,
                    profilePictureUrl = imageUrl ?: existingProfile?.profilePictureUrl
                )
                userProfileLocalRepository.insertUserProfile(localProfile)

            }.onSuccess {
                _uiState.update { it.copy(isLoading = false, isUploadingImage = false) }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isUploadingImage = false,
                        error = throwable.message
                    )
                }
                Log.e("ProfileViewModel", "Error al actualizar el perfil", throwable)
            }
        }
    }
}