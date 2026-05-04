package com.yumedev.seijakulist.ui.screens.profile

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import com.yumedev.seijakulist.ui.components.CustomDialog
import com.yumedev.seijakulist.ui.components.DialogType
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import kotlinx.coroutines.delay
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

private val top5PositionColors = listOf(
    Color(0xFFFFD700), // #1 Oro
    Color(0xFFC0C0C0), // #2 Plata
    Color(0xFFCD7F32), // #3 Bronce
    Color(0xFF6366F1), // #4 Índigo
    Color(0xFFEC4899)  // #5 Rosa
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTop5Screen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by profileViewModel.uiState.collectAsState()

    val originalIds = remember { uiState.top5Animes.map { it.malId } }
    var orderedSelectedIds by remember { mutableStateOf(originalIds) }
    var activeSlotIndex by remember { mutableStateOf<Int?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = orderedSelectedIds.isNotEmpty()) {
        showExitDialog = true
    }

    LaunchedEffect(uiState.top5UpdateSuccess) {
        if (uiState.top5UpdateSuccess) {
            delay(300)
            profileViewModel.resetTop5UpdateSuccess()
            navController.popBackStack()
        }
    }

    val filteredAnimes = remember(searchQuery, uiState.allSavedAnimes) {
        if (searchQuery.isBlank()) uiState.allSavedAnimes
        else uiState.allSavedAnimes.filter { it.title.contains(searchQuery, ignoreCase = true) }
    }

    // Toca un slot lleno → lo activa. Toca otro slot lleno con uno activo → swap.
    fun handleSlotClick(slotIndex: Int) {
        when {
            activeSlotIndex == slotIndex -> activeSlotIndex = null
            activeSlotIndex != null -> {
                val a = activeSlotIndex!!
                val b = slotIndex
                if (a < orderedSelectedIds.size && b < orderedSelectedIds.size) {
                    val newList = orderedSelectedIds.toMutableList()
                    val temp = newList[a]
                    newList[a] = newList[b]
                    newList[b] = temp
                    orderedSelectedIds = newList
                }
                activeSlotIndex = null
            }
            slotIndex < orderedSelectedIds.size -> activeSlotIndex = slotIndex
        }
    }

    // Toca un anime de la lista:
    // - Sin slot activo + anime no seleccionado → agrega al final (si hay espacio)
    // - Sin slot activo + anime ya seleccionado → lo elimina del top
    // - Con slot activo + anime ya seleccionado → swap con ese slot
    // - Con slot activo + anime no seleccionado → reemplaza el slot activo
    fun handleAnimeClick(anime: AnimeEntity) {
        val currentPos = orderedSelectedIds.indexOf(anime.malId)
        val isSelected = currentPos >= 0
        val activeSlot = activeSlotIndex

        when {
            activeSlot != null -> {
                val newList = orderedSelectedIds.toMutableList()
                if (isSelected) {
                    if (activeSlot < newList.size) {
                        val temp = newList[activeSlot]
                        newList[activeSlot] = newList[currentPos]
                        newList[currentPos] = temp
                    }
                } else {
                    if (activeSlot < newList.size) newList[activeSlot] = anime.malId
                    else newList.add(anime.malId)
                }
                orderedSelectedIds = newList
                activeSlotIndex = null
            }
            isSelected -> {
                orderedSelectedIds = orderedSelectedIds.toMutableList().also { it.removeAt(currentPos) }
            }
            orderedSelectedIds.size < 5 -> {
                orderedSelectedIds = orderedSelectedIds + anime.malId
            }
        }
    }

    if (showExitDialog) {
        CustomDialog(
            onDismissRequest = { showExitDialog = false },
            onConfirm = { navController.popBackStack() },
            onDismiss = { },
            title = "¿Salir sin guardar?",
            message = "Perderás los cambios que hiciste en tu Top 5.",
            confirmButtonText = "Salir",
            dismissButtonText = "Quedarme",
            type = DialogType.WARNING
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Mi Top 5",
                            fontFamily = PoppinsBold,
                            fontSize = 20.asp()
                        )
                        Text(
                            text = "${orderedSelectedIds.size}/5 seleccionados",
                            fontSize = 12.asp(),
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (orderedSelectedIds.isNotEmpty()) showExitDialog = true
                        else navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left_line),
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { profileViewModel.updateTop5Animes(orderedSelectedIds) },
                        enabled = orderedSelectedIds.isNotEmpty() && !uiState.isSavingTop5
                    ) {
                        if (uiState.isSavingTop5) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.adp()),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.adp())
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Guardar", fontFamily = PoppinsBold)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── Tira de 5 slots ──────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (0 until 5).forEach { index ->
                    val animeId = orderedSelectedIds.getOrNull(index)
                    val anime = animeId?.let { id -> uiState.allSavedAnimes.find { it.malId == id } }
                    Top5SlotCard(
                        position = index + 1,
                        anime = anime,
                        isActive = activeSlotIndex == index,
                        onClick = { handleSlotClick(index) },
                        onRemove = if (animeId != null) ({
                            val newList = orderedSelectedIds.toMutableList()
                            newList.removeAt(index)
                            orderedSelectedIds = newList
                            if (activeSlotIndex == index) activeSlotIndex = null
                        }) else null,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // ── Hint contextual ──────────────────────────────────────────
            Text(
                text = if (activeSlotIndex != null)
                    "Toca otro slot o un anime para moverlo al #${activeSlotIndex!! + 1}"
                else
                    "Toca un slot para reordenar • Toca un anime para agregar",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                fontSize = 12.asp(),
                fontFamily = PoppinsRegular,
                textAlign = TextAlign.Center,
                color = if (activeSlotIndex != null)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // ── Buscador ─────────────────────────────────────────────────
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Buscar anime...", fontFamily = PoppinsRegular) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = searchQuery.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Lista de animes ──────────────────────────────────────────
            when {
                uiState.allSavedAnimes.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(56.adp()),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                            Text(
                                "No tienes animes guardados",
                                fontFamily = PoppinsBold,
                                fontSize = 16.asp()
                            )
                            Text(
                                "Agrega animes a tu lista para poder armar tu Top 5",
                                fontFamily = PoppinsRegular,
                                fontSize = 14.asp(),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                }
                filteredAnimes.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Sin resultados para \"$searchQuery\"",
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(filteredAnimes, key = { it.malId }) { anime ->
                            val slotPosition = orderedSelectedIds.indexOf(anime.malId)
                            val isSelected = slotPosition >= 0
                            AnimePickerCard(
                                anime = anime,
                                slotPosition = if (isSelected) slotPosition + 1 else null,
                                isSlotActive = activeSlotIndex != null,
                                canAdd = !isSelected && orderedSelectedIds.size < 5,
                                onClick = { handleAnimeClick(anime) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Slot card (tira de 5 en la parte superior)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun Top5SlotCard(
    position: Int,
    anime: AnimeEntity?,
    isActive: Boolean,
    onClick: () -> Unit,
    onRemove: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val positionColor = top5PositionColors[position - 1]
    val isFilled = anime != null

    Box(
        modifier = modifier
            .aspectRatio(2f / 3f)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .then(
                when {
                    isActive -> Modifier.border(
                        2.dp,
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(10.dp)
                    )
                    isFilled -> Modifier.border(
                        1.dp,
                        positionColor.copy(alpha = 0.5f),
                        RoundedCornerShape(10.dp)
                    )
                    else -> Modifier.border(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(10.dp)
                    )
                }
            )
            .clickable(onClick = onClick, enabled = isFilled || isActive)
    ) {
        if (anime != null) {
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Gradiente inferior para legibilidad del badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.45f))
            )
        }

        // Badge de posición (esquina superior izquierda)
        Surface(
            modifier = Modifier
                .padding(4.dp)
                .size(18.adp())
                .align(Alignment.TopStart),
            shape = RoundedCornerShape(5.dp),
            color = positionColor
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "$position",
                    fontSize = 9.asp(),
                    fontFamily = PoppinsBold,
                    color = if (position <= 2) Color.Black else Color.White
                )
            }
        }

        // Ícono "+" cuando está vacío
        if (!isFilled) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                modifier = Modifier
                    .size(22.adp())
                    .align(Alignment.Center)
            )
        }

        // Botón X para quitar (esquina superior derecha)
        if (onRemove != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(3.dp)
                    .size(16.adp())
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.65f))
                    .clickable(onClick = onRemove),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Quitar",
                    tint = Color.White,
                    modifier = Modifier.size(10.dp)
                )
            }
        }

        // Overlay de slot activo
        if (isActive) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.18f))
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Card de anime en la lista de selección
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AnimePickerCard(
    anime: AnimeEntity,
    slotPosition: Int?,
    isSlotActive: Boolean,
    canAdd: Boolean,
    onClick: () -> Unit
) {
    val isSelected = slotPosition != null
    val positionColor = slotPosition?.let { top5PositionColors.getOrNull(it - 1) }

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                isSlotActive && canAdd -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)
                else -> MaterialTheme.colorScheme.surfaceContainerHigh
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                modifier = Modifier
                    .width(50.dp)
                    .height(70.adp())
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = anime.title,
                    fontSize = 15.asp(),
                    fontFamily = PoppinsBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurface
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = anime.userScore.toString(),
                        fontSize = 12.asp(),
                        fontFamily = PoppinsRegular,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    if (!anime.statusUser.isNullOrBlank()) {
                        Text(
                            text = "· ${anime.statusUser}",
                            fontSize = 12.asp(),
                            fontFamily = PoppinsRegular,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                        )
                    }
                }
            }

            // Indicador de estado (badge de posición / botón agregar / desactivado)
            when {
                isSelected && positionColor != null -> {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = positionColor,
                        modifier = Modifier.size(32.adp())
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "#$slotPosition",
                                fontSize = 11.asp(),
                                fontFamily = PoppinsBold,
                                color = if (slotPosition!! <= 2) Color.Black else Color.White
                            )
                        }
                    }
                }
                canAdd -> {
                    Surface(
                        shape = CircleShape,
                        color = if (isSlotActive)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Transparent,
                        border = if (isSlotActive) null
                                 else BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar",
                            tint = if (isSlotActive) Color.White
                                   else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier
                                .padding(5.dp)
                                .size(18.adp())
                        )
                    }
                }
                else -> {
                    // Top5 lleno y no está seleccionado: ícono desactivado
                    Surface(
                        shape = CircleShape,
                        color = Color.Transparent,
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            modifier = Modifier
                                .padding(5.dp)
                                .size(18.adp())
                        )
                    }
                }
            }
        }
    }
}