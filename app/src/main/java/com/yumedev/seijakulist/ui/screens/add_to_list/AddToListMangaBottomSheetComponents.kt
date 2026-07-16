package com.yumedev.seijakulist.ui.screens.add_to_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yumedev.seijakulist.domain.models.MangaDetail
import com.yumedev.seijakulist.ui.theme.*

// ============================================================================
// MODAL HEADER PARA MANGA - FIJO
// ============================================================================

@Composable
fun MangaModalHeader(
    manga: MangaDetail?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster pequeño
            if (manga != null) {
                AsyncImage(
                    model = manga.images,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Títulos
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                if (manga != null) {
                    Text(
                        text = manga.title,
                        fontFamily = PoppinsBold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!manga.titleJapanese.isNullOrBlank()) {
                        Text(
                            text = manga.titleJapanese,
                            fontFamily = PoppinsRegular,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Botón cerrar
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cerrar",
                    fontFamily = PoppinsMedium,
                    fontSize = 14.sp
                )
            }
        }

        // Divider
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )
    }
}

// ============================================================================
// STATUS SELECTOR PARA MANGA - Radio buttons verticales
// ============================================================================

@Composable
fun MangaStatusSelector(
    selectedStatus: String?,
    onStatusSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val statusList = listOf(
        "Leyendo" to Icons.Default.MenuBook,
        "Completado" to Icons.Default.CheckCircle,
        "Pendiente" to Icons.Default.WatchLater,
        "Abandonado" to Icons.Default.Close,
        "Planeado" to Icons.Default.EventNote
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "¿Dónde está hoy?",
            fontFamily = PoppinsRegular,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            statusList.forEachIndexed { index, (status, icon) ->
                MangaStatusRadioItem(
                    status = status,
                    icon = icon,
                    isSelected = selectedStatus == status,
                    onClick = { onStatusSelected(if (selectedStatus == status) null else status) }
                )

                // Agregar divider entre elementos (excepto después del último)
                if (index < statusList.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MangaStatusRadioItem(
    status: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = status,
                fontFamily = PoppinsRegular,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

// ============================================================================
// PROGRESS SECTION - Capítulos y Volúmenes
// ============================================================================

@Composable
fun ProgressSection(
    currentChapter: Int,
    totalChapters: Int?,
    currentVolume: Int,
    totalVolumes: Int?,
    onChapterChanged: (Int) -> Unit,
    onVolumeChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Progreso",
            fontFamily = PoppinsRegular,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Capítulo
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Capítulo",
                    fontFamily = PoppinsRegular,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    OutlinedTextField(
                        value = currentChapter.toString(),
                        onValueChange = {
                            val newValue = it.toIntOrNull() ?: 0
                            onChapterChanged(newValue.coerceAtLeast(0))
                        },
                        modifier = Modifier.weight(1f),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontFamily = PoppinsMedium,
                            fontSize = 16.sp
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    )
                    Text(
                        text = "/ ${totalChapters ?: "?"}",
                        fontFamily = PoppinsMedium,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Volumen
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Volumen",
                    fontFamily = PoppinsRegular,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    OutlinedTextField(
                        value = currentVolume.toString(),
                        onValueChange = {
                            val newValue = it.toIntOrNull() ?: 0
                            onVolumeChanged(newValue.coerceAtLeast(0))
                        },
                        modifier = Modifier.weight(1f),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontFamily = PoppinsMedium,
                            fontSize = 16.sp
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    )
                    Text(
                        text = "/ ${totalVolumes ?: "?"}",
                        fontFamily = PoppinsMedium,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

// ============================================================================
// REREADING TOGGLE - Switch
// ============================================================================

@Composable
fun RereadingToggle(
    isRereading: Boolean,
    onRereadingChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onRereadingChanged(!isRereading) }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Estoy releyéndolo",
            fontFamily = PoppinsRegular,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Switch(
            checked = isRereading,
            onCheckedChange = onRereadingChanged,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
        )
    }
}

// ============================================================================
// RATING SECTION PARA MANGA - 5 Estrellas (igual que anime)
// ============================================================================

@Composable
fun MangaRatingSection(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Tu calificación",
            fontFamily = PoppinsRegular,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Rating bar con 5 estrellas (igual que anime)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..5) {
                val isFilled = rating >= i * 2
                val isHalf = rating >= (i * 2 - 1) && rating < i * 2

                IconButton(
                    onClick = {
                        val newRating = when {
                            isFilled -> (i * 2 - 1).toFloat()
                            isHalf -> (i * 2).toFloat()
                            else -> (i * 2 - 1).toFloat()
                        }
                        onRatingChanged(newRating)
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = when {
                            isFilled -> Icons.Default.Star
                            isHalf -> Icons.AutoMirrored.Filled.StarHalf
                            else -> Icons.Default.StarOutline
                        },
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = if (isFilled || isHalf) Color(0xFFFFD700)
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

// ============================================================================
// MAIN MODAL CONTENT - Integra todos los componentes
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToListMangaModalContent(
    modifier: Modifier = Modifier,
    manga: MangaDetail?,
    existingManga: Any?, // TODO: Cambiar a MangaEntityDomain cuando esté implementado
    isAdded: Boolean,
    onDismiss: () -> Unit,
    onSave: (String?, Float, Int, Int, Boolean, String) -> Unit,
    onDelete: () -> Unit
) {
    // Estado del formulario
    // TODO: Cuando MangaEntityDomain esté implementado, obtener valores de existingManga
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    var rating by remember { mutableStateOf(0f) }
    var currentChapter by remember { mutableIntStateOf(0) }
    var currentVolume by remember { mutableIntStateOf(0) }
    var isRereading by remember { mutableStateOf(false) }
    var opinion by remember { mutableStateOf("") }
    var plannedPriority by remember { mutableStateOf<String?>(null) }
    var plannedNote by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // HEADER FIJO
            MangaModalHeader(
                manga = manga,
                onDismiss = onDismiss
            )

            // CONTENIDO SCROLLEABLE
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(androidx.compose.foundation.rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 160.dp), // Espacio para el botón + navigation bar
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                // Selector de Estado
                MangaStatusSelector(
                    selectedStatus = selectedStatus,
                    onStatusSelected = { selectedStatus = it }
                )

                // Contenido condicional según estado
                when (selectedStatus) {
                    "Planeado" -> {
                        PlannedSection(
                            priority = plannedPriority,
                            note = plannedNote,
                            onPriorityChanged = { plannedPriority = it },
                            onNoteChanged = { plannedNote = it }
                        )
                    }
                    null -> {
                        // Estado vacío, no mostrar nada
                    }
                    else -> {
                        // Leyendo, Completado, Pendiente, Abandonado

                        // Progreso
                        ProgressSection(
                            currentChapter = currentChapter,
                            totalChapters = manga?.chapters,
                            currentVolume = currentVolume,
                            totalVolumes = manga?.volumes,
                            onChapterChanged = { currentChapter = it },
                            onVolumeChanged = { currentVolume = it }
                        )

                        // Estoy releyéndolo
                        RereadingToggle(
                            isRereading = isRereading,
                            onRereadingChanged = { isRereading = it }
                        )

                        // Calificación
                        MangaRatingSection(
                            rating = rating,
                            onRatingChanged = { rating = it }
                        )

                        // Opinión
                        OpinionSection(
                            opinion = opinion,
                            onOpinionChanged = { opinion = it }
                        )
                    }
                }
            }
        }

        // BOTÓN FIJO EN EL BOTTOM
        ActionButton(
            isAdded = isAdded,
            selectedStatus = selectedStatus,
            onSave = {
                // TODO: Implementar lógica de guardado después
                // Por ahora solo llamar al callback con valores dummy
                val scoreToPass = if (selectedStatus == "Planeado") 0f else rating
                onSave(
                    selectedStatus,
                    scoreToPass,
                    currentChapter,
                    currentVolume,
                    isRereading,
                    if (selectedStatus == "Planeado") plannedNote else opinion
                )
                // Cerrar el modal después de guardar
                onDismiss()
            },
            onDelete = {
                // Ejecutar callback de eliminación
                onDelete()
                // Cerrar el modal después de eliminar
                onDismiss()
            },
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .align(Alignment.BottomCenter)
        )
    }
}
