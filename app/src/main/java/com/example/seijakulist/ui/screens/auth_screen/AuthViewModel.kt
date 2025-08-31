package com.example.seijakulist.ui.screens.auth_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
                auth.createUserWithEmailAndPassword(email, password).await()
                _authResult.value = AuthResult.Success
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}