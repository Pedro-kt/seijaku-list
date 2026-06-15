package com.yumedev.seijakulist.domain.usecase.anilist

import com.yumedev.seijakulist.data.repository.AnimeAniListRepository
import com.yumedev.seijakulist.domain.models.Anime
import java.util.Calendar
import javax.inject.Inject

/**
 * UseCase para obtener anime que se emiten en un día específico de la semana
 *
 * @param repository Repositorio de AniList
 */
class GetAiringScheduleByDayAniListUseCase @Inject constructor(
    private val repository: AnimeAniListRepository
) {
    /**
     * Obtiene anime que se emiten en un día específico de la semana
     *
     * @param day Día de la semana: "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"
     * @param page Número de página (default: 1)
     * @return Lista de Anime con información de schedule
     */
    suspend operator fun invoke(day: String, page: Int = 1): List<Anime> {
        // Obtener el timestamp range para el día especificado
        val (startTimestamp, endTimestamp) = getTimestampRangeForDay(day)

        // Obtener anime cards con schedule info
        val animeCards = repository.getAiringScheduleByTimeRange(
            airingAtGreater = startTimestamp,
            airingAtLesser = endTimestamp,
            page = page,
            perPage = 50
        )

        // Convertir AnimeCard a Anime (modelo simple para HomeScreen)
        // Incluimos la información de schedule
        return animeCards.map { card ->
            Anime(
                malId = card.malId,
                title = card.title,
                image = card.images,
                score = card.score,
                airingSchedule = card.airingSchedule
            )
        }
    }

    /**
     * Calcula el rango de timestamps Unix para un día específico de la semana
     * Obtiene el próximo día que coincide con el día solicitado
     *
     * @param day Día de la semana en inglés (monday, tuesday, etc.)
     * @return Par de timestamps (inicio del día, fin del día) en segundos
     */
    private fun getTimestampRangeForDay(day: String): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        val targetDayOfWeek = mapDayStringToCalendarDay(day)
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // Calcular cuántos días faltan para el día objetivo
        var daysUntilTarget = targetDayOfWeek - currentDayOfWeek
        if (daysUntilTarget < 0) {
            daysUntilTarget += 7 // Si ya pasó esta semana, ir a la próxima
        }

        // Avanzar al día objetivo
        calendar.add(Calendar.DAY_OF_YEAR, daysUntilTarget)

        // Inicio del día (00:00:00)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startTimestamp = (calendar.timeInMillis / 1000).toInt()

        // Fin del día (23:59:59)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endTimestamp = (calendar.timeInMillis / 1000).toInt()

        return Pair(startTimestamp, endTimestamp)
    }

    /**
     * Mapea el string del día a la constante de Calendar
     */
    private fun mapDayStringToCalendarDay(day: String): Int {
        return when (day.lowercase()) {
            "sunday" -> Calendar.SUNDAY
            "monday" -> Calendar.MONDAY
            "tuesday" -> Calendar.TUESDAY
            "wednesday" -> Calendar.WEDNESDAY
            "thursday" -> Calendar.THURSDAY
            "friday" -> Calendar.FRIDAY
            "saturday" -> Calendar.SATURDAY
            else -> Calendar.MONDAY // Default
        }
    }
}
