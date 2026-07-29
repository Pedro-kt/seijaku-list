package com.yumedev.seijakulist.ui.screens.onboarding.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sin

/**
 * ANIMACIONES AVANZADAS PARA ONBOARDING IMPRESIONANTE
 *
 * Incluye:
 * - Parallax effect
 * - Animaciones de entrada dramáticas
 * - Partículas orgánicas con física
 * - Blur dinámico
 */

/**
 * Entrada dramática con fade + slide + scale escalonados
 */
@Composable
fun DramaticEntrance(
    delay: Int = 0,
    content: @Composable () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        isVisible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "alpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.85f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val offsetY by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 40.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "offsetY"
    )

    Box(
        modifier = Modifier
            .offset(y = offsetY)
            .scale(scale)
            .alpha(alpha)
    ) {
        content()
    }
}

/**
 * Partículas orgánicas avanzadas con movimiento de física
 */
@Composable
fun OrganicParticles(
    modifier: Modifier = Modifier,
    count: Int = 8,
    color: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "organic_particles")

    Box(modifier = modifier.fillMaxSize()) {
        repeat(count) { index ->
            val offset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 8000 + (index * 500),
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                ),
                label = "particle_$index"
            )

            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.05f,
                targetValue = 0.15f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 3000 + (index * 200),
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "alpha_$index"
            )

            // Posición sinusoidal para movimiento orgánico
            val xOffset = (sin(Math.toRadians(offset.toDouble())) * 100).toFloat()
            val yOffset = (sin(Math.toRadians(offset * 0.7)) * 150).toFloat()

            val size = (40 + index * 15).dp

            Box(
                modifier = Modifier
                    .offset(
                        x = (50 + index * 40 + xOffset).dp,
                        y = (100 + index * 60 + yOffset).dp
                    )
                    .size(size)
                    .alpha(alpha)
                    .blur(8.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                color,
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }
    }
}

/**
 * Parallax layers - diferentes capas se mueven a diferentes velocidades
 */
@Composable
fun ParallaxLayer(
    speedFactor: Float = 1f,
    content: @Composable (offset: Float) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "parallax")

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (15000 / speedFactor).toInt(),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "parallax_offset"
    )

    content(offset)
}

/**
 * Glow pulsante avanzado con múltiples capas
 */
@Composable
fun AdvancedGlow(
    size: Dp,
    color: Color,
    layers: Int = 3
) {
    val infiniteTransition = rememberInfiniteTransition(label = "advanced_glow")

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_scale"
    )

    Box {
        repeat(layers) { index ->
            val layerSize = size * (1f + index * 0.3f)
            val layerAlpha = alpha / (index + 1)

            Box(
                modifier = Modifier
                    .size(layerSize)
                    .scale(scale)
                    .alpha(layerAlpha)
                    .blur((6 + index * 4).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                color,
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }
    }
}
