package com.yumedev.seijakulist.ui.screens.add_to_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.ui.theme.*
import java.text.SimpleDateFormat

// ============================================================================
// MODAL HEADER - FIJO
// ============================================================================

@Composable
fun ModalHeader(
    anime: AnimeDetail?,
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
            if (anime != null) {
                AsyncImage(
                    model = anime.images,
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
                if (anime != null) {
                    Text(
                        text = anime.title,
                        fontFamily = PoppinsBold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!anime.titleJapanese.isNullOrBlank()) {
                        Text(
                            text = anime.titleJapanese,
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
// STATUS SELECTOR - Radio buttons verticales
// ============================================================================

@Composable
fun StatusSelector(
    selectedStatus: String?,
    onStatusSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val statusList = listOf(
        "Viendo" to Icons.Default.PlayArrow,
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
                StatusRadioItem(
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
private fun StatusRadioItem(
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
// PLANNED SECTION
// ============================================================================

@Composable
fun PlannedSection(
    priority: String?,
    note: String,
    onPriorityChanged: (String?) -> Unit,
    onNoteChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Prioridad
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Prioridad",
                fontFamily = PoppinsRegular,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val priorities = listOf("Alta", "Media", "Baja")
                priorities.forEach { p ->
                    val isSelected = priority == p
                    OutlinedButton(
                        onClick = { onPriorityChanged(if (isSelected) null else p) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else Color.Transparent,
                            contentColor = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = p,
                            fontFamily = if (isSelected) PoppinsBold else PoppinsRegular,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Nota de plan
        OutlinedTextField(
            value = note,
            onValueChange = onNoteChanged,
            placeholder = {
                Text(
                    "¿Por qué esperás verla?",
                    fontFamily = PoppinsRegular,
                    fontSize = 14.sp
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = PoppinsRegular,
                fontSize = 14.sp
            ),
            maxLines = 3
        )
    }
}

// ============================================================================
// RATING SECTION - Estrellas
// ============================================================================

@Composable
fun RatingSection(
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

        // Rating bar con estrellas
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
                            isHalf -> Icons.Default.StarHalf
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
// OPINION SECTION
// ============================================================================

@Composable
fun OpinionSection(
    opinion: String,
    onOpinionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val maxChars = 500

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Una nota para vos (opcional)",
                fontFamily = PoppinsRegular,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${opinion.length}/$maxChars",
                fontFamily = PoppinsRegular,
                fontSize = 12.sp,
                color = when {
                    opinion.length >= maxChars -> MaterialTheme.colorScheme.error
                    opinion.length > maxChars * 0.8 -> Color(0xFFFFCA28)
                    else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                }
            )
        }

        OutlinedTextField(
            value = opinion,
            onValueChange = { if (it.length <= maxChars) onOpinionChanged(it) },
            placeholder = {
                Text(
                    "Qué te dejó esta obra...",
                    fontFamily = PoppinsRegular,
                    fontSize = 14.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = PoppinsRegular,
                fontSize = 14.sp
            ),
            maxLines = 4,
            isError = opinion.length >= maxChars
        )
    }
}

// ============================================================================
// DATES SECTION
// ============================================================================

@Composable
fun DatesSection(
    startDate: Long?,
    endDate: Long?,
    canSelectEndDate: Boolean,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    dateFormat: SimpleDateFormat,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Fechas (opcional)",
            fontFamily = PoppinsRegular,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Fecha inicio
            DateCard(
                label = "Empezó",
                placeholder = "Elegir fecha",
                date = startDate,
                enabled = true,
                onClick = onStartDateClick,
                dateFormat = dateFormat,
                modifier = Modifier.weight(1f)
            )

            // Fecha final
            DateCard(
                label = "Terminó",
                placeholder = "Elegir fecha",
                date = endDate,
                enabled = canSelectEndDate,
                onClick = onEndDateClick,
                dateFormat = dateFormat,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DateCard(
    label: String,
    placeholder: String,
    date: Long?,
    enabled: Boolean,
    onClick: () -> Unit,
    dateFormat: SimpleDateFormat,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (enabled) MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (enabled) MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                fontFamily = PoppinsRegular,
                fontSize = 12.sp,
                color = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Text(
                text = date?.let { dateFormat.format(it) } ?: placeholder,
                fontFamily = PoppinsMedium,
                fontSize = 14.sp,
                color = if (enabled) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

// ============================================================================
// ACTION BUTTON - FIJO EN EL BOTTOM
// ============================================================================

@Composable
fun ActionButton(
    isAdded: Boolean,
    selectedStatus: String?,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Botones con background sólido
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background) // Background solo en esta parte
                .padding(bottom = 16.dp) // Padding adicional para separación
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón eliminar (solo si está añadido)
                if (isAdded) {
                    OutlinedButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
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
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Eliminar",
                            fontSize = 15.sp,
                            fontFamily = PoppinsBold
                        )
                    }
                }

                // Botón guardar/añadir
                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .weight(if (isAdded) 1f else 1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    enabled = selectedStatus != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD4C5A9), // Color beige/crema del diseño
                        contentColor = Color(0xFF3E2723), // Texto oscuro para contraste
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        if (isAdded) "Guardar" else "Añadir a mi lista",
                        fontSize = 15.sp,
                        fontFamily = PoppinsBold
                    )
                }
            }
        }
    }
}
