package com.yumedev.seijakulist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.SeijakuColors
import kotlinx.coroutines.launch

// Data class para manejar el estado de los filtros
data class AnimeFilters(
    val statuses: Set<String> = emptySet(),
    val genres: Set<String> = emptySet(),
    val types: Set<String> = emptySet(),
    val yearFrom: String = "",
    val yearTo: String = "",
    val minRating: Int = 0,
    val sortBy: String = "",
    val onlyWithPersonalNote: Boolean = false
) {
    fun isActive(): Boolean {
        return statuses.isNotEmpty() ||
                genres.isNotEmpty() ||
                types.isNotEmpty() ||
                yearFrom.isNotEmpty() ||
                yearTo.isNotEmpty() ||
                minRating > 0 ||
                sortBy.isNotEmpty() ||
                onlyWithPersonalNote
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
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()

    // Estados locales para los filtros
    var selectedStatuses by remember(currentFilters) { mutableStateOf(currentFilters.statuses) }
    var selectedGenres by remember(currentFilters) { mutableStateOf(currentFilters.genres) }
    var selectedTypes by remember(currentFilters) { mutableStateOf(currentFilters.types) }
    var yearFrom by remember(currentFilters) { mutableStateOf(currentFilters.yearFrom.ifEmpty { "1980" }) }
    var yearTo by remember(currentFilters) { mutableStateOf(currentFilters.yearTo.ifEmpty { "2026" }) }
    var minRating by remember(currentFilters) { mutableIntStateOf(currentFilters.minRating) }
    var selectedSort by remember(currentFilters) { mutableStateOf(currentFilters.sortBy.ifEmpty { "Recientes" }) }
    var onlyWithNote by remember(currentFilters) { mutableStateOf(currentFilters.onlyWithPersonalNote) }

    val statusOptions = listOf("Viendo", "Completado", "Pendiente", "Abandonado", "Planeado")
    val typeOptions = listOf("TV", "Película", "OVA")
    val sortOptions = listOf("Recientes", "Alfabético", "Mejor calificados", "Año")

    // Estados de expansión para cada sección
    var statusExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }
    var genresExpanded by remember { mutableStateOf(false) }
    var sortExpanded by remember { mutableStateOf(false) }

    // Colores del tema
    val creamColor = SeijakuColors.Dark.cream
    val darkBgColor = Color(0xFF2A2826)

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color(0xFF1C1B19),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .background(
                                Color(0xFF4A4A4A),
                                RoundedCornerShape(2.dp)
                            )
                    )
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
                // Header - Filtros | Limpiar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filtros",
                        fontSize = 24.sp,
                        fontFamily = PoppinsMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    TextButton(
                        onClick = {
                            selectedStatuses = emptySet()
                            selectedGenres = emptySet()
                            selectedTypes = emptySet()
                            yearFrom = "1980"
                            yearTo = "2026"
                            minRating = 0
                            selectedSort = "Recientes"
                            onlyWithNote = false
                        }
                    ) {
                        Text(
                            text = "Limpiar",
                            fontSize = 14.sp,
                            fontFamily = PoppinsRegular,
                            color = Color(0xFFAAAAAA)
                        )
                    }
                }

                // Divider debajo del header
                HorizontalDivider(
                    modifier = Modifier.padding(bottom = 20.dp),
                    thickness = 1.dp,
                    color = Color(0xFF3A3A3A)
                )

                // Sección: Estado (expandible)
                ExpandableChipSection(
                    title = "Estado",
                    items = statusOptions,
                    selectedItems = selectedStatuses,
                    isExpanded = statusExpanded,
                    onExpandToggle = { statusExpanded = !statusExpanded },
                    onItemClick = { status ->
                        selectedStatuses = if (selectedStatuses.contains(status)) {
                            selectedStatuses - status
                        } else {
                            selectedStatuses + status
                        }
                    },
                    creamColor = creamColor,
                    darkBgColor = darkBgColor
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Sección: Tipo (expandible)
                ExpandableChipSection(
                    title = "Tipo",
                    items = typeOptions,
                    selectedItems = selectedTypes,
                    isExpanded = typeExpanded,
                    onExpandToggle = { typeExpanded = !typeExpanded },
                    onItemClick = { type ->
                        selectedTypes = if (selectedTypes.contains(type)) {
                            selectedTypes - type
                        } else {
                            selectedTypes + type
                        }
                    },
                    creamColor = creamColor,
                    darkBgColor = darkBgColor
                )

                // Sección: Géneros (expandible)
                if (availableGenres.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    ExpandableChipSection(
                        title = "Géneros",
                        items = availableGenres,
                        selectedItems = selectedGenres,
                        isExpanded = genresExpanded,
                        onExpandToggle = { genresExpanded = !genresExpanded },
                        onItemClick = { genre ->
                            selectedGenres = if (selectedGenres.contains(genre)) {
                                selectedGenres - genre
                            } else {
                                selectedGenres + genre
                            }
                        },
                        creamColor = creamColor,
                        darkBgColor = darkBgColor
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sección: Año de estreno
                FilterSectionTitle("Año de estreno")
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Campo Desde
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Desde",
                            fontSize = 12.sp,
                            fontFamily = PoppinsRegular,
                            color = Color(0xFF888888)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(darkBgColor)
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = yearFrom,
                                fontSize = 16.sp,
                                fontFamily = PoppinsRegular,
                                color = Color.White
                            )
                        }
                    }
                    // Campo Hasta
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hasta",
                            fontSize = 12.sp,
                            fontFamily = PoppinsRegular,
                            color = Color(0xFF888888)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(darkBgColor)
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = yearTo,
                                fontSize = 16.sp,
                                fontFamily = PoppinsRegular,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sección: Calificación mínima
                FilterSectionTitle("Calificación mínima")
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = if (index < minRating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {
                                    minRating = if (minRating == index + 1) 0 else index + 1
                                },
                            tint = if (index < minRating) Color(0xFFFFB800) else Color(0xFF4A4A4A)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sección: Ordenar por (expandible, single selection)
                ExpandableChipSection(
                    title = "Ordenar por",
                    items = sortOptions,
                    selectedItems = setOf(selectedSort),
                    isExpanded = sortExpanded,
                    onExpandToggle = { sortExpanded = !sortExpanded },
                    onItemClick = { sort -> selectedSort = sort },
                    creamColor = creamColor,
                    darkBgColor = darkBgColor,
                    singleSelection = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Sección: Solo con nota personal
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Solo con nota personal",
                        fontSize = 16.sp,
                        fontFamily = PoppinsRegular,
                        color = Color(0xFFCCCCCC)
                    )
                    Switch(
                        checked = onlyWithNote,
                        onCheckedChange = { onlyWithNote = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = creamColor,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFF4A4A4A)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón Ver resultados
                Surface(
                    onClick = {
                        onApplyFilters(
                            AnimeFilters(
                                statuses = selectedStatuses,
                                genres = selectedGenres,
                                types = selectedTypes,
                                yearFrom = yearFrom,
                                yearTo = yearTo,
                                minRating = minRating,
                                sortBy = selectedSort,
                                onlyWithPersonalNote = onlyWithNote
                            )
                        )
                        scope.launch {
                            sheetState.hide()
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    color = creamColor
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ver resultados",
                            fontSize = 16.sp,
                            fontFamily = PoppinsMedium,
                            color = Color(0xFF2A2826),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontFamily = PoppinsRegular,
        color = Color(0xFF888888)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpandableChipSection(
    title: String,
    items: List<String>,
    selectedItems: Set<String>,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    onItemClick: (String) -> Unit,
    creamColor: Color,
    darkBgColor: Color,
    singleSelection: Boolean = false
) {
    // Limitar a 3 líneas aproximadamente (9 items por defecto)
    val maxItemsCollapsed = 9
    val displayItems = if (isExpanded) items else items.take(maxItemsCollapsed)
    val hasMore = items.size > maxItemsCollapsed

    Column {
        FilterSectionTitle(title)
        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            displayItems.forEach { item ->
                SimpleFilterChip(
                    label = item,
                    selected = selectedItems.contains(item),
                    onClick = { onItemClick(item) },
                    creamColor = creamColor,
                    darkBgColor = darkBgColor
                )
            }
        }

        // Botón Ver más / Ver menos
        if (hasMore) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = onExpandToggle
                ) {
                    Text(
                        text = if (isExpanded) "Ver menos" else "Ver más",
                        fontSize = 13.sp,
                        fontFamily = PoppinsRegular,
                        color = creamColor
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = creamColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SimpleFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    creamColor: Color,
    darkBgColor: Color
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) creamColor else darkBgColor
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontFamily = PoppinsRegular,
                color = if (selected) Color(0xFF2A2826) else Color(0xFFCCCCCC)
            )
        }
    }
}
