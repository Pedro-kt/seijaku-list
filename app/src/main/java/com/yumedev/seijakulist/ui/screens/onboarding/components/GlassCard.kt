package com.yumedev.seijakulist.ui.screens.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Card con efecto de glassmorphism
 *
 * Características:
 * - Fondo semi-transparente con blur simulado
 * - Borde sutil
 * - Sombra suave
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
    borderColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
    cornerRadius: Dp = 20.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(20.dp)
    ) {
        content()
    }
}

/**
 * Gradiente mesh para fondos (estilo lofi)
 */
@Composable
fun MeshGradientBackground(
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = colors,
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
    )
}

/**
 * Partículas flotantes decorativas (círculos pequeños)
 */
@Composable
fun FloatingParticles(
    modifier: Modifier = Modifier,
    particleColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Partícula superior derecha
        Box(
            modifier = Modifier
                .offset(x = 280.dp, y = 80.dp)
                .size(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(particleColor)
        )

        // Partícula inferior izquierda
        Box(
            modifier = Modifier
                .offset(x = 40.dp, y = 520.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(particleColor)
        )

        // Partícula central
        Box(
            modifier = Modifier
                .offset(x = 300.dp, y = 400.dp)
                .size(80.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(particleColor.copy(alpha = 0.05f))
        )
    }
}
