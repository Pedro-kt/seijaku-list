package com.yumedev.seijakulist.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

/**
 * Diálogo para pedir review del anime al usuario cuando lo completa
 *
 * @param onDismissRequest Acción al cerrar el diálogo
 * @param onSave Acción al guardar (recibe score y opinion)
 * @param onSkip Acción al omitir (no guardar review)
 * @param animeTitle Título del anime completado
 * @param currentScore Score actual del anime (si existe)
 * @param currentOpinion Opinión actual del anime (si existe)
 */
@Composable
fun AnimeReviewDialog(
    onDismissRequest: () -> Unit,
    onSave: (score: Float, opinion: String) -> Unit,
    onSkip: () -> Unit,
    animeTitle: String,
    currentScore: Float = 0f,
    currentOpinion: String = ""
) {
    var selectedScore by remember { mutableFloatStateOf(currentScore) }
    var opinion by remember { mutableStateOf(currentOpinion) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Text(
                    text = "¡Completado!",
                    fontFamily = PoppinsBold,
                    fontSize = 24.asp(),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                // Nombre del anime
                Text(
                    text = animeTitle,
                    fontFamily = PoppinsBold,
                    fontSize = 16.asp(),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )

                Text(
                    text = "¿Qué te pareció?",
                    fontFamily = PoppinsRegular,
                    fontSize = 14.asp(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Rating con estrellas
                StarRatingSelector(
                    rating = selectedScore,
                    onRatingChanged = { selectedScore = it }
                )

                // Mostrar puntuación numérica
                if (selectedScore > 0f) {
                    Text(
                        text = String.format("%.1f / 10.0", selectedScore),
                        fontFamily = PoppinsBold,
                        fontSize = 18.asp(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Campo de texto para opinión
                OutlinedTextField(
                    value = opinion,
                    onValueChange = { opinion = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.adp()),
                    placeholder = {
                        Text(
                            text = "Comparte tu opinión... (opcional)",
                            fontFamily = PoppinsRegular,
                            fontSize = 13.asp()
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    maxLines = 4,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = PoppinsRegular,
                        fontSize = 14.asp()
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón de omitir
                    OutlinedButton(
                        onClick = {
                            onSkip()
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.adp()),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(
                            text = "Más tarde",
                            fontFamily = PoppinsBold,
                            fontSize = 14.asp()
                        )
                    }

                    // Botón de guardar
                    Button(
                        onClick = {
                            onSave(selectedScore, opinion.trim())
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.adp()),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = "Guardar",
                            fontFamily = PoppinsBold,
                            fontSize = 14.asp()
                        )
                    }
                }
            }
        }
    }
}

/**
 * Componente de selector de puntuación con estrellas (1-10)
 * Permite seleccionar de 0.5 en 0.5
 */
@Composable
private fun StarRatingSelector(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 10 estrellas (cada una representa 1 punto)
        for (i in 1..10) {
            StarIcon(
                filled = rating >= i,
                halfFilled = rating >= (i - 0.5f) && rating < i,
                onClick = {
                    // Si ya está seleccionada esta estrella completa, deseleccionar
                    val newRating = if (rating == i.toFloat()) {
                        (i - 0.5f).coerceAtLeast(0f)
                    } else {
                        i.toFloat()
                    }
                    onRatingChanged(newRating)
                },
                modifier = Modifier.weight(1f, fill = false)
            )
        }
    }
}

/**
 * Icono de estrella individual con animación
 */
@Composable
private fun StarIcon(
    filled: Boolean,
    halfFilled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (filled || halfFilled) 1.1f else 1f,
        animationSpec = tween(200),
        label = "star_scale"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Icon(
        imageVector = if (filled || halfFilled) Icons.Filled.Star else Icons.Outlined.StarBorder,
        contentDescription = "Star",
        tint = when {
            filled -> Color(0xFFFFD700)
            halfFilled -> Color(0xFFFFD700).copy(alpha = 0.6f)
            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        },
        modifier = modifier
            .size(28.adp())
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    )
}
