package com.yumedev.seijakulist.ui.screens.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

// ─────────────────────────────────────────────────────────────────────────────
//  ProfileHeaderCompact - Header compacto del perfil
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ProfileHeaderCompact(
    username: String,
    fullName: String,
    memberSince: String,
    currentlyWatching: String?,
    profilePictureUrl: String?,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.adp(), vertical = 12.adp())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar y info
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.adp()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar con gradiente
                Box(
                    modifier = Modifier.size(70.adp())
                ) {
                    if (profilePictureUrl != null) {
                        AsyncImage(
                            model = profilePictureUrl,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.adp())),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.adp()))
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF9C6FDE),
                                            Color(0xFFE88EA7)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = fullName.firstOrNull()?.toString()?.uppercase() ?: "U",
                                fontFamily = PoppinsBold,
                                fontSize = 32.asp(),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Info del usuario
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.adp())
                ) {
                    Text(
                        text = "@${username.uppercase()} · $memberSince",
                        fontFamily = PoppinsMedium,
                        fontSize = 11.asp(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = fullName,
                        fontFamily = PoppinsBold,
                        fontSize = 26.asp(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (currentlyWatching != null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.adp()),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.adp())
                                    .background(Color.Red, CircleShape)
                            )
                            Text(
                                text = "Ahora viendo $currentlyWatching",
                                fontFamily = PoppinsRegular,
                                fontSize = 11.asp(),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Botón Editar
            OutlinedButton(
                onClick = onEditClick,
                modifier = Modifier
                    .height(36.adp())
                    .widthIn(min = 80.adp()),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(18.adp()),
                contentPadding = PaddingValues(horizontal = 16.adp(), vertical = 8.adp())
            ) {
                Text(
                    text = "Editar",
                    fontFamily = PoppinsMedium,
                    fontSize = 13.asp()
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  ProfileTabSelector - Selector de tabs (Anime, Manga, General)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ProfileTabSelector(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<String> = listOf("Anime", "Manga", "General"),
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.adp(), vertical = 8.adp()),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = selectedTab == index
            val color by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                animationSpec = tween(300),
                label = "tab_color"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onTabSelected(index)
                    }
            ) {
                Text(
                    text = title,
                    fontFamily = if (isSelected) PoppinsBold else PoppinsMedium,
                    fontSize = 16.asp(),
                    color = color
                )
                Spacer(modifier = Modifier.height(4.adp()))
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.adp())
                            .background(
                                color = Color(0xFF00A8FF),
                                shape = RoundedCornerShape(2.adp())
                            )
                    )
                } else {
                    Spacer(modifier = Modifier.height(3.adp()))
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  ResumenSection - Sección de resumen con estadísticas principales
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ResumenSection(
    year: Int,
    totalAnimes: Int,
    averageScore: Float,
    totalEpisodes: Int,
    totalHours: Int,
    totalDaysAndHours: Pair<Int, Int>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.adp(), vertical = 12.adp())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Resumen",
                fontFamily = PoppinsBold,
                fontSize = 20.asp(),
                color = MaterialTheme.colorScheme.onSurface,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
            Text(
                text = year.toString(),
                fontFamily = PoppinsBold,
                fontSize = 11.asp(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(16.adp()))

        // Stats principales (2 cards arriba)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Total animes
            androidx.compose.material3.Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.adp(), vertical = 16.adp())
                ) {
                    Text(
                        text = totalAnimes.toString(),
                        fontFamily = PoppinsBold,
                        fontSize = 48.asp(),
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 48.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                    Text(
                        text = "ANIMES",
                        fontFamily = PoppinsBold,
                        fontSize = 11.asp(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Score medio
            androidx.compose.material3.Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.adp(), vertical = 16.adp())
                ) {
                    Text(
                        text = String.format("%.1f", averageScore),
                        fontFamily = PoppinsBold,
                        fontSize = 48.asp(),
                        color = Color(0xFF00A8FF),
                        lineHeight = 48.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                    Text(
                        text = "SCORE MEDIO",
                        fontFamily = PoppinsBold,
                        fontSize = 11.asp(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.adp()))

        // Stats secundarias (3 cards abajo)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Episodios
            androidx.compose.material3.Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.adp(), vertical = 14.adp())
                ) {
                    Text(
                        text = totalEpisodes.toString(),
                        fontFamily = PoppinsBold,
                        fontSize = 28.asp(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "EPISODIOS",
                        fontFamily = PoppinsBold,
                        fontSize = 10.asp(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Tiempo (horas)
            androidx.compose.material3.Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.adp(), vertical = 14.adp())
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = totalHours.toString(),
                            fontFamily = PoppinsBold,
                            fontSize = 28.asp(),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "hs",
                            fontFamily = PoppinsBold,
                            fontSize = 10.asp(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                    Text(
                        text = "TIEMPO",
                        fontFamily = PoppinsBold,
                        fontSize = 10.asp(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Total visto (días y horas)
            androidx.compose.material3.Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.adp(), vertical = 14.adp())
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "${totalDaysAndHours.first}",
                            fontFamily = PoppinsBold,
                            fontSize = 28.asp(),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "d",
                            fontFamily = PoppinsBold,
                            fontSize = 10.asp(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        Text(
                            text = "${totalDaysAndHours.second}",
                            fontFamily = PoppinsBold,
                            fontSize = 28.asp(),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "h",
                            fontFamily = PoppinsBold,
                            fontSize = 10.asp(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                    Text(
                        text = "TOT. VISTO",
                        fontFamily = PoppinsBold,
                        fontSize = 10.asp(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  DistribucionSection - Distribución de animes por estado
// ─────────────────────────────────────────────────────────────────────────────
data class DistributionItem(
    val label: String,
    val count: Int,
    val color: Color
)

@Composable
fun DistribucionSection(
    totalAnimes: Int,
    items: List<DistributionItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.adp(), vertical = 12.adp())
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Distribución",
                fontFamily = PoppinsBold,
                fontSize = 20.asp(),
                color = MaterialTheme.colorScheme.onSurface,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
            Spacer(modifier = Modifier.width(8.adp()))
            Text(
                text = "$totalAnimes animes en tu lista",
                fontFamily = PoppinsRegular,
                fontSize = 12.asp(),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.adp()))

        // Barra de progreso multi-color
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.adp())
                .clip(RoundedCornerShape(4.adp()))
        ) {
            items.forEach { item ->
                if (item.count > 0) {
                    val weight = item.count.toFloat() / totalAnimes.toFloat()
                    Box(
                        modifier = Modifier
                            .weight(weight)
                            .fillMaxHeight()
                            .background(item.color)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.adp()))

        // Lista de items con counts
        Column(
            verticalArrangement = Arrangement.spacedBy(8.adp())
        ) {
            items.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.adp())
                ) {
                    rowItems.forEach { item ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.adp()),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.adp())
                                    .background(item.color, CircleShape)
                            )
                            Text(
                                text = item.label,
                                fontFamily = PoppinsRegular,
                                fontSize = 13.asp(),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = item.count.toString(),
                                fontFamily = PoppinsBold,
                                fontSize = 13.asp(),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    // Si solo hay un item en la fila, añadir spacer para balance
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  GenerosFavoritosSection - Géneros favoritos con barras de progreso
// ─────────────────────────────────────────────────────────────────────────────
data class GenreItem(
    val name: String,
    val count: Int,
    val percentage: Int,
    val color: Color
)

@Composable
fun GenerosFavoritosSection(
    genres: List<GenreItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.adp(), vertical = 12.adp())
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Géneros favoritos",
                fontFamily = PoppinsBold,
                fontSize = 20.asp(),
                color = MaterialTheme.colorScheme.onSurface,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
            Spacer(modifier = Modifier.width(8.adp()))
            Text(
                text = "los que más aparecen en tu lista",
                fontFamily = PoppinsRegular,
                fontSize = 12.asp(),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(20.adp()))

        // Lista de géneros
        Column(
            verticalArrangement = Arrangement.spacedBy(16.adp())
        ) {
            genres.forEach { genre ->
                GenreProgressBar(
                    name = genre.name,
                    count = genre.count,
                    percentage = genre.percentage,
                    color = genre.color
                )
            }
        }
    }
}

@Composable
private fun GenreProgressBar(
    name: String,
    count: Int,
    percentage: Int,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                fontFamily = PoppinsMedium,
                fontSize = 15.asp(),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$count · ${percentage}%",
                fontFamily = PoppinsMedium,
                fontSize = 13.asp(),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(8.adp()))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.adp())
                .clip(RoundedCornerShape(4.adp()))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage / 100f)
                    .fillMaxHeight()
                    .background(color, RoundedCornerShape(4.adp()))
            )
        }
    }
}
