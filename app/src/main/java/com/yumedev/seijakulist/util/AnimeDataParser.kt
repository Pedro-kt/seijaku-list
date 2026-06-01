package com.yumedev.seijakulist.util

/**
 * Extrae el año desde un string de fecha aired.
 *
 * Formatos soportados:
 * - "jan 19, 2010"
 * - "apr 3, 2009 to jun 26, 2009"
 * - "2010"
 *
 * @param airedString String con la fecha de emisión
 * @return El año extraído o null si no se puede parsear
 */
fun extractYearFromAired(airedString: String?): Int? {
    if (airedString.isNullOrBlank()) return null

    // Regex para extraer un año de 4 dígitos
    val yearRegex = Regex("""\b(\d{4})\b""")
    val matchResult = yearRegex.find(airedString)

    return matchResult?.groupValues?.get(1)?.toIntOrNull()
}

/**
 * Obtiene el año para mostrar en la UI, priorizando el campo year,
 * y si es null, extrae desde el campo aired.
 *
 * @param year Año directo del campo year
 * @param airedString String de fecha aired
 * @return String del año o "N/A" si no se puede obtener
 */
fun getDisplayYear(year: Int?, airedString: String?): String {
    return when {
        year != null && year > 0 -> year.toString()
        else -> extractYearFromAired(airedString)?.toString() ?: "N/A"
    }
}

/**
 * Obtiene el display de episodios, manejando casos especiales según el tipo.
 *
 * @param episodes Número de episodios
 * @param type Tipo de anime (TV, Movie, OVA, etc.)
 * @param status Estado del anime (Currently Airing, Finished Airing, etc.)
 * @return String formateado para mostrar episodios
 */
fun getDisplayEpisodes(episodes: Int?, type: String?, status: String?): String {
    return when {
        // Movies siempre tienen 1 episodio
        type?.equals("Movie", ignoreCase = true) == true -> "1"

        // Si tiene episodios definidos, mostrarlos
        episodes != null && episodes > 0 -> episodes.toString()

        // Si está en emisión y no tiene episodios, mostrar "?"
        status?.contains("Airing", ignoreCase = true) == true -> "?"

        // Si es Special, OVA, ONA sin episodios definidos
        type in listOf("Special", "OVA", "ONA") -> "?"

        // Default
        else -> "?"
    }
}
