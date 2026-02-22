package com.yumedev.seijakulist.ui.screens.novedades

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.ui.components.ChangeType
import com.yumedev.seijakulist.ui.components.WhatsNewChange
import com.yumedev.seijakulist.ui.components.WhatsNewVersion
import com.yumedev.seijakulist.ui.components.WHATS_NEW_HISTORY
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular

@Composable
fun NovedadesScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        itemsIndexed(WHATS_NEW_HISTORY) { index, version ->
            VersionSection(
                version = version,
                isLatest = index == 0,
                startExpanded = index == 0
            )
            if (index < WHATS_NEW_HISTORY.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                )
            }
        }

        item { Spacer(modifier = Modifier.height(96.dp)) }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sección de versión desplegable
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun VersionSection(
    version: WhatsNewVersion,
    isLatest: Boolean,
    startExpanded: Boolean
) {
    var expanded by remember { mutableStateOf(startExpanded) }

    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "arrow_rotation"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        // ── Cabecera clicable ─────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "v${version.versionName}",
                        fontFamily = PoppinsBold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (isLatest) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(horizontal = 9.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Actual",
                                fontFamily = PoppinsBold,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                // Contador visible solo cuando está colapsado
                AnimatedVisibility(visible = !expanded) {
                    Text(
                        text = "${version.changes.size} cambios",
                        fontFamily = PoppinsRegular,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                    )
                }
            }

            // Flecha con rotación animada
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Colapsar" else "Expandir",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier
                    .size(22.dp)
                    .rotate(arrowRotation)
            )
        }

        // ── Lista de cambios animada ──────────────────────────────────────────
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(animationSpec = tween(300)) +
                    fadeIn(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(250)) +
                    fadeOut(animationSpec = tween(200))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                version.changes.forEach { change ->
                    ChangeItem(change)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Ítem de cambio individual
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ChangeItem(change: WhatsNewChange) {
    val style = changeStyle(change.type)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(style.color.copy(alpha = 0.13f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = style.icon,
                    contentDescription = null,
                    tint = style.color,
                    modifier = Modifier.size(19.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = style.label,
                    fontFamily = PoppinsBold,
                    fontSize = 10.sp,
                    color = style.color,
                    letterSpacing = 0.6.sp
                )
                Text(
                    text = change.title,
                    fontFamily = PoppinsBold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = change.description,
                    fontFamily = PoppinsRegular,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                    lineHeight = 17.sp
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Estilo por tipo
// ─────────────────────────────────────────────────────────────────────────────
private data class ChangeStyle(
    val icon: ImageVector,
    val color: Color,
    val label: String
)

@Composable
private fun changeStyle(type: ChangeType): ChangeStyle = when (type) {
    ChangeType.NEW -> ChangeStyle(
        icon = Icons.Default.AutoAwesome,
        color = MaterialTheme.colorScheme.primary,
        label = "NUEVO"
    )
    ChangeType.IMPROVED -> ChangeStyle(
        icon = Icons.AutoMirrored.Filled.TrendingUp,
        color = MaterialTheme.colorScheme.tertiary,
        label = "MEJORA"
    )
    ChangeType.FIX -> ChangeStyle(
        icon = Icons.Default.BugReport,
        color = MaterialTheme.colorScheme.error,
        label = "CORRECCIÓN"
    )
}