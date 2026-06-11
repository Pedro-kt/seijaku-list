package com.yumedev.seijakulist.ui.screens.home

import com.yumedev.seijakulist.domain.models.Anime

/**
 * Estados de UI para las secciones del Home
 * Diferencia entre carga inicial y actualización de datos
 */
sealed class HomeUiState<out T> {
    /**
     * Estado inicial - Primera carga sin datos
     * Debe mostrar skeleton completo
     */
    object Initial : HomeUiState<Nothing>()

    /**
     * Cargando - Primera carga en progreso
     * Debe mostrar skeleton completo
     */
    object Loading : HomeUiState<Nothing>()

    /**
     * Actualizando - Recargando datos que ya existen
     * Debe mostrar el contenido existente (puede mostrar un indicador sutil)
     */
    data class Refreshing<T>(val data: T) : HomeUiState<T>()

    /**
     * Éxito - Datos disponibles
     */
    data class Success<T>(val data: T) : HomeUiState<T>()

    /**
     * Error sin datos previos
     */
    data class Error(val message: String) : HomeUiState<Nothing>()

    /**
     * Error pero tenemos datos en caché
     */
    data class ErrorWithCache<T>(val data: T, val message: String) : HomeUiState<T>()
}

/**
 * Extension para verificar si debe mostrar skeleton
 */
fun <T> HomeUiState<T>.shouldShowSkeleton(): Boolean {
    return this is HomeUiState.Initial || this is HomeUiState.Loading
}

/**
 * Extension para obtener los datos si existen
 */
fun <T> HomeUiState<T>.getDataOrNull(): T? {
    return when (this) {
        is HomeUiState.Success -> data
        is HomeUiState.Refreshing -> data
        is HomeUiState.ErrorWithCache -> data
        else -> null
    }
}

/**
 * Extension para verificar si está cargando (cualquier tipo)
 */
fun <T> HomeUiState<T>.isLoading(): Boolean {
    return this is HomeUiState.Loading || this is HomeUiState.Refreshing
}

/**
 * Extension para verificar si tiene datos
 */
fun <T> HomeUiState<T>.hasData(): Boolean {
    return getDataOrNull() != null
}
