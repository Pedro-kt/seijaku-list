package com.yumedev.seijakulist.util

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Traduce el nombre de la temporada del inglés al español
 */
fun translateSeason(season: String): String {
    return when (season.lowercase()) {
        "spring" -> "Primavera"
        "summer" -> "Verano"
        "fall", "autumn" -> "Otoño"
        "winter" -> "Invierno"
        else -> season.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

/**
 * Traduce el estado de emisión del anime del inglés al español
 */
fun translateStatus(status: String): String {
    return when (status) {
        "Currently Airing" -> "Actualmente en emisión"
        "Finished Airing" -> "Finalizado"
        "Not yet aired" -> "Próximamente"
        else -> status
    }
}

/**
 * Traduce la duración del formato inglés al español
 * Ejemplo: "23 min per ep" -> "23 min por ep"
 */
fun translateDuration(duration: String): String {
    return duration
        .replace("per ep", "por ep")
        .replace("min", "min")
        .replace("hr", "h")
        .replace("sec", "seg")
}

/**
 * Traduce las fechas de emisión del formato inglés al español
 * Ejemplo: "Apr 6, 2026 to Jun 22, 2026" -> "6 de abril de 2026 a 22 de junio de 2026"
 */
fun translateAiredDates(aired: String): String {
    // Si no contiene "to", puede ser una fecha simple o un formato diferente
    if (!aired.contains(" to ", ignoreCase = true)) {
        return translateSingleDate(aired)
    }

    val parts = aired.split(" to ", ignoreCase = true)
    if (parts.size != 2) return aired

    val startDate = translateSingleDate(parts[0].trim())
    val endDate = translateSingleDate(parts[1].trim())

    return "$startDate a $endDate"
}

/**
 * Traduce una fecha individual del formato inglés al español
 */
private fun translateSingleDate(date: String): String {
    // Si la fecha es "?" o desconocida
    if (date == "?" || date.isBlank()) return date

    // Mapeo de meses en inglés a español
    val monthsMap = mapOf(
        "jan" to "enero",
        "feb" to "febrero",
        "mar" to "marzo",
        "apr" to "abril",
        "may" to "mayo",
        "jun" to "junio",
        "jul" to "julio",
        "aug" to "agosto",
        "sep" to "septiembre",
        "oct" to "octubre",
        "nov" to "noviembre",
        "dec" to "diciembre"
    )

    var translatedDate = date

    // Reemplazar cada mes
    monthsMap.forEach { (eng, esp) ->
        translatedDate = translatedDate.replace(eng, esp, ignoreCase = true)
    }

    // Transformar formato: "6 abril 2026" -> "6 de abril de 2026"
    // Patrón: número + mes + año
    val datePattern = Regex("""(\d+)\s+(\w+)[,\s]+(\d{4})""")
    val match = datePattern.find(translatedDate)

    if (match != null) {
        val day = match.groupValues[1]
        val month = match.groupValues[2]
        val year = match.groupValues[3]
        return "$day de $month de $year"
    }

    // Si solo tiene mes y año: "abril 2026" -> "abril de 2026"
    val monthYearPattern = Regex("""(\w+)[,\s]+(\d{4})""")
    val monthYearMatch = monthYearPattern.find(translatedDate)

    if (monthYearMatch != null) {
        val month = monthYearMatch.groupValues[1]
        val year = monthYearMatch.groupValues[2]
        return "$month de $year"
    }

    return translatedDate
}

/**
 * Capitaliza la primera letra de cada palabra
 */
fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}
