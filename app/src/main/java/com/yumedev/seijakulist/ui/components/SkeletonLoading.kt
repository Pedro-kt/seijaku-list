package com.yumedev.seijakulist.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

// ══════════════════════════════════════════════════════════════════════════════
// SHIMMER BRUSH - Utilidad compartida para el efecto shimmer
// ══════════════════════════════════════════════════════════════════════════════
@Composable
fun shimmerBrush(): Brush {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f),
        MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.3f),
        MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )
}

// ══════════════════════════════════════════════════════════════════════════════
// TAB ROW SKELETON - Para el selector de tabs (Anime/Manga)
// ══════════════════════════════════════════════════════════════════════════════
@Composable
fun TabRowSkeleton() {
    val brush = shimmerBrush()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(2) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(brush)
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// FILTER CHIPS SKELETON - Para los chips de filtros
// ══════════════════════════════════════════════════════════════════════════════
@Composable
fun FilterChipsSkeleton() {
    val brush = shimmerBrush()

    // Anchos fijos para evitar que se muevan durante el shimmer
    val chipWidths = listOf(75.dp, 90.dp, 65.dp, 85.dp, 70.dp)

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(chipWidths.size) { index ->
            Box(
                modifier = Modifier
                    .width(chipWidths[index])
                    .height(32.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(brush)
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// QUICK STATS SKELETON - Para la sección de estadísticas rápidas
// ══════════════════════════════════════════════════════════════════════════════
@Composable
fun QuickStatsSkeleton() {
    val brush = shimmerBrush()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(brush)
                )
            }

            // Insight box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(brush)
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// HERO CAROUSEL SKELETON - Para el carrusel de destacados
// ══════════════════════════════════════════════════════════════════════════════
@Composable
fun HeroCarouselSkeleton() {
    val brush = shimmerBrush()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(brush)
            )
        }

        // Card principal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(220.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(brush)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Dots indicadores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(5) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(brush)
                        )
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// SECTION HEADER SKELETON - Para los headers de sección
// ══════════════════════════════════════════════════════════════════════════════
@Composable
fun SectionHeaderSkeleton() {
    val brush = shimmerBrush()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(160.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(brush)
        )
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(brush)
        )
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// ANIME CARDS SKELETON - Para la lista horizontal de cards de anime
// ══════════════════════════════════════════════════════════════════════════════
@Composable
fun AnimeCardsSkeleton() {
    val brush = shimmerBrush()

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(6) {
            Column(
                modifier = Modifier.width(130.adp()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen
                Box(
                    modifier = Modifier
                        .width(130.adp())
                        .height(200.adp())
                        .clip(RoundedCornerShape(12.dp))
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Título línea 1
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Título línea 2
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// SECTION WITH FILTER SKELETON - Sección completa con header, filtros y cards
// ══════════════════════════════════════════════════════════════════════════════
@Composable
fun SectionWithFilterSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        SectionHeaderSkeleton()
        Spacer(modifier = Modifier.height(4.dp))
        FilterChipsSkeleton()
        AnimeCardsSkeleton()
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// FULL HOME SCREEN SKELETON - Skeleton completo para la pantalla principal
// ══════════════════════════════════════════════════════════════════════════════
@Composable
fun FullHomeScreenSkeleton() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRowSkeleton()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            QuickStatsSkeleton()
            Spacer(modifier = Modifier.height(8.dp))
            HeroCarouselSkeleton()
            Spacer(modifier = Modifier.height(16.dp))

            // Sección 1
            SectionWithFilterSkeleton()
            Spacer(modifier = Modifier.height(16.dp))

            // Sección 2
            SectionWithFilterSkeleton()
        }
    }
}
