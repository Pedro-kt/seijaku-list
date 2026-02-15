package com.yumedev.seijakulist.util

data class CharacterInfo(
    val key: String,
    val value: String
)

data class ParsedCharacterDescription(
    val keyValuePairs: List<CharacterInfo>,
    val cleanDescription: String
)

object CharacterDescriptionParser {

    /**
     * Parsea la descripción del personaje extrayendo pares clave:valor
     * Ejemplos: "Edad: 16 años", "Altura: 170 cm", "Cumpleaños: 5 de Mayo"
     */
    fun parseDescription(description: String): ParsedCharacterDescription {
        if (description.isBlank()) {
            return ParsedCharacterDescription(emptyList(), "")
        }

        val keyValuePairs = mutableListOf<CharacterInfo>()
        val lines = description.split("\n")
        val cleanLines = mutableListOf<String>()

        for (line in lines) {
            val trimmedLine = line.trim()

            // Detectar pares clave:valor
            // Patrones comunes: "Clave: Valor" o "Clave : Valor"
            if (trimmedLine.contains(":")) {
                val parts = trimmedLine.split(":", limit = 2)
                if (parts.size == 2) {
                    val key = parts[0].trim()
                    val value = parts[1].trim()

                    // Validar que la clave no sea muy larga (probablemente no es un par clave:valor)
                    // y que el valor no esté vacío
                    if (key.length <= 30 && value.isNotEmpty() && !key.contains(".")) {
                        // Verificar que la clave parezca ser un atributo (no parte de una oración)
                        val commonKeys = listOf(
                            "edad", "age", "altura", "height", "peso", "weight",
                            "cumpleaños", "birthday", "nacimiento", "birth",
                            "género", "gender", "especie", "species",
                            "ocupación", "occupation", "trabajo", "job",
                            "afiliación", "affiliation", "clan", "rango", "rank",
                            "estado", "status", "tipo de sangre", "blood type",
                            "nacionalidad", "nationality", "residencia", "residence",
                            "familia", "family", "clase", "class", "nivel", "level"
                        )

                        val keyLower = key.lowercase()
                        val isLikelyAttribute = commonKeys.any { keyLower.contains(it) } ||
                                                key.length <= 15

                        if (isLikelyAttribute) {
                            keyValuePairs.add(CharacterInfo(key, value))
                            continue // No agregar esta línea a la descripción limpia
                        }
                    }
                }
            }

            // Si no es un par clave:valor, agregar a la descripción limpia
            if (trimmedLine.isNotEmpty()) {
                cleanLines.add(trimmedLine)
            }
        }

        val cleanDescription = cleanLines.joinToString("\n\n")

        return ParsedCharacterDescription(keyValuePairs, cleanDescription)
    }
}
