package com.yumedev.seijakulist.ui.screens.add_to_list

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.ui.screens.detail.AnimeDetailAniListViewModel
import com.yumedev.seijakulist.ui.screens.detail.components.shared.RatingBar
import com.yumedev.seijakulist.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToListScreen(
    navController: NavController,
    animeId: Int,
    viewModel: AnimeDetailAniListViewModel = hiltViewModel()
) {
    val animeDetail by viewModel.animeDetail.collectAsState()
    val existingAnime by viewModel.existingAnime.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Load anime detail if not loaded
    LaunchedEffect(animeId) {
        if (animeDetail?.malId != animeId) {
            viewModel.loadAnimeDetail(animeId)
        }
    }

    val isAdded = existingAnime != null
    val anime = animeDetail

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isAdded) "Editar en Mi Lista" else "Añadir a Mi Lista",
                        fontFamily = PoppinsBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        AddToListContent(
            modifier = Modifier.padding(paddingValues),
            anime = anime,
            existingAnime = existingAnime,
            isAdded = isAdded,
            onSave = { status, rating, startDate, endDate, priority, note ->
                scope.launch {
                    viewModel.addAnimeToList(
                        userScore = rating,
                        userStatus = status ?: "Viendo",
                        userOpinion = note,
                        startDate = startDate,
                        endDate = endDate,
                        plannedPriority = priority,
                        plannedNote = note
                    )

                    val statusEmoji = when (status) {
                        "Viendo" -> "▶️"
                        "Completado" -> "✅"
                        "Pendiente" -> "⏳"
                        "Abandonado" -> "❌"
                        "Planeado" -> "📋"
                        else -> "📺"
                    }
                    val message = if (isAdded)
                        "$statusEmoji Anime actualizado en tu lista como '$status'"
                    else
                        "$statusEmoji Anime añadido a tu lista como '$status'"

                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short
                    )

                    navController.navigateUp()
                }
            },
            onDelete = {
                scope.launch {
                    // TODO: Implement delete method
                    snackbarHostState.showSnackbar("Anime eliminado de tu lista")
                    navController.navigateUp()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddToListContent(
    modifier: Modifier = Modifier,
    anime: AnimeDetail?,
    existingAnime: com.yumedev.seijakulist.domain.models.AnimeEntityDomain?,
    isAdded: Boolean,
    onSave: (String?, Float, Long?, Long?, String?, String) -> Unit,
    onDelete: () -> Unit
) {
    // Estado del formulario
    var sheetStatus by remember { mutableStateOf(existingAnime?.userStatus) }
    var sheetRating by remember { mutableStateOf(existingAnime?.userScore ?: 0f) }
    var sheetOpinion by remember { mutableStateOf(existingAnime?.userOpiniun ?: "") }
    var sheetStartDate by remember { mutableStateOf(existingAnime?.startDate) }
    var sheetEndDate by remember { mutableStateOf(existingAnime?.endDate) }
    var sheetPlannedPriority by remember { mutableStateOf(existingAnime?.plannedPriority) }
    var sheetPlannedNote by remember { mutableStateOf(existingAnime?.plannedNote ?: "") }

    var sheetShowStartPicker by remember { mutableStateOf(false) }
    var sheetShowEndPicker by remember { mutableStateOf(false) }
    var showValidationError by remember { mutableStateOf(false) }

    val statusList = listOf(
        "Viendo" to Icons.Default.PlayArrow,
        "Completado" to Icons.Default.CheckCircle,
        "Pendiente" to Icons.Default.WatchLater,
        "Abandonado" to Icons.Default.Close,
        "Planeado" to Icons.Default.EventNote
    )
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    // DatePicker Dialogs
    if (sheetShowStartPicker) {
        DatePickerDialog(
            onDismissRequest = { sheetShowStartPicker = false },
            confirmButton = {
                TextButton(onClick = { sheetShowStartPicker = false }) {
                    Text("OK", fontFamily = PoppinsMedium)
                }
            },
            dismissButton = {
                TextButton(onClick = { sheetShowStartPicker = false }) {
                    Text("Cancelar", fontFamily = PoppinsRegular)
                }
            }
        ) {
            val pickerState = rememberDatePickerState(initialSelectedDateMillis = sheetStartDate)
            DatePicker(state = pickerState)
            sheetStartDate = pickerState.selectedDateMillis
        }
    }

    if (sheetShowEndPicker) {
        DatePickerDialog(
            onDismissRequest = { sheetShowEndPicker = false },
            confirmButton = {
                TextButton(onClick = { sheetShowEndPicker = false }) {
                    Text("OK", fontFamily = PoppinsMedium)
                }
            },
            dismissButton = {
                TextButton(onClick = { sheetShowEndPicker = false }) {
                    Text("Cancelar", fontFamily = PoppinsRegular)
                }
            }
        ) {
            val pickerState = rememberDatePickerState(initialSelectedDateMillis = sheetEndDate)
            DatePicker(state = pickerState)
            sheetEndDate = pickerState.selectedDateMillis
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Box para superponer botones sobre contenido scrolleable
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Contenido scrolleable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .padding(bottom = 96.dp), // Espacio para los botones
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header con contexto del anime
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    anime?.let {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                // Banner image de fondo con fallback a poster
                                val backgroundImage = when {
                                    it.bannerImage.isNullOrBlank() -> it.images
                                    it.bannerImage.contains("no-image", ignoreCase = true) -> it.images
                                    it.bannerImage.contains("default", ignoreCase = true) -> it.images
                                    else -> it.bannerImage
                                }

                                coil.compose.AsyncImage(
                                    model = backgroundImage,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                )

                                // Gradiente para legibilidad
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Black.copy(alpha = 0.7f),
                                                    Color.Black.copy(alpha = 0.85f)
                                                )
                                            )
                                        )
                                )

                                // Contenido
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Título del anime
                                    Text(
                                        text = it.title,
                                        fontFamily = PoppinsBold,
                                        fontSize = 20.sp,
                                        maxLines = 3,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )

                                    // Botón para resetear el formulario
                                    androidx.compose.material3.IconButton(
                                        onClick = {
                                            sheetStatus = null
                                            sheetRating = 0f
                                            sheetOpinion = ""
                                            sheetStartDate = null
                                            sheetEndDate = null
                                            sheetPlannedPriority = null
                                            sheetPlannedNote = ""
                                            showValidationError = false
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Refresh,
                                            contentDescription = "Limpiar formulario",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                        // Estado - Card con grid compacto y con iconos
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                            border = if (showValidationError && sheetStatus == null)
                                androidx.compose.foundation.BorderStroke(
                                    2.dp,
                                    MaterialTheme.colorScheme.error
                                )
                            else null
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "¿Cuál es el estado?",
                                        fontFamily = PoppinsBold,
                                        fontSize = 21.sp,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        letterSpacing = 0.3.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier.height(200.adp()),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(statusList) { (status, icon) ->
                                        val isSelected = sheetStatus == status
                                        val statusColor = getAnimeStatusColor(status)

                                        Card(
                                            onClick = {
                                                sheetStatus = if (isSelected) null else status
                                                showValidationError = false
                                            },
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (isSelected) statusColor.copy(alpha = 0.15f)
                                                else MaterialTheme.colorScheme.surfaceContainerHighest
                                            ),
                                            modifier = Modifier
                                                .height(60.adp())
                                                .fillMaxWidth(),
                                            shape = RoundedCornerShape(14.dp),
                                            border = if (isSelected)
                                                androidx.compose.foundation.BorderStroke(
                                                    2.dp,
                                                    statusColor
                                                )
                                            else null
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(8.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Icon(
                                                    icon,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(22.adp()),
                                                    tint = if (isSelected) statusColor else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    status,
                                                    fontSize = 12.asp(),
                                                    fontFamily = if (isSelected) PoppinsBold else PoppinsRegular,
                                                    color = if (isSelected) statusColor else MaterialTheme.colorScheme.onSurface,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                }

                                // Mensaje de error de validación
                                androidx.compose.animation.AnimatedVisibility(
                                    visible = showValidationError && sheetStatus == null,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Warning,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                        Text(
                                            text = "Por favor, seleccioná un estado",
                                            fontFamily = PoppinsRegular,
                                            fontSize = 12.asp(),
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }

                    }
                    }

                    // Prioridad y Nota (solo si Planeado) - Card
                    AnimatedVisibility(
                        visible = sheetStatus == "Planeado",
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Prioridad del plan",
                                        fontFamily = PoppinsBold,
                                        fontSize = 21.sp,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        letterSpacing = 0.3.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                val priorities = listOf(
                                    "Alta" to Color(0xFFEF5350),
                                    "Media" to Color(0xFFFFCA28),
                                    "Baja" to Color(0xFF66BB6A)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    priorities.forEach { (priority, color) ->
                                        val isSel = sheetPlannedPriority == priority
                                        Card(
                                            onClick = {
                                                sheetPlannedPriority = if (isSel) null else priority
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(50.adp()),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (isSel) color.copy(alpha = 0.2f)
                                                else MaterialTheme.colorScheme.surfaceContainerHighest
                                            ),
                                            border = if (isSel) androidx.compose.foundation.BorderStroke(
                                                2.dp,
                                                color
                                            ) else null
                                        ) {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    priority,
                                                    fontSize = 14.asp(),
                                                    fontFamily = if (isSel) PoppinsBold else PoppinsRegular,
                                                    color = if (isSel) color else MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }

                                OutlinedTextField(
                                    value = sheetPlannedNote,
                                    onValueChange = { sheetPlannedNote = it },
                                    placeholder = {
                                        Text(
                                            "¿Por qué lo tenés planeado?",
                                            fontFamily = PoppinsRegular,
                                            fontSize = 13.asp()
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(
                                            alpha = 0.3f
                                        )
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    maxLines = 3,
                                    textStyle = androidx.compose.ui.text.TextStyle(
                                        fontFamily = PoppinsRegular,
                                        fontSize = 14.asp()
                                    )
                                )
                            }
                        }
                    }

                    // Calificación (solo si no es Planeado) - Card
                    AnimatedVisibility(
                        visible = sheetStatus != null && sheetStatus != "Planeado",
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "Tu calificación",
                                            fontFamily = PoppinsBold,
                                            fontSize = 21.sp,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                            letterSpacing = 0.3.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    if (sheetRating > 0) {
                                        Surface(
                                            shape = RoundedCornerShape(20.dp),
                                            color = Color(0xFFFFD700).copy(alpha = 0.15f)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(
                                                    horizontal = 12.dp,
                                                    vertical = 6.dp
                                                ),
                                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    String.format("%.1f", sheetRating),
                                                    fontSize = 16.asp(),
                                                    fontFamily = PoppinsBold,
                                                    color = Color(0xFFFFD700)
                                                )
                                                Text(
                                                    "/ 10",
                                                    fontSize = 12.asp(),
                                                    fontFamily = PoppinsRegular,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }

                                RatingBar(
                                    rating = sheetRating,
                                    onRatingChange = { sheetRating = it },
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }

                    // Opinión - Card (solo si no es Planeado)
                    AnimatedVisibility(
                        visible = sheetStatus != null && sheetStatus != "Planeado",
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Tu opinión (opcional)",
                                        fontFamily = PoppinsBold,
                                        fontSize = 21.sp,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        letterSpacing = 0.3.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    // Contador de caracteres
                                    val maxChars = 500
                                    val charCount = sheetOpinion.length
                                    val isNearLimit = charCount > maxChars * 0.8

                                    Text(
                                        text = "$charCount/$maxChars",
                                        fontFamily = PoppinsRegular,
                                        fontSize = 11.asp(),
                                        color = when {
                                            charCount >= maxChars -> MaterialTheme.colorScheme.error
                                            isNearLimit -> Color(0xFFFFCA28)
                                            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                alpha = 0.6f
                                            )
                                        }
                                    )
                                }

                                OutlinedTextField(
                                    value = sheetOpinion,
                                    onValueChange = { if (it.length <= 500) sheetOpinion = it },
                                    placeholder = {
                                        Text(
                                            "Comparte qué te pareció este anime...",
                                            fontFamily = PoppinsRegular,
                                            fontSize = 13.asp()
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.adp()),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(
                                            alpha = 0.3f
                                        )
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    maxLines = 4,
                                    textStyle = androidx.compose.ui.text.TextStyle(
                                        fontFamily = PoppinsRegular,
                                        fontSize = 14.asp()
                                    ),
                                    isError = sheetOpinion.length >= 500
                                )
                            }
                        }
                    }

                    // Fechas - Card (solo si no es Planeado)
                    AnimatedVisibility(
                        visible = sheetStatus != null && sheetStatus != "Planeado",
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Fechas (opcional)",
                                        fontFamily = PoppinsBold,
                                        fontSize = 21.sp,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        letterSpacing = 0.3.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                val canEnd = sheetStatus == "Completado"

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Fecha inicio
                                    Card(
                                        onClick = { sheetShowStartPicker = true },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 1.dp
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(14.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.PlayArrow,
                                                contentDescription = null,
                                                modifier = Modifier.size(20.adp()),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                sheetStartDate?.let { dateFormat.format(it) }
                                                    ?: "Inicio",
                                                fontSize = 13.asp(),
                                                fontFamily = PoppinsMedium,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }

                                    // Fecha final
                                    Card(
                                        onClick = { if (canEnd) sheetShowEndPicker = true },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (canEnd) MaterialTheme.colorScheme.surface
                                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = if (canEnd) 1.dp else 0.dp
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(14.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                modifier = Modifier.size(20.adp()),
                                                tint = if (canEnd) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                    alpha = 0.4f
                                                )
                                            )
                                            Text(
                                                sheetEndDate?.let { dateFormat.format(it) }
                                                    ?: "Final",
                                                fontSize = 13.asp(),
                                                fontFamily = PoppinsMedium,
                                                color = if (canEnd) MaterialTheme.colorScheme.onSurface
                                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                    alpha = 0.4f
                                                ),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

            }

                // Botones de acción con gradiente superior (flotando sobre el contenido)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    // Gradiente superior para difuminar transición
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.background
                                    )
                                )
                            )
                    )

                    // Contenedor de botones
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Botón eliminar (solo si está añadido)
                            if (isAdded) {
                                OutlinedButton(
                                    onClick = onDelete,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(56.adp()),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.5.dp,
                                        MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.adp())
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Eliminar",
                                        fontSize = 15.asp(),
                                        fontFamily = PoppinsBold
                                    )
                                }
                            }

                            // Botón guardar
                            Button(
                                onClick = {
                                    val selectedStatus = sheetStatus
                                    if (selectedStatus != null) {
                                        val scoreToPass =
                                            if (selectedStatus == "Planeado") 0f else sheetRating
                                        val priorityToPass =
                                            if (selectedStatus == "Planeado") sheetPlannedPriority else null
                                        val noteToPass =
                                            if (selectedStatus == "Planeado" && sheetPlannedNote.isNotBlank())
                                                sheetPlannedNote
                                            else if (sheetOpinion.isNotBlank())
                                                sheetOpinion
                                            else
                                                ""

                                        onSave(
                                            selectedStatus,
                                            scoreToPass,
                                            sheetStartDate,
                                            sheetEndDate,
                                            priorityToPass,
                                            noteToPass
                                        )
                                    } else {
                                        showValidationError = true
                                    }
                                },
                                modifier = Modifier
                                    .weight(if (isAdded) 1f else 1f)
                                    .height(56.adp()),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 3.dp,
                                    pressedElevation = 6.dp
                                )
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.adp())
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    if (isAdded) "Guardar" else "Agregar",
                                    fontSize = 15.asp(),
                                    fontFamily = PoppinsBold
                                )
                            }
                        }
                    }
                }
        }
    }
}
