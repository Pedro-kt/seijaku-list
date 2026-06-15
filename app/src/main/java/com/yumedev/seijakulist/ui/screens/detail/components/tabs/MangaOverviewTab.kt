package com.yumedev.seijakulist.ui.screens.detail.components.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.domain.models.MangaDetail
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular

/**
 * Tab de resumen/overview para el detalle del manga
 */
@Composable
fun MangaOverviewTab(mangaDetail: MangaDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Sinopsis
        if (mangaDetail.synopsis.isNotBlank()) {
            SectionTitle("Sinopsis")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mangaDetail.synopsis,
                fontFamily = PoppinsRegular,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Información general
        SectionTitle("Información")
        Spacer(modifier = Modifier.height(12.dp))

        InfoRow("Título en inglés", mangaDetail.titleEnglish)
        InfoRow("Título japonés", mangaDetail.titleJapanese)
        InfoRow("Tipo", mangaDetail.typeManga)
        InfoRow("Estado", formatStatus(mangaDetail.status))

        if (mangaDetail.chapters != null && mangaDetail.chapters > 0) {
            InfoRow("Capítulos", mangaDetail.chapters.toString())
        }

        if (mangaDetail.volumes != null && mangaDetail.volumes > 0) {
            InfoRow("Volúmenes", mangaDetail.volumes.toString())
        }

        InfoRow("Fuente", formatSource(mangaDetail.source))
        InfoRow("Publicado", mangaDetail.published)

        if (mangaDetail.score > 0) {
            InfoRow("Puntuación", "${mangaDetail.score}/10")
        }

        if (mangaDetail.scoreBy > 0) {
            InfoRow("Puntuado por", "${mangaDetail.scoreBy} usuarios")
        }

        if (mangaDetail.rank > 0) {
            InfoRow("Ranking", "#${mangaDetail.rank}")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Autores
        if (mangaDetail.authors.isNotEmpty()) {
            SectionTitle("Autores")
            Spacer(modifier = Modifier.height(12.dp))

            mangaDetail.authors.forEach { author ->
                InfoRow(author.role, author.name)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Géneros
        if (mangaDetail.genres.isNotEmpty()) {
            SectionTitle("Géneros")
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(mangaDetail.genres.filterNotNull()) { genre ->
                    GenreChip(genreName = genre.name ?: "")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Background (si existe)
        if (mangaDetail.background.isNotBlank()) {
            SectionTitle("Contexto")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mangaDetail.background,
                fontFamily = PoppinsRegular,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontFamily = PoppinsBold,
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontFamily = PoppinsMedium,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontFamily = PoppinsRegular,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun GenreChip(genreName: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Text(
            text = genreName,
            fontFamily = PoppinsMedium,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

private fun formatStatus(status: String): String {
    return when (status) {
        "FINISHED" -> "Finalizado"
        "RELEASING" -> "En publicación"
        "NOT_YET_RELEASED" -> "Próximamente"
        "CANCELLED" -> "Cancelado"
        "HIATUS" -> "En pausa"
        else -> status
    }
}

private fun formatSource(source: String): String {
    return when (source) {
        "ORIGINAL" -> "Original"
        "MANGA" -> "Manga"
        "LIGHT_NOVEL" -> "Light Novel"
        "VISUAL_NOVEL" -> "Visual Novel"
        "VIDEO_GAME" -> "Videojuego"
        "OTHER" -> "Otro"
        "NOVEL" -> "Novela"
        "DOUJINSHI" -> "Doujinshi"
        "ANIME" -> "Anime"
        "WEB_NOVEL" -> "Web Novel"
        "LIVE_ACTION" -> "Live Action"
        "GAME" -> "Juego"
        "COMIC" -> "Comic"
        "MULTIMEDIA_PROJECT" -> "Proyecto Multimedia"
        "PICTURE_BOOK" -> "Libro Ilustrado"
        else -> source
    }
}
