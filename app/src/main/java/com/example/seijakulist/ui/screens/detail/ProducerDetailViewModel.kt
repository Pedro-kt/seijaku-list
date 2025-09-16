package com.example.seijakulist.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.ProducerDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProducerDetailViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {
    private val _producer = MutableStateFlow<ProducerDetail?>(null)
    val producer: StateFlow<ProducerDetail?> = _producer

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun getProducerDetail(producerId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val producerDetail = animeRepository.getProducerDetail(producerId)
                _producer.value = producerDetail
                _isLoading.value = false
                _errorMessage.value = null
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.message
            }
        }
    }
}