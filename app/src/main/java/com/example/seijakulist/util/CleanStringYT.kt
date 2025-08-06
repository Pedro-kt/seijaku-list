package com.example.seijakulist.util

fun CleanName(raw: String): String {
    return raw
        .substringAfter(":") // Elimina el número inicial
        .substringBefore("(eps") // Elimina la parte de los episodios
        .trim() // Elimina espacios sobrantes
}