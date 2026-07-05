package com.yumedev.seijakulist.ui.screens.local_anime_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.ui.theme.SeijakuColors
import com.yumedev.seijakulist.ui.theme.SeijakuListTheme
import com.yumedev.seijakulist.ui.theme.ThemeMode

// Mock data class for previews
data class MockAnimeDetail(
    val title: String,
    val titleJapanese: String,
    val imageUrl: String,
    val score: Double,
    val scoreBy: Int,
    val rank: Int,
    val episodes: Int,
    val year: String,
    val genres: List<String>,
    val synopsis: String,
    val type: String,
    val status: String,
    val studio: String,
    val source: String,
    val rating: String
)

// Datos mock
private val mockAnime = MockAnimeDetail(
    title = "Frieren: Beyond Journey's End",
    titleJapanese = "葬送のフリーレン",
    imageUrl = "",
    score = 9.1,
    scoreBy = 342891,
    rank = 8,
    episodes = 28,
    year = "2023",
    genres = listOf("Adventure", "Drama", "Fantasy", "Slice of Life"),
    synopsis = "Durante su aventura de diez años para derrotar al Rey Demonio, la maga elfa Frieren y sus compañeros—el héroe Himmel, el sacerdote Heiter y el guerrero Eisen—le trajeron la paz al reino. Pero para Frieren, este viaje fue solo un pequeño momento en su larga vida. Ahora que su grupo se ha dispersado, Frieren se embarca en un nuevo viaje para comprender mejor lo que significaba su aventura para ella.",
    type = "TV",
    status = "Finished",
    studio = "Madhouse",
    source = "Manga",
    rating = "PG-13"
)

// ════════════════════════════════════════════════════════════════════════════════
// PROPUESTA ÚNICA: DISEÑO SEIJAKU CON ESTÉTICA JAPONESA MODERNA
// ════════════════════════════════════════════════════════════════════════════════
// Principios:
// - BALANCE: Cada elemento tiene su espacio, nada domina
// - QUIETUD: Espacios en blanco generosos, respiración
// - CALIDEZ: Colores Seijaku, no fríos ni clínicos
// - JAPONÉS MODERNO: Limpio, ordenado, zen pero no minimalista extremo
// - EXPLORACIÓN: Diseñado para descubrir y decidir si agregar
// ════════════════════════════════════════════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailSeijaku(
    anime: MockAnimeDetail,
    onBackClick: () -> Unit,
    onAddToList: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        containerColor = SeijakuColors.Dark.fondoBase,
        floatingActionButton = {
            // FAB discreto, no invasivo
            FloatingActionButton(
                onClick = onAddToList,
                containerColor = SeijakuColors.Dark.cream,
                contentColor = SeijakuColors.Dark.sobreCream,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 2.dp
                )
            ) {
                Icon(Icons.Default.Add, "Agregar a mi lista")
            }
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(64.dp)) // Espacio para top bar

                // ═══════════════════════════════════════════════════════
                // HEADER SECTION - Balance entre cover e info
                // ═══════════════════════════════════════════════════════
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Cover - mediano, centrado (estilo japonés)
                    Surface(
                        modifier = Modifier
                            .width(140.dp)
                            .height(200.dp),
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 4.dp,
                        color = SeijakuColors.Dark.fondoCard
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = SeijakuColors.Dark.textoTenue,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }

                    // Títulos - centrados, jerarquía clara
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = anime.title,
                            fontSize = 20.sp,
                            lineHeight = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = SeijakuColors.Dark.textoPrimario,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Text(
                            text = anime.titleJapanese,
                            fontSize = 14.sp,
                            color = SeijakuColors.Dark.textoSecundario,
                            textAlign = TextAlign.Center
                        )

                        // Badges - info básica horizontal
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            InfoBadge(text = anime.type, color = SeijakuColors.Dark.estadoViendo)
                            InfoBadge(text = anime.year, color = SeijakuColors.Dark.salvia)
                            InfoBadge(text = "${anime.episodes} eps", color = SeijakuColors.Dark.estadoCompletado)
                        }
                    }
                }

                // ═══════════════════════════════════════════════════════
                // STATS - Información clave balanceada
                // ═══════════════════════════════════════════════════════
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCardSeijaku(
                        value = "${anime.score}",
                        label = "Puntuación",
                        icon = Icons.Default.Star,
                        color = SeijakuColors.Dark.cream,
                        modifier = Modifier.weight(1f)
                    )
                    StatCardSeijaku(
                        value = "#${anime.rank}",
                        label = "Ranking",
                        icon = Icons.Default.EmojiEvents,
                        color = SeijakuColors.Dark.estadoPendiente,
                        modifier = Modifier.weight(1f)
                    )
                    StatCardSeijaku(
                        value = formatNumberShort(anime.scoreBy),
                        label = "Usuarios",
                        icon = Icons.Default.People,
                        color = SeijakuColors.Dark.salvia,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ═══════════════════════════════════════════════════════
                // TABS - Organización del contenido
                // ═══════════════════════════════════════════════════════
                SeijakuTabRow(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    tabs = listOf("Resumen", "Detalles", "Más")
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ═══════════════════════════════════════════════════════
                // CONTENIDO POR TAB
                // ═══════════════════════════════════════════════════════
                when (selectedTab) {
                    0 -> OverviewTabSeijaku(anime)
                    1 -> DetailsTabSeijaku(anime)
                    2 -> MoreTabSeijaku(anime)
                }

                Spacer(modifier = Modifier.height(100.dp)) // Espacio para FAB
            }

            // Top bar simple
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .align(Alignment.TopCenter),
                color = SeijakuColors.Dark.fondoBase.copy(alpha = 0.95f),
                shadowElevation = 1.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Volver",
                            tint = SeijakuColors.Dark.textoPrimario
                        )
                    }
                }
            }
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════════
// COMPONENTS
// ════════════════════════════════════════════════════════════════════════════════

