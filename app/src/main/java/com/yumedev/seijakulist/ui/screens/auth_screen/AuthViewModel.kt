package com.yumedev.seijakulist.ui.screens.auth_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
    object Idle : AuthResult()
}

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

    private val _authResult = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val authResult = _authResult.asStateFlow()

    fun signIn(email: String, password: String) {
        _authResult.value = AuthResult.Loading
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
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
                        //añadir mas datos adelante si es necesario para el usuario
                    )

                    // Guarda los datos en una colección "users" usando el UID como ID del documento
                    db.collection("users").document(user.uid).set(profileData).await()
                }

                _authResult.value = AuthResult.Success
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error(e.message ?: "Error desconocido")
            }
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
                // Actualiza el documento del perfil del usuario con el nombre de usuario
                val profileData = hashMapOf(
                    "username" to username,
                )
                db.collection("users").document(user.uid).update(profileData as Map<String, Any>).await()
                _authResult.value = AuthResult.Success
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error(e.message ?: "Error al guardar el perfil")
            }
        }
    }

    fun createUserProfileInFirestore() {
        val user = auth.currentUser
        if (user == null) {
            // Maneja el caso en que no haya un usuario autenticado
            return
        }

        viewModelScope.launch {
            try {
                // Prepara los datos básicos del perfil
                val profileData = hashMapOf(
                    "email" to user.email,
                    "createdAt" to System.currentTimeMillis()
                    // Si tienes un nombre de usuario de la pantalla de registro, puedes agregarlo aquí
                    // "username" to "nombre_de_usuario"
                )

                // Guarda el documento en la colección "users" usando el UID del usuario como ID del documento
                db.collection("users").document(user.uid).set(profileData).await()

                // Si tienes que hacer más cosas, aquí podrías actualizar el estado
                // _authResult.value = AuthResult.Success

            } catch (e: Exception) {
                // Maneja los errores de Firestore
                println("Error al crear el perfil en Firestore: ${e.message}")
            }
        }
    }
}