package com.yumedev.seijakulist.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yumedev.seijakulist.data.local.entities.MangaEntity
import com.yumedev.seijakulist.domain.models.AuthorDto
import com.yumedev.seijakulist.domain.models.MangaDetail
import com.yumedev.seijakulist.domain.models.MangaEntityDomain
import com.yumedev.seijakulist.domain.models.SerializationDto

private val gson = Gson()

/**
 * Convierte MangaDetail (de la API) a MangaEntity (para Room)
 */
fun MangaDetail.toMangaEntity(
    statusUser: String = "Planeado",
    chaptersRead: Int = 0,
    volumesRead: Int = 0,
    userScore: Float = 0f,
    userOpinion: String = "",
    rereadCount: Int = 0,
    startDate: Long? = null,
    endDate: Long? = null,
    plannedPriority: String? = null,
    plannedNote: String? = null
): MangaEntity {
    return MangaEntity(
        malId = malId,
        title = title,
        titleEnglish = titleEnglish,
        titleJapanese = titleJapanese,
        imageUrl = images,
        bannerImage = bannerImage,
        typeManga = typeManga,
        source = source,
        chapters = chapters,
        volumes = volumes,
        status = status,
        published = published,
        score = score,
        scoreBy = scoreBy,
        rank = rank,
        synopsis = synopsis,
        background = background,
        genres = genres.mapNotNull { it?.name }.joinToString(","),
        demographics = demographics.mapNotNull { it?.name }.joinToString(","),
        authors = gson.toJson(authors),
        serializations = gson.toJson(serializations),
        userScore = userScore,
        statusUser = statusUser,
        userOpinion = userOpinion,
        chaptersRead = chaptersRead,
        volumesRead = volumesRead,
        rereadCount = rereadCount,
        startDate = startDate,
        endDate = endDate,
        plannedPriority = plannedPriority,
        plannedNote = plannedNote,
        lastModified = System.currentTimeMillis()
    )
}

/**
 * Convierte MangaEntity (de Room) a MangaEntityDomain (para la capa de dominio)
 */
fun MangaEntity.toMangaEntityDomain(): MangaEntityDomain {
    return MangaEntityDomain(
        malId = malId,
        title = title,
        titleEnglish = titleEnglish,
        titleJapanese = titleJapanese,
        image = imageUrl ?: "",
        bannerImage = bannerImage,
        typeManga = typeManga,
        source = source,
        chapters = chapters,
        volumes = volumes,
        status = status,
        published = published,
        score = score,
        scoreBy = scoreBy,
        rank = rank,
        synopsis = synopsis,
        background = background,
        genres = genres,
        demographics = demographics,
        authors = authors,
        serializations = serializations,
        userScore = userScore,
        userStatus = statusUser,
        userOpinion = userOpinion,
        chaptersRead = chaptersRead,
        volumesRead = volumesRead,
        rereadCount = rereadCount,
        startDate = startDate,
        endDate = endDate,
        plannedPriority = plannedPriority,
        plannedNote = plannedNote,
        lastModified = lastModified
    )
}

/**
 * Convierte MangaEntityDomain a MangaEntity
 */
fun MangaEntityDomain.toMangaEntity(): MangaEntity {
    return MangaEntity(
        malId = malId,
        title = title,
        titleEnglish = titleEnglish,
        titleJapanese = titleJapanese,
        imageUrl = image,
        bannerImage = bannerImage,
        typeManga = typeManga,
        source = source,
        chapters = chapters,
        volumes = volumes,
        status = status,
        published = published,
        score = score,
        scoreBy = scoreBy,
        rank = rank,
        synopsis = synopsis,
        background = background,
        genres = genres,
        demographics = demographics,
        authors = authors,
        serializations = serializations,
        userScore = userScore,
        statusUser = userStatus,
        userOpinion = userOpinion,
        chaptersRead = chaptersRead,
        volumesRead = volumesRead,
        rereadCount = rereadCount,
        startDate = startDate,
        endDate = endDate,
        plannedPriority = plannedPriority,
        plannedNote = plannedNote,
        lastModified = lastModified
    )
}

/**
 * Deserializa JSON de autores a lista de AuthorDto
 */
fun String?.toAuthorList(): List<AuthorDto> {
    if (this.isNullOrEmpty()) return emptyList()
    return try {
        val type = object : TypeToken<List<AuthorDto>>() {}.type
        gson.fromJson(this, type) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }
}

/**
 * Deserializa JSON de serializaciones a lista de SerializationDto
 */
fun String?.toSerializationList(): List<SerializationDto> {
    if (this.isNullOrEmpty()) return emptyList()
    return try {
        val type = object : TypeToken<List<SerializationDto>>() {}.type
        gson.fromJson(this, type) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }
}
