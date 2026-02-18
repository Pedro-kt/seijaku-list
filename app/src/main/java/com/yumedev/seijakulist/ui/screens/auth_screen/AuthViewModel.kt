package com.yumedev.seijakulist.ui.screens.auth_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
import com.yumedev.seijakulist.data.repository.FirestoreAnimeRepository
import com.yumedev.seijakulist.data.repository.toAnimeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
    object Idle : AuthResult()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firestoreAnimeRepository: FirestoreAnimeRepository,
    private val animeLocalRepository: AnimeLocalRepository
) : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

    private val _authResult = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val authResult = _authResult.asStateFlow()

    fun signIn(email: String, password: String) {
        _authResult.value = AuthResult.Loading
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                syncFirestoreToRoom()
                _authResult.value = AuthResult.Success
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun signUp(email: String, password: String) {
        _authResult.value = AuthResult.Loading
        viewModelScope.launch {
            try {
                val userCredential = auth.createUserWithEmailAndPassword(email, password).await()
                val user = userCredential.user

                if (user != null) {
                    val profileData = hashMapOf(
                        "email" to user.email,
                        "createdAt" to System.currentTimeMillis()
                    )
                    db.collection("users").document(user.uid).set(profileData).await()
                }

                syncFirestoreToRoom()
                _authResult.value = AuthResult.Success
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error(e.message ?: "Error desconocido")
            }
        }
    }

    private suspend fun syncFirestoreToRoom() {
        try {
            val firestoreAnimes = firestoreAnimeRepository.fetchAllAnimesFromFirestore()
            if (firestoreAnimes.isNotEmpty()) {
                val entities = firestoreAnimes.map { it.toAnimeEntity() }
                animeLocalRepository.insertAllAnimes(entities)
                Log.d("AuthViewModel", "Sync: ${entities.size} animes importados de Firestore a Room")
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Sync fallida (auth exitoso): ${e.message}")
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun updateUserProfile(username: String) {
        val user = auth.currentUser
        if (user == null) {
            _authResult.value = AuthResult.Error("No hay usuario autenticado.")
            return
        }

        _authResult.value = AuthResult.Loading
        viewModelScope.launch {
            try {
                val profileData = hashMapOf("username" to username)
                db.collection("users").document(user.uid).update(profileData as Map<String, Any>).await()
                _authResult.value = AuthResult.Success
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error(e.message ?: "Error al guardar el perfil")
            }
        }
    }

    fun createUserProfileInFirestore() {
        val user = auth.currentUser ?: return
        viewModelScope.launch {
            try {
                val profileData = hashMapOf(
                    "email" to user.email,
                    "createdAt" to System.currentTimeMillis()
                )
                db.collection("users").document(user.uid).set(profileData).await()
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error al crear el perfil en Firestore: ${e.message}")
            }
        }
    }
}