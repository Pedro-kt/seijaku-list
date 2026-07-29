package com.yumedev.seijakulist.ui.screens.onboarding.pages

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.ui.screens.onboarding.components.GlassCard
import com.yumedev.seijakulist.ui.screens.onboarding.components.MeshGradientBackground
import com.yumedev.seijakulist.ui.screens.onboarding.components.DramaticEntrance
import com.yumedev.seijakulist.ui.theme.SeijakuVoice

/**
 * PANTALLA 4: El Mecanismo - Las Huellas - VERSIÓN MEJORADA
 *
 * Visual:
 * - Huellas como sticky notes coloridos pasteles
 * - Cada huella con rotación ligera (efecto pegado)
 * - Sombras realistas
 * - Colores pasteles diferentes
 */
@Composable
fun HuellasPage(modifier: Modifier = Modifier) {
    // Animación de entrada escalonada
    val infiniteTransition = rememberInfiniteTransition(label = "huellas_float")
    val floatOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    val floatOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )

    val floatOffset3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float3"
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Fondo con gradiente suave
        MeshGradientBackground(
            colors = listOf(
                Color(0xFF2D2228),
                Color(0xFF332A2D),
                MaterialTheme.colorScheme.background
            )
        )

        DramaticEntrance(delay = 200) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
            ) {
                // Copy principal en card de vidrio con entrada dramática
                DramaticEntrance(delay = 400) {
                    GlassCard(
                        backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.12f),
                        borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        cornerRadius = 24.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Copy principal (Lora, cream)
                            Text(
                                text = "dejás huellas de lo que sentiste",
                                style = SeijakuVoice.vozGrande,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Subcopy (Onest)
                            Text(
                                text = "no es una reseña,\nno es un rating público",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center,
                                lineHeight = 27.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "es algo tuyo que le dejás a la obra,\npara volver cuando lo extrañés",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Ejemplos de huellas como sticky notes con entrada escalonada
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DramaticEntrance(delay = 700) {
                        StickyNoteHuella(
                            texto = "este episodio me destruyó",
                            color = Color(0xFFFFB7C5), // Rosa pastel
                            rotation = -2f,
                            offset = floatOffset1
                        )
                    }

                    DramaticEntrance(delay = 900) {
                        StickyNoteHuella(
                            texto = "mio es todo",
                            color = Color(0xFFFFF4A0), // Amarillo pastel
                            rotation = 1.5f,
                            offset = floatOffset2
                        )
                    }

                    DramaticEntrance(delay = 1100) {
                        StickyNoteHuella(
                            texto = "no quiero que termine",
                            color = Color(0xFFB4D3FF), // Azul pastel
                            rotation = -1f,
                            offset = floatOffset3
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StickyNoteHuella(
    texto: String,
    color: Color,
    rotation: Float,
    offset: Float
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .offset(y = offset.dp)
            .rotate(rotation)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color.Black.copy(alpha = 0.15f),
                spotColor = Color.Black.copy(alpha = 0.15f)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .border(
                width = 0.5.dp,
                color = Color.Black.copy(alpha = 0.05f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Ícono de corazón pequeño
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = null,
                tint = Color(0xFF5A4A4E).copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )

            // Texto de la huella (Lora italic)
            Text(
                text = texto,
                style = SeijakuVoice.huellaTexto.copy(fontSize = 15.sp),
                color = Color(0xFF3A2A2E),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
