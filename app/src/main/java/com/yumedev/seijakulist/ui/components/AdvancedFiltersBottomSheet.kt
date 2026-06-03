package com.yumedev.seijakulist.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp
import kotlinx.coroutines.launch

// Data class para manejar el estado de los filtros
data class AnimeFilters(
    val statuses: Set<String> = emptySet(),
    val genres: Set<String> = emptySet(),
    val types: Set<String> = emptySet(),
    val years: Set<String> = emptySet(),
    val scoreRange: ClosedFloatingPointRange<Float> = 0f..10f
) {
    fun isActive(): Boolean {
        return statuses.isNotEmpty() ||
                genres.isNotEmpty() ||
                types.isNotEmpty() ||
                years.isNotEmpty() ||
                scoreRange != 0f..10f
    }

    fun clear(): AnimeFilters {
        return AnimeFilters()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AdvancedFiltersBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    currentFilters: AnimeFilters,
    onApplyFilters: (AnimeFilters) -> Unit,
    availableGenres: List<String> = emptyList(),
    availableYears: List<String> = emptyList()
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val scope = rememberCoroutineScope()

    // Estados locales para los filtros
    var selectedStatuses by remember(currentFilters) { mutableStateOf(currentFilters.statuses) }
    var selectedGenres by remember(currentFilters) { mutableStateOf(currentFilters.genres) }
    var selectedTypes by remember(currentFilters) { mutableStateOf(currentFilters.types) }
    var selectedYears by remember(currentFilters) { mutableStateOf(currentFilters.years) }
    var scoreRange by remember(currentFilters) { mutableStateOf(currentFilters.scoreRange.start..currentFilters.scoreRange.endInclusive) }

    val statusOptions = listOf(
        "Viendo" to Icons.Default.RemoveRedEye,
        "Completado" to Icons.Default.CheckCircle,
        "Pendiente" to Icons.Default.Schedule,
        "Abandonado" to Icons.Default.Stop,
        "Planeado" to Icons.Default.EventAvailable
    )

    val typeOptions = listOf("TV", "Movie", "OVA", "ONA", "Special")

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                RoundedCornerShape(2.dp)
                            )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filtros avanzados",
                        fontFamily = PoppinsBold,
                        fontSize = 20.asp(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Sección: Estados
                FilterSection(
                    title = "Estado",
                    count = selectedStatuses.size
                ) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        statusOptions.forEach { (status, icon) ->
                            CompactFilterChip(
                                label = status,
                                icon = icon,
                                selected = selectedStatuses.contains(status),
                                onClick = {
                                    selectedStatuses = if (selectedStatuses.contains(status)) {
                                        selectedStatuses - status
                                    } else {
                                        selectedStatuses + status
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sección: Tipos
                FilterSection(
                    title = "Tipo",
                    count = selectedTypes.size
                ) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        typeOptions.forEach { type ->
                            CompactFilterChip(
                                label = type,
                                icon = Icons.Default.LocalMovies,
                                selected = selectedTypes.contains(type),
                                onClick = {
                                    selectedTypes = if (selectedTypes.contains(type)) {
                                        selectedTypes - type
                                    } else {
                                        selectedTypes + type
                                    }
                                }
                            )
                        }
                    }
                }

                // Sección: Géneros (solo si hay disponibles)
                if (availableGenres.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    FilterSection(
                        title = "Géneros",
                        count = selectedGenres.size
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            maxItemsInEachRow = 3
                        ) {
                            availableGenres.take(12).forEach { genre ->
                                CompactFilterChip(
                                    label = genre,
                                    selected = selectedGenres.contains(genre),
                                    onClick = {
                                        selectedGenres = if (selectedGenres.contains(genre)) {
                                            selectedGenres - genre
                                        } else {
                                            selectedGenres + genre
                                        }
                                    },
                                    compact = true
                                )
                            }
                        }
                    }
                }

                // Sección: Años (solo si hay disponibles)
                if (availableYears.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    FilterSection(
                        title = "Año",
                        count = selectedYears.size
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            maxItemsInEachRow = 4
                        ) {
                            availableYears.take(12).forEach { year ->
                                CompactFilterChip(
                                    label = year,
                                    selected = selectedYears.contains(year),
                                    onClick = {
                                        selectedYears = if (selectedYears.contains(year)) {
                                            selectedYears - year
                                        } else {
                                            selectedYears + year
                                        }
                                    },
                                    compact = true
                                )
                            }
                        }
                    }
                }

                // Sección: Puntuación personal
                Spacer(modifier = Modifier.height(16.dp))
                FilterSection(
                    title = "Puntuación personal",
                    count = if (scoreRange.start > 0f || scoreRange.endInclusive < 10f) 1 else 0
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${String.format("%.1f", scoreRange.start)} ⭐",
                                fontFamily = PoppinsBold,
                                fontSize = 14.asp(),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${String.format("%.1f", scoreRange.endInclusive)} ⭐",
                                fontFamily = PoppinsBold,
                                fontSize = 14.asp(),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        RangeSlider(
                            value = scoreRange,
                            onValueChange = { scoreRange = it },
                            valueRange = 0f..10f,
                            steps = 19,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            selectedStatuses = emptySet()
                            selectedGenres = emptySet()
                            selectedTypes = emptySet()
                            selectedYears = emptySet()
                            scoreRange = 0f..10f
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Text(
                            text = "Limpiar",
                            fontFamily = PoppinsRegular,
                            fontSize = 14.asp()
                        )
                    }

                    Button(
                        onClick = {
                            onApplyFilters(
                                AnimeFilters(
                                    statuses = selectedStatuses,
                                    genres = selectedGenres,
                                    types = selectedTypes,
                                    years = selectedYears,
                                    scoreRange = scoreRange.start..scoreRange.endInclusive
                                )
                            )
                            scope.launch {
                                sheetState.hide()
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = "Aplicar",
                            fontFamily = PoppinsBold,
                            fontSize = 14.asp()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    count: Int,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontFamily = PoppinsBold,
                fontSize = 16.asp(),
                color = MaterialTheme.colorScheme.onSurface
            )
            if (count > 0) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = count.toString(),
                        fontFamily = PoppinsBold,
                        fontSize = 12.asp(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
private fun CompactFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    compact: Boolean = false
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (selected)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        else
            MaterialTheme.colorScheme.surfaceContainerHigh,
        border = if (selected)
            BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
        else
            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = if (compact) 12.dp else 14.dp,
                vertical = 5.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (icon != null && !compact) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.adp()),
                    tint = if (selected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Text(
                text = label,
                fontFamily = if (selected) PoppinsBold else PoppinsRegular,
                fontSize = if (compact) 12.asp() else 13.asp(),
                color = if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}
