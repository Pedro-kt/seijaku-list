package com.yumedev.seijakulist.data.mapper

import com.yumedev.seijakulist.data.remote.graphql.GetMangaDetailsQuery
import com.yumedev.seijakulist.data.remote.graphql.fragment.MediaFields
import com.yumedev.seijakulist.data.remote.models.CharacterJpgDto
import com.yumedev.seijakulist.data.remote.models.CharacterWebpDto
import com.yumedev.seijakulist.data.remote.models.GenreDto
import com.yumedev.seijakulist.data.remote.models.ImagesCharactersDto
import com.yumedev.seijakulist.domain.models.AnimeCharactersDetail
import com.yumedev.seijakulist.domain.models.AuthorDto
import com.yumedev.seijakulist.domain.models.MangaDetail
import com.yumedev.seijakulist.domain.models.SerializationDto

/**
 * Mappers para convertir modelos de Apollo GraphQL (AniList) a Domain Models para Manga
 */

/**
 * Convierte GetMangaDetailsQuery.Media a MangaDetail
 */
fun GetMangaDetailsQuery.Media.toMangaDetail(): MangaDetail {
    val fields = mediaFields // Acceder al fragment

    // Extraer autores del staff
    val authors = staff?.edges?.filterNotNull()?.mapNotNull { edge ->
        val node = edge.node ?: return@mapNotNull null
        AuthorDto(
            malId = node.id,
            name = node.name?.full ?: node.name?.native ?: "Desconocido",
            url = node.siteUrl ?: "",
            role = edge.role ?: "Unknown"
        )
    } ?: emptyList()

    return MangaDetail(
        malId = fields.idMal ?: 0,
        title = fields.title?.romaji ?: fields.title?.english ?: fields.title?.native ?: "Sin título",
        titleEnglish = fields.title?.english ?: "No encontrado",
        titleJapanese = fields.title?.native ?: "No encontrado",
        images = fields.coverImage?.extraLarge ?: fields.coverImage?.large ?: "",
        bannerImage = fields.bannerImage,
        typeManga = fields.format?.name ?: "UNKNOWN",
        source = fields.source?.name ?: "UNKNOWN",
        chapters = this.chapters, // chapters está en GetMangaDetailsQuery.Media, no en MediaFields
        volumes = this.volumes,   // volumes está en GetMangaDetailsQuery.Media, no en MediaFields
        status = fields.status?.name ?: "UNKNOWN",
        published = formatPublishedDates(fields.startDate, fields.endDate),
        score = (fields.averageScore ?: 0) / 10.0f,
        scoreBy = fields.stats?.scoreDistribution?.sumOf { it?.amount ?: 0 } ?: 0,
        rank = fields.rankings?.firstOrNull { it?.allTime == true }?.rank ?: 0,
        synopsis = fields.description?.stripHtml() ?: "Sinopsis no disponible",
        background = "", // AniList no tiene campo de background
        authors = authors,
        serializations = emptyList(), // AniList no proporciona información de serialización estructurada
        genres = fields.genres?.filterNotNull()?.map { GenreDto(malId = null, name = it) } ?: emptyList(),
        demographics = fields.tags
            ?.filter { (it?.rank ?: 0) > 80 } // Solo tags con alto ranking
            ?.map {
                com.yumedev.seijakulist.data.remote.models.DemographicDto(
                    malId = it?.id ?: 0,
                    type = "tag",
                    name = it?.name ?: "",
                    url = null
                )
            } ?: emptyList()
    )
}

/**
 * Formatea las fechas de publicación en formato legible
 */
private fun formatPublishedDates(
    startDate: MediaFields.StartDate?,
    endDate: MediaFields.EndDate?
): String {
    val start = startDate?.let {
        "${it.year ?: "?"}-${it.month?.toString()?.padStart(2, '0') ?: "?"}-${it.day?.toString()?.padStart(2, '0') ?: "?"}"
    } ?: "?"

    val end = endDate?.let {
        "${it.year ?: "?"}-${it.month?.toString()?.padStart(2, '0') ?: "?"}-${it.day?.toString()?.padStart(2, '0') ?: "?"}"
    } ?: "?"

    return if (end == "?") start else "$start to $end"
}

/**
 * Remueve tags HTML de un string
 */
private fun String.stripHtml(): String {
    return this
        .replace("<br>", "\n")
        .replace("<br/>", "\n")
        .replace("<br />", "\n")
        .replace(Regex("<[^>]*>"), "")
        .replace("&quot;", "\"")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .trim()
}
