package com.yumedev.seijakulist.ui.screens.onboarding.pages

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.ui.screens.onboarding.components.GlassCard
import com.yumedev.seijakulist.ui.screens.onboarding.components.MeshGradientBackground
import com.yumedev.seijakulist.ui.screens.onboarding.components.DramaticEntrance
import com.yumedev.seijakulist.ui.theme.SeijakuVoice

/**
 * PANTALLA 2: El Problema/Necesidad - VERSIÓN MEJORADA
 *
 * Visual Split: Contraste entre "ruido" y "calma"
 * - Izquierda: Notificaciones agresivas (rojo, amarillo)
 * - Derecha: Espacio vacío calmo
 *
 * Estética: Contraste visual fuerte para comunicar el problema
 */
@Composable
fun ProblemPage(modifier: Modifier = Modifier) {
    // Animación de pulse para notificaciones
    val infiniteTransition = rememberInfiniteTransition(label = "notification_pulse")
    val notificationScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Fondo con gradiente (más oscuro para contraste)
        MeshGradientBackground(
            colors = listOf(
                Color(0xFF1F1625),
                Color(0xFF2A1F2E),
                MaterialTheme.colorScheme.background
            )
        )

        DramaticEntrance(delay = 200) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                // LADO IZQUIERDO: El Ruido (notificaciones)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Notificación 1 (roja agresiva) con entrada escalonada
                        DramaticEntrance(delay = 300) {
                            NotificationBadge(
                                color = Color(0xFFFF4444),
                                scale = notificationScale,
                                alpha = 0.9f
                            )
                        }

                        // Notificación 2 (naranja)
                        DramaticEntrance(delay = 450) {
                            NotificationBadge(
                                color = Color(0xFFFF9800),
                                scale = notificationScale * 0.9f,
                                alpha = 0.8f
                            )
                        }

                        // Notificación 3 (amarilla)
                        DramaticEntrance(delay = 600) {
                            NotificationBadge(
                                color = Color(0xFFFFC107),
                                scale = notificationScale * 0.95f,
                                alpha = 0.85f
                            )
                        }
                    }
                }

                // LADO DERECHO: La Calma (vacío)
                DramaticEntrance(delay = 750) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Círculo vacío calmo
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                    }
                }
            }
        }

        // Copy sobre el visual split con entrada dramática
        DramaticEntrance(delay = 900) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))

                // Card de vidrio para el copy
                GlassCard(
                    backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.25f),
                    borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    cornerRadius = 24.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Copy principal (Lora, cream)
                        Text(
                            text = "todos los trackers te gritan,\nte llenan de notificaciones,\nte piden que compartas",
                            style = SeijakuVoice.vozGrande.copy(fontSize = 20.sp),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            lineHeight = 29.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Divisor sutil
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(2.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Subcopy (Onest)
                        Text(
                            text = "pero vos solo querés\ntener cerca lo que amaste\nsin ruido",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            lineHeight = 27.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun NotificationBadge(
    color: Color,
    scale: Float,
    alpha: Float
) {
    Box(
        modifier = Modifier
            .size(56.dp * scale)
            .alpha(alpha)
            .clip(RoundedCornerShape(16.dp))
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }
}
