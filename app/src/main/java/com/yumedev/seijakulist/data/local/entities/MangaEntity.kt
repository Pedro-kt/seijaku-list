package com.yumedev.seijakulist.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mangas",
    indices = [
        Index(value = ["statusUser"]),
        Index(value = ["lastModified"])
    ]
)
data class MangaEntity(
    @PrimaryKey val malId: Int,

    // Información básica
    val title: String,
    val titleEnglish: String? = null,
    val titleJapanese: String? = null,
    val imageUrl: String? = null,
    val bannerImage: String? = null,

    // Metadatos del manga
    val typeManga: String, // MANGA, NOVEL, ONE_SHOT, MANHWA, MANHUA
    val source: String? = null,
    val chapters: Int? = null,
    val volumes: Int? = null,
    val status: String, // FINISHED, RELEASING, NOT_YET_RELEASED, CANCELLED, HIATUS
    val published: String? = null,

    // Scores y rankings
    val score: Float? = null,
    val scoreBy: Int? = null,
    val rank: Int? = null,

    // Contenido
    val synopsis: String? = null,
    val background: String? = null,
    val genres: String = "", // separados por comas
    val demographics: String? = null, // separados por comas
    val authors: String? = null, // JSON serializado: [{"malId":123,"name":"...","role":"Story"}]
    val serializations: String? = null, // JSON serializado

    // === DATOS DEL USUARIO ===
    val userScore: Float = 0f, // 0-10 con decimales
    val statusUser: String, // "Leyendo", "Completado", "Planeado", "Pausado", "Abandonado"
    val userOpinion: String = "",
    val chaptersRead: Int = 0,
    val volumesRead: Int = 0,
    val rereadCount: Int = 0,

    // Fechas de seguimiento
    val startDate: Long? = null,
    val endDate: Long? = null,

    // Prioridad (solo para "Planeado")
    val plannedPriority: String? = null, // "Alta", "Media", "Baja"
    val plannedNote: String? = null,

    // Timestamp para sincronización
    val lastModified: Long = System.currentTimeMillis()
)
