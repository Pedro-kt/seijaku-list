package com.yumedev.seijakulist.data.mapper

import com.yumedev.seijakulist.data.remote.graphql.SearchAnimeQuery
import com.yumedev.seijakulist.data.remote.graphql.GetAnimeDetailsQuery
import com.yumedev.seijakulist.data.remote.graphql.GetSeasonalAnimeQuery
import com.yumedev.seijakulist.data.remote.graphql.fragment.MediaFields
import com.yumedev.seijakulist.data.remote.models.CharacterJpgDto
import com.yumedev.seijakulist.data.remote.models.CharacterWebpDto
import com.yumedev.seijakulist.data.remote.models.GenreDto
import com.yumedev.seijakulist.data.remote.models.ImagesCharactersDto
import com.yumedev.seijakulist.domain.models.AnimeCard
import com.yumedev.seijakulist.domain.models.AnimeCharactersDetail
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.domain.models.BroadcastInfo
import com.yumedev.seijakulist.domain.models.TrailerInfo
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

/**
 * Mappers para convertir modelos de Apollo GraphQL (AniList) a Domain Models
 */

// ========== AnimeCard Mappers ==========

/**
 * Convierte SearchAnimeQuery.Medium (contiene MediaFields) a AnimeCard
 */
fun SearchAnimeQuery.Medium.toAnimeCard(): AnimeCard {
    return mediaFields.toAnimeCard()
}

/**
 * Convierte GetSeasonalAnimeQuery.Medium (contiene MediaFields) a AnimeCard
 */
fun GetSeasonalAnimeQuery.Medium.toAnimeCard(): AnimeCard {
    return mediaFields.toAnimeCard()
}

/**
 * Convierte MediaFields fragment a AnimeCard
 * Este es el mapper principal que reutilizan los demás
 */
fun MediaFields.toAnimeCard(): AnimeCard {
    return AnimeCard(
        malId = idMal ?: 0, // Algunos animes pueden no tener MAL ID
        title = title?.romaji ?: title?.english ?: title?.native ?: "Sin título",
        images = coverImage?.large ?: coverImage?.extraLarge ?: "",
        score = (averageScore ?: 0) / 10.0f, // AniList usa escala 0-100, convertir a 0-10
        status = status?.name ?: "UNKNOWN",
        type = format?.name ?: "UNKNOWN",
        genres = genres?.filterNotNull()?.map { GenreDto(malId = null, name = it) } ?: emptyList(),
        year = seasonYear?.toString() ?: "Desconocido",
        episodes = episodes?.toString() ?: "?"
    )
}

// ========== AnimeDetail Mappers ==========

/**
 * Convierte GetAnimeDetailsQuery.Media a AnimeDetail
 */
fun GetAnimeDetailsQuery.Media.toAnimeDetail(): AnimeDetail {
    val fields = mediaFields // Acceder al fragment

    return AnimeDetail(
        malId = fields.idMal ?: 0,
        title = fields.title?.romaji ?: fields.title?.english ?: fields.title?.native ?: "Sin título",
        titleEnglish = fields.title?.english ?: "No encontrado",
        titleJapanese = fields.title?.native ?: "No encontrado",
        images = fields.coverImage?.extraLarge ?: fields.coverImage?.large ?: fields.bannerImage ?: "",
        trailer = fields.trailer?.let {
            TrailerInfo(
                youtubeId = it.id,
                url = when (it.site?.lowercase()) {
                    "youtube" -> "https://www.youtube.com/watch?v=${it.id}"
                    "dailymotion" -> "https://www.dailymotion.com/video/${it.id}"
                    else -> null
                },
                embedUrl = when (it.site?.lowercase()) {
                    "youtube" -> "https://www.youtube.com/embed/${it.id}"
                    "dailymotion" -> "https://www.dailymotion.com/embed/video/${it.id}"
                    else -> null
                },
                thumbnailUrl = when (it.site?.lowercase()) {
                    "youtube" -> "https://img.youtube.com/vi/${it.id}/maxresdefault.jpg"
                    else -> null
                }
            )
        },
        typeAnime = fields.format?.name ?: "UNKNOWN",
        source = fields.source?.name ?: "UNKNOWN",
        episodes = fields.episodes ?: 0,
        status = fields.status?.name ?: "UNKNOWN",
        aired = formatAiredDates(fields.startDate, fields.endDate),
        broadcast = fields.nextAiringEpisode?.let { mapBroadcastInfo(it) },
        duration = fields.duration?.let { "${it} min" } ?: "Desconocido",
        rating = if (fields.isAdult == true) "R+" else "PG-13", // AniList solo tiene isAdult boolean
        score = (fields.averageScore ?: 0) / 10.0f,
        scoreBy = fields.stats?.scoreDistribution?.sumOf { it?.amount ?: 0 } ?: 0,
        rank = fields.rankings?.firstOrNull { it?.allTime == true }?.rank ?: 0,
        synopsis = fields.description?.stripHtml() ?: "Sinopsis no disponible",
        background = "", // AniList no tiene campo de background
        season = fields.season?.name ?: "UNKNOWN",
        year = fields.seasonYear ?: 0,
        producers = emptyList(), // AniList no separa producers de studios
        studios = fields.studios?.edges?.mapNotNull { it?.node }?.map {
            com.yumedev.seijakulist.data.remote.models.StudiosDto(
                idStudio = null,
                nameStudio = it.name
            )
        } ?: emptyList(),
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

// ========== Helper Functions ==========

/**
 * Formatea las fechas de inicio y fin en formato legible
 */
private fun formatAiredDates(
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
 * Mapea nextAiringEpisode a BroadcastInfo
 */
private fun mapBroadcastInfo(airing: MediaFields.NextAiringEpisode): BroadcastInfo {
    try {
        val instant = Instant.ofEpochSecond(airing.airingAt.toLong())
        val zonedDateTime = instant.atZone(ZoneId.of("Asia/Tokyo"))
        val dayOfWeek = zonedDateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        val time = String.format("%02d:%02d", zonedDateTime.hour, zonedDateTime.minute)

        return BroadcastInfo(
            day = dayOfWeek,
            time = time,
            timezone = "JST",
            fullString = "$dayOfWeek at $time JST"
        )
    } catch (e: Exception) {
        return BroadcastInfo(
            day = "Unknown",
            time = "Unknown",
            timezone = "JST",
            fullString = "Broadcast time unknown"
        )
    }
}

/**
 * Remueve tags HTML de la descripción
 * AniList puede incluir HTML en las descripciones
 */
private fun String.stripHtml(): String {
    return this
        .replace("<br>", "\n")
        .replace("<br/>", "\n")
        .replace("<br />", "\n")
        .replace("<i>", "")
        .replace("</i>", "")
        .replace("<b>", "")
        .replace("</b>", "")
        .replace(Regex("<.*?>"), "")
        .trim()
}

// ========== Character Mappers ==========

/**
 * Convierte CharacterEdge de GetAnimeDetailsQuery a AnimeCharactersDetail
 */
fun GetAnimeDetailsQuery.Edge1.toAnimeCharactersDetail(): AnimeCharactersDetail {
    val characterFields = node?.characterFields
    return AnimeCharactersDetail(
        idCharacter = characterFields?.id,
        imageCharacter = characterFields?.image?.large?.let {
            ImagesCharactersDto(
                jpg = CharacterJpgDto(imageUrl = it),
                webp = CharacterWebpDto(imageUrl = it)
            )
        },
        nameCharacter = characterFields?.name?.full ?: characterFields?.name?.native,
        role = role?.name ?: "SUPPORTING"
    )
}