@Composable
private fun InfoBadge(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.25f))
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = color,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun StatCardSeijaku(
    value: String,
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = SeijakuColors.Dark.fondoCard,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SeijakuColors.Dark.textoPrimario
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = SeijakuColors.Dark.textoSecundario,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SeijakuTabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tabs.forEachIndexed { index, tab ->
            SeijakuTab(
                text = tab,
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SeijakuTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) SeijakuColors.Dark.cream else Color.Transparent,
        border = if (!selected) BorderStroke(1.dp, SeijakuColors.Dark.borde) else null,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) SeijakuColors.Dark.sobreCream else SeijakuColors.Dark.textoSecundario
            )
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════════
// TAB CONTENTS
// ════════════════════════════════════════════════════════════════════════════════

@Composable
private fun OverviewTabSeijaku(anime: MockAnimeDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Sinopsis
        SeijakuSection(title = "Sinopsis") {
            var expanded by remember { mutableStateOf(false) }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = anime.synopsis,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = SeijakuColors.Dark.textoPrimario.copy(alpha = 0.9f),
                    textAlign = TextAlign.Justify,
                    maxLines = if (expanded) Int.MAX_VALUE else 5,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.animateContentSize()
                )

                if (anime.synopsis.length > 200) {
                    TextButton(
                        onClick = { expanded = !expanded },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = SeijakuColors.Dark.cream
                        )
                    ) {
                        Text(
                            text = if (expanded) "Ver menos" else "Leer más",
                            fontSize = 12.sp
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Géneros
        SeijakuSection(title = "Géneros") {
            FlowRowCustom(
                horizontalSpacing = 8.dp,
                verticalSpacing = 8.dp
            ) {
                anime.genres.forEach { genre ->
                    GenreChipSeijaku(text = genre)
                }
            }
        }
    }
}

@Composable
private fun DetailsTabSeijaku(anime: MockAnimeDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DetailRowSeijaku("Estudio", anime.studio)
        DetailRowSeijaku("Tipo", anime.type)
        DetailRowSeijaku("Estado", anime.status)
        DetailRowSeijaku("Episodios", "${anime.episodes}")
        DetailRowSeijaku("Fuente", anime.source)
        DetailRowSeijaku("Clasificación", anime.rating)
    }
}

@Composable
private fun MoreTabSeijaku(anime: MockAnimeDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Más información próximamente",
            fontSize = 13.sp,
            color = SeijakuColors.Dark.textoSecundario,
            textAlign = TextAlign.Center
        )
    }
}

// ════════════════════════════════════════════════════════════════════════════════
// HELPER COMPONENTS
// ════════════════════════════════════════════════════════════════════════════════

@Composable
private fun SeijakuSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = SeijakuColors.Dark.textoPrimario
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SeijakuColors.Dark.fondoCard,
            tonalElevation = 1.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun GenreChipSeijaku(text: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = SeijakuColors.Dark.salvia.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, SeijakuColors.Dark.salvia.copy(alpha = 0.25f))
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = SeijakuColors.Dark.salvia,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
        )
    }
}

@Composable
private fun DetailRowSeijaku(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = SeijakuColors.Dark.fondoCard,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                color = SeijakuColors.Dark.textoSecundario
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = SeijakuColors.Dark.textoPrimario
            )
        }
    }
}

@Composable
private fun FlowRowCustom(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 0.dp,
    verticalSpacing: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }

        var currentX = 0
        var currentY = 0
        var maxRowHeight = 0
        val positions = mutableListOf<Pair<Int, Int>>()

        placeables.forEach { placeable ->
            if (currentX + placeable.width > constraints.maxWidth && currentX > 0) {
                currentY += maxRowHeight + verticalSpacing.roundToPx()
                currentX = 0
                maxRowHeight = 0
            }

            positions.add(currentX to currentY)
            currentX += placeable.width + horizontalSpacing.roundToPx()
            maxRowHeight = maxOf(maxRowHeight, placeable.height)
        }

        val width = constraints.maxWidth
        val height = currentY + maxRowHeight

        layout(width, height) {
            placeables.forEachIndexed { index, placeable ->
                val (x, y) = positions[index]
                placeable.place(x, y)
            }
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════════
// HELPERS
// ════════════════════════════════════════════════════════════════════════════════

private fun formatNumberShort(number: Int): String {
    return when {
        number >= 1_000_000 -> String.format("%.1fM", number / 1_000_000.0)
        number >= 1_000 -> String.format("%.0fk", number / 1_000.0)
        else -> number.toString()
    }
}

// ════════════════════════════════════════════════════════════════════════════════
// PREVIEW
// ════════════════════════════════════════════════════════════════════════════════

@Preview(name = "Anime Detail Seijaku", showBackground = true, heightDp = 900)
@Composable
private fun PreviewAnimeDetailSeijaku() {
    SeijakuListTheme(themeMode = ThemeMode.DARK) {
        AnimeDetailSeijaku(
            anime = mockAnime,
            onBackClick = {},
            onAddToList = {}
        )
    }
}
