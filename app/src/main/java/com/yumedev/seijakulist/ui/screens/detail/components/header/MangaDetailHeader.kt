package com.yumedev.seijakulist.ui.screens.detail.components.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yumedev.seijakulist.domain.models.MangaDetail
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium

/**
 * Header del detalle del manga con imagen de portada y información principal
 */
@Composable
fun MangaDetailHeader(mangaDetail: MangaDetail) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        // Imagen de fondo
        AsyncImage(
            model = mangaDetail.images,
            contentDescription = mangaDetail.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradiente oscuro
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        // Información sobre el gradiente
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = mangaDetail.title,
                fontFamily = PoppinsBold,
                fontSize = 24.sp,
                color = Color.White,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Score
                if (mangaDetail.score > 0) {
                    ScoreChip(score = mangaDetail.score)
                }

                // Tipo
                TypeChip(type = mangaDetail.typeManga)

                // Estado
                StatusChip(status = mangaDetail.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Capítulos
                if (mangaDetail.chapters != null && mangaDetail.chapters > 0) {
                    InfoChip(text = "${mangaDetail.chapters} caps")
                }

                // Volúmenes
                if (mangaDetail.volumes != null && mangaDetail.volumes > 0) {
                    InfoChip(text = "${mangaDetail.volumes} vols")
                }
            }
        }
    }
}

@Composable
private fun ScoreChip(score: Float) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "⭐ ${String.format("%.1f", score)}",
            fontFamily = PoppinsMedium,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun TypeChip(type: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = type,
            fontFamily = PoppinsMedium,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun StatusChip(status: String) {
    val statusText = when (status) {
        "FINISHED" -> "Finalizado"
        "RELEASING" -> "En publicación"
        "NOT_YET_RELEASED" -> "Próximamente"
        "CANCELLED" -> "Cancelado"
        "HIATUS" -> "En pausa"
        else -> status
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = statusText,
            fontFamily = PoppinsMedium,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun InfoChip(text: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontFamily = PoppinsMedium,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
