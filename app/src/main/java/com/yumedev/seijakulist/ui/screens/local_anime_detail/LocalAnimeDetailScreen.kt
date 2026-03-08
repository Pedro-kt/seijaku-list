package com.yumedev.seijakulist.ui.screens.local_anime_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.res.painterResource
import com.yumedev.seijakulist.R
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.yumedev.seijakulist.ui.components.TitleWithPadding
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailScreenLocal(
    navController: NavController,
    viewModel: LocalAnimeDetailViewModel = hiltViewModel(),
    animeId: Int
) {

    val anime by viewModel.anime.collectAsState()
    val isSharing by viewModel.isSharing.collectAsState()
    val context = LocalContext.current
    val focusManager: FocusManager = LocalFocusManager.current

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // Estado para editar prioridad y nota del plan
    var showEditPlannedDialog by remember { mutableStateOf(false) }
    var editPlannedPriority by remember { mutableStateOf<String?>(null) }
    var editPlannedNote by remember { mutableStateOf("") }

    val startDatePickerState = rememberDatePickerState()
    val endDatePickerState = rememberDatePickerState()

    LaunchedEffect(anime?.startDate) {
        startDatePickerState.selectedDateMillis = anime?.startDate
    }
    LaunchedEffect(anime?.endDate) {
        endDatePickerState.selectedDateMillis = anime?.endDate
    }

    when (val currentAnime = anime) {
        null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        else -> {
            if (showStartDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showStartDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.updateDates(
                                startDate = startDatePickerState.selectedDateMillis,
                                endDate = currentAnime.endDate
                            )
                            showStartDatePicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showStartDatePicker = false }) { Text("Cancelar") }
                    }
                ) { DatePicker(state = startDatePickerState) }
            }

            if (showEndDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showEndDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.updateDates(
                                startDate = currentAnime.startDate,
                                endDate = endDatePickerState.selectedDateMillis
                            )
                            showEndDatePicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showEndDatePicker = false }) { Text("Cancelar") }
                    }
                ) { DatePicker(state = endDatePickerState) }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { focusManager.clearFocus() }
                        )
                    }
            ) {
                // Top App Bar
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Transparent,
                    tonalElevation = 3.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_left_line),
                                contentDescription = "Volver",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Mi Anime",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 20.sp,
                            fontFamily = PoppinsBold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        IconButton(
                            onClick = { viewModel.shareAnime(context) },
                            enabled = !isSharing,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            if (isSharing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Compartir",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header mejorado con imagen
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                        ) {
                            // Fondo borroso
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(data = currentAnime.image)
                                        .apply(block = fun ImageRequest.Builder.() {
                                            size(Size.ORIGINAL)
                                        }).build()
                                ),
                                contentDescription = "Fondo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .blur(radius = 30.dp)
                                    .scale(1.3f),
                                contentScale = ContentScale.Crop
                            )

                            // Overlay gradient mejorado para TopAppBar
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.background,
                                                MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                                                MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
                                                MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                                                MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                                MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                                                MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                                                MaterialTheme.colorScheme.background
                                            ),
                                            startY = 0f,
                                            endY = Float.POSITIVE_INFINITY
                                        )
                                    )
                            )

                            // Contenido
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp, vertical = 20.dp)
                                    .fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Imagen del anime con sombra
                                Card(
                                    modifier = Modifier
                                        .width(140.dp)
                                        .height(210.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(currentAnime.image),
                                        contentDescription = currentAnime.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                // Información básica
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    // Título
                                    Text(
                                        text = currentAnime.title,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 20.sp,
                                        fontFamily = PoppinsBold,
                                        maxLines = 4,
                                        overflow = TextOverflow.Ellipsis,
                                        lineHeight = 24.sp
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Puntuación usuario
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Tu puntuación",
                                            tint = Color(0xFFFFD700),
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Text(
                                            text = if (currentAnime.userScore % 1.0 == 0.0) {
                                                "${currentAnime.userScore.toInt()}/10"
                                            } else {
                                                "${currentAnime.userScore}/10"
                                            },
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 18.sp,
                                            fontFamily = PoppinsBold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Estado
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                        )
                                    ) {
                                        Text(
                                            text = currentAnime.userStatus,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            fontSize = 13.sp,
                                            fontFamily = PoppinsBold,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Botones de acción
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            FilledTonalButton(
                                onClick = { /* TODO: Editar puntuación */ },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Puntuar")
                            }

                            OutlinedButton(
                                onClick = { /* TODO: Editar información */ },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Editar")
                            }
                        }
                    }

                    // Estadísticas rápidas - Diseño compacto
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Episodios vistos
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Tv,
                                            contentDescription = "Episodios",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "Episodios vistos",
                                            fontSize = 14.sp,
                                            fontFamily = PoppinsRegular,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = "${currentAnime.episodesWatched}/${currentAnime.totalEpisodes ?: "?"}",
                                        fontSize = 16.sp,
                                        fontFamily = PoppinsBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                androidx.compose.material3.HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                )

                                // Veces visto
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Autorenew,
                                            contentDescription = "Veces visto",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "Veces visto",
                                            fontSize = 14.sp,
                                            fontFamily = PoppinsRegular,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = "${currentAnime.rewatchCount}",
                                        fontSize = 16.sp,
                                        fontFamily = PoppinsBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Sección de prioridad del plan (solo si está Planeado)
                    if (currentAnime.userStatus == "Planeado") {
                        item {
                            // Dialog para editar prioridad y nota
                            if (showEditPlannedDialog) {
                                AlertDialog(
                                    onDismissRequest = { showEditPlannedDialog = false },
                                    title = {
                                        Text(
                                            text = "Editar prioridad del plan",
                                            fontFamily = PoppinsBold
                                        )
                                    },
                                    text = {
                                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                            Text(
                                                text = "Prioridad",
                                                fontFamily = PoppinsBold,
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )

                                            val priorities = listOf("Alta", "Media", "Baja")
                                            val priorityColors = mapOf(
                                                "Alta" to Color(0xFFEF5350),
                                                "Media" to Color(0xFFFFCA28),
                                                "Baja" to Color(0xFF66BB6A)
                                            )

                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                priorities.forEach { priority ->
                                                    val isSelected = editPlannedPriority == priority
                                                    Surface(
                                                        onClick = {
                                                            editPlannedPriority = if (isSelected) null else priority
                                                        },
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .height(40.dp),
                                                        shape = RoundedCornerShape(10.dp),
                                                        color = if (isSelected)
                                                            priorityColors[priority] ?: MaterialTheme.colorScheme.primaryContainer
                                                        else
                                                            MaterialTheme.colorScheme.surfaceContainerHighest,
                                                        shadowElevation = if (isSelected) 4.dp else 1.dp
                                                    ) {
                                                        Box(
                                                            modifier = Modifier.fillMaxSize(),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Text(
                                                                text = priority,
                                                                fontSize = 13.sp,
                                                                fontFamily = PoppinsRegular,
                                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                                                            )
                                                        }
                                                    }
                                                }
                                            }

                                            Text(
                                                text = "Nota del plan",
                                                fontFamily = PoppinsBold,
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )

                                            OutlinedTextField(
                                                value = editPlannedNote,
                                                onValueChange = { editPlannedNote = it },
                                                placeholder = { Text("¿Por qué lo tenés planeado?") },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(100.dp),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                                    cursorColor = MaterialTheme.colorScheme.primary
                                                ),
                                                shape = RoundedCornerShape(10.dp),
                                                maxLines = 4
                                            )
                                        }
                                    },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                viewModel.updatePlannedPriorityAndNote(
                                                    priority = editPlannedPriority,
                                                    note = editPlannedNote.ifBlank { null }
                                                )
                                                showEditPlannedDialog = false
                                            }
                                        ) {
                                            Text("Guardar")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(onClick = { showEditPlannedDialog = false }) {
                                            Text("Cancelar")
                                        }
                                    }
                                )
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // Header
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Flag,
                                                contentDescription = "Prioridad",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                text = "Prioridad del plan",
                                                fontSize = 16.sp,
                                                fontFamily = PoppinsBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                editPlannedPriority = currentAnime.plannedPriority
                                                editPlannedNote = currentAnime.plannedNote ?: ""
                                                showEditPlannedDialog = true
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Editar prioridad",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    androidx.compose.material3.HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                    )

                                    // Prioridad actual
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Prioridad",
                                            fontSize = 14.sp,
                                            fontFamily = PoppinsRegular,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        val priorityColor = when (currentAnime.plannedPriority) {
                                            "Alta" -> Color(0xFFEF5350)
                                            "Media" -> Color(0xFFFFCA28)
                                            "Baja" -> Color(0xFF66BB6A)
                                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (currentAnime.plannedPriority != null)
                                                priorityColor.copy(alpha = 0.15f)
                                            else
                                                MaterialTheme.colorScheme.surfaceContainerHighest
                                        ) {
                                            Text(
                                                text = currentAnime.plannedPriority ?: "Sin definir",
                                                fontSize = 14.sp,
                                                fontFamily = PoppinsBold,
                                                color = if (currentAnime.plannedPriority != null) priorityColor
                                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    // Nota del plan
                                    if (!currentAnime.plannedNote.isNullOrBlank()) {
                                        androidx.compose.material3.HorizontalDivider(
                                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                        )
                                        Text(
                                            text = "Nota",
                                            fontSize = 14.sp,
                                            fontFamily = PoppinsRegular,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = currentAnime.plannedNote,
                                            fontSize = 14.sp,
                                            fontFamily = PoppinsRegular,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    // Géneros - Diseño moderno
                    if (!currentAnime.genres.isNullOrEmpty()) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Géneros",
                                        fontSize = 16.sp,
                                        fontFamily = PoppinsBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    val genresList = currentAnime.genres.split(",").map { it.trim() }
                                    androidx.compose.foundation.layout.FlowRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        genresList.forEach { genre ->
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                                border = androidx.compose.foundation.BorderStroke(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                                )
                                            ) {
                                                Text(
                                                    text = genre,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                    fontFamily = PoppinsRegular,
                                                    fontSize = 13.sp,
                                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    // Sinopsis - Diseño mejorado
                    if (!currentAnime.synopsis.isNullOrEmpty()) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Sinopsis",
                                        fontSize = 16.sp,
                                        fontFamily = PoppinsBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = currentAnime.synopsis,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 14.sp,
                                        fontFamily = PoppinsRegular,
                                        textAlign = TextAlign.Justify,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    // Otros títulos - Diseño compacto
                    if (!currentAnime.titleEnglish.isNullOrEmpty() || !currentAnime.titleJapanese.isNullOrEmpty()) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Otros títulos",
                                        fontSize = 16.sp,
                                        fontFamily = PoppinsBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    if (!currentAnime.titleEnglish.isNullOrEmpty()) {
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(
                                                text = "Inglés",
                                                fontSize = 12.sp,
                                                fontFamily = PoppinsRegular,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = currentAnime.titleEnglish,
                                                fontSize = 14.sp,
                                                fontFamily = PoppinsBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        if (!currentAnime.titleJapanese.isNullOrEmpty()) {
                                            androidx.compose.material3.HorizontalDivider(
                                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                            )
                                        }
                                    }

                                    if (!currentAnime.titleJapanese.isNullOrEmpty()) {
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(
                                                text = "Japonés",
                                                fontSize = 12.sp,
                                                fontFamily = PoppinsRegular,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = currentAnime.titleJapanese,
                                                fontSize = 14.sp,
                                                fontFamily = PoppinsBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    // Estudio - Diseño moderno
                    if (!currentAnime.studios.isNullOrEmpty()) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Estudio",
                                        fontSize = 16.sp,
                                        fontFamily = PoppinsBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    val studiosList = currentAnime.studios.split(",").map { it.trim() }
                                    androidx.compose.foundation.layout.FlowRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        studiosList.forEach { studio ->
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                                border = androidx.compose.foundation.BorderStroke(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                                )
                                            ) {
                                                Text(
                                                    text = studio,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                    fontFamily = PoppinsRegular,
                                                    fontSize = 13.sp,
                                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    // Información completa - Diseño compacto
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Información",
                                    fontSize = 16.sp,
                                    fontFamily = PoppinsBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                                currentAnime.score?.let {
                                    CompactInfoRow(label = "Puntuación MAL", value = "$it")
                                }
                                currentAnime.scoreBy?.let {
                                    CompactInfoRow(label = "Puntuado por", value = "$it personas")
                                }
                                currentAnime.typeAnime?.let {
                                    CompactInfoRow(label = "Tipo", value = it)
                                }
                                currentAnime.duration?.let {
                                    CompactInfoRow(label = "Duración", value = it)
                                }
                                currentAnime.season?.let {
                                    CompactInfoRow(label = "Temporada", value = it)
                                }
                                currentAnime.year?.let {
                                    CompactInfoRow(label = "Año", value = it)
                                }
                                currentAnime.status?.let {
                                    CompactInfoRow(label = "Estado", value = it)
                                }
                                currentAnime.aired?.let {
                                    CompactInfoRow(label = "Transmitido", value = it)
                                }
                                currentAnime.rank?.let {
                                    CompactInfoRow(label = "Ranking", value = "#$it")
                                }
                                currentAnime.rating?.let {
                                    CompactInfoRow(label = "Rating", value = it)
                                }
                                currentAnime.source?.let {
                                    CompactInfoRow(label = "Origen", value = it)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Fechas de seguimiento - Siempre visible y editable
                    item {
                        val sdf = remember {
                            java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                                .apply { timeZone = java.util.TimeZone.getTimeZone("UTC") }
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Seguimiento",
                                    fontSize = 16.sp,
                                    fontFamily = PoppinsBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                // Fecha de inicio
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarToday,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Text(
                                                text = "Inicio",
                                                fontSize = 12.sp,
                                                fontFamily = PoppinsRegular,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = currentAnime.startDate?.let { sdf.format(it) } ?: "Sin fecha",
                                                fontSize = 14.sp,
                                                fontFamily = PoppinsBold,
                                                color = if (currentAnime.startDate != null)
                                                    MaterialTheme.colorScheme.onSurface
                                                else
                                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                            )
                                        }
                                    }
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        if (currentAnime.startDate != null) {
                                            IconButton(
                                                onClick = { viewModel.updateDates(null, currentAnime.endDate) },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Clear,
                                                    contentDescription = "Borrar fecha de inicio",
                                                    tint = MaterialTheme.colorScheme.error,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        IconButton(
                                            onClick = { showStartDatePicker = true },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Editar fecha de inicio",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }

                                androidx.compose.material3.HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                )

                                // Fecha de finalización
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarToday,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Text(
                                                text = "Finalización",
                                                fontSize = 12.sp,
                                                fontFamily = PoppinsRegular,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = currentAnime.endDate?.let { sdf.format(it) } ?: "Sin fecha",
                                                fontSize = 14.sp,
                                                fontFamily = PoppinsBold,
                                                color = if (currentAnime.endDate != null)
                                                    MaterialTheme.colorScheme.onSurface
                                                else
                                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                            )
                                        }
                                    }
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        if (currentAnime.endDate != null) {
                                            IconButton(
                                                onClick = { viewModel.updateDates(currentAnime.startDate, null) },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Clear,
                                                    contentDescription = "Borrar fecha de finalización",
                                                    tint = MaterialTheme.colorScheme.error,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        IconButton(
                                            onClick = { showEndDatePicker = true },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Editar fecha de finalización",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Mi Reseña - Diseño mejorado
                    item {
                        if (!currentAnime.userOpiniun.isNullOrEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Mi Reseña",
                                            fontSize = 16.sp,
                                            fontFamily = PoppinsBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        IconButton(
                                            onClick = { /* TODO: Editar reseña */ },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Editar reseña",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = currentAnime.userOpiniun,
                                        textAlign = TextAlign.Justify,
                                        fontSize = 14.sp,
                                        fontFamily = PoppinsRegular,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 20.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontFamily = PoppinsRegular,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.5f)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontFamily = PoppinsBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.End,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}


