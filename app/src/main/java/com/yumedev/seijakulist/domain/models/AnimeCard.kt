package com.yumedev.seijakulist.domain.models

import com.yumedev.seijakulist.data.remote.models.GenreDto
import com.yumedev.seijakulist.data.remote.models.ImagesDto
import com.google.gson.annotations.SerializedName

data class AnimeCard(
    val malId: Int,
    val title: String,
    val images: String,
    val score: Float,
    val status: String,
    val type: String,
    val genres: List<GenreDto?>,
    val year: String,
    val episodes: String,
    val airingSchedule: AiringScheduleInfo? = null
)

/**
 * Información de horario de emisión para anime en broadcasting
 */
data class AiringScheduleInfo(
    val airingAt: Long,           // Timestamp Unix (segundos)
    val episode: Int,              // Número del episodio que se emitirá
    val timeUntilAiring: Long,     // Segundos hasta la emisión
    val dayOfWeek: String,         // Día de la semana (ej: "Monday", "Tuesday")
    val formattedTime: String      // Hora formateada (ej: "15:30")
)