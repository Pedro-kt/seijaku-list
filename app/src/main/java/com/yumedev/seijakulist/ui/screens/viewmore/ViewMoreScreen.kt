package com.yumedev.seijakulist.ui.screens.viewmore

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yumedev.seijakulist.ui.components.CardAnimesHomeGrid
import com.yumedev.seijakulist.ui.components.LoadingScreen
import com.yumedev.seijakulist.ui.components.NoInternetScreen
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewMoreScreen(
    navController: NavController,
    section: String,
    filter: String? = null,
    viewModel: ViewMoreViewModel = hiltViewModel()
) {
    val animeList by viewModel.animeList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Inicializar con el filtro recibido por navegación (si hay)
    var selectedFilter by remember { mutableStateOf(filter) }

    val (filterValues, filterLabels) = remember(section) {
        when (section) {
            "season_now" -> Pair(
                listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"),
                listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
            )
            "top_anime", "season_upcoming" -> Pair(
                listOf("tv", "movie", "ova", "special", "ona", "music"),
                listOf("TV", "Película", "OVA", "Especial", "ONA", "Música")
            )
            else -> Pair(emptyList<String>(), emptyList<String>())
        }
    }

    LaunchedEffect(section, selectedFilter) {
        viewModel.loadAnimes(section, selectedFilter)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Fila de filtros — siempre visible, incluso durante la carga
        if (filterValues.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterValues.forEachIndexed { index, value ->
                    val isSelected = selectedFilter == value
                    val chipContainerColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceContainer,
                        animationSpec = tween(300),
                        label = "chip_container_$index"
                    )
                    val chipContentColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        animationSpec = tween(300),
                        label = "chip_content_$index"
                    )
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = if (isSelected) null else value },
                        label = {
                            Text(
                                text = filterLabels[index],
                                fontFamily = PoppinsMedium,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = chipContainerColor,
                            labelColor = chipContentColor,
                            selectedContainerColor = chipContainerColor,
                            selectedLabelColor = chipContentColor
                        )
                    )
                }
            }
        }

        // Área de contenido (ocupa el espacio restante)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingScreen()
                    }
                }

                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        NoInternetScreen(
                            onRetryClick = {
                                viewModel.loadAnimes(section, selectedFilter)
                            }
                        )
                    }
                }

                animeList.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No se encontraron animes",
                                fontSize = 18.sp,
                                fontFamily = PoppinsBold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Intenta con otra sección",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(
                            items = animeList,
                            key = { _, anime -> anime.malId }
                        ) { index, anime ->
                            CardAnimesHomeGrid(
                                anime = anime,
                                navController = navController,
                                modifier = Modifier.animateItem()
                            )

                            // Cargar más cuando llegamos cerca del final
                            if (index >= animeList.size - 6 && !isLoadingMore) {
                                LaunchedEffect(Unit) {
                                    viewModel.loadMoreAnimes()
                                }
                            }
                        }

                        // Indicador de carga al final (span = 3 columnas)
                        if (isLoadingMore) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(32.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                        strokeWidth = 3.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}