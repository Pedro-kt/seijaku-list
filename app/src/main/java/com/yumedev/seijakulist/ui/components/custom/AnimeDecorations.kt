package com.yumedev.seijakulist.ui.components.custom

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Fondo decorativo con gradiente estilo anime
 */
@Composable
fun AnimeGradientBackground(
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.1f),
                        backgroundColor,
                        backgroundColor
                    )
                )
            )
    ) {
        // Círculo decorativo superior derecho
        Box(
            modifier = Modifier
                .offset(x = 200.dp, y = (-50).dp)
                .size(300.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Círculo decorativo inferior izquierdo
        Box(
            modifier = Modifier
                .offset(x = (-100).dp, y = 500.dp)
                .size(250.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
    }
}

/**
 * Flor de cerezo (Sakura) decorativa simplificada
 */
@Composable
fun SakuraFlower(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    color: Color = Color(0xFFFFB7C5)
) {
    Box(
        modifier = modifier
            .size(size)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = 0.6f),
                        color.copy(alpha = 0.3f),
                        Color.Transparent
                    )
                ),
                shape = CircleShape
            )
    ) {
        // Centro dorado
        Box(
            modifier = Modifier
                .size(size * 0.3f)
                .offset(x = size * 0.35f, y = size * 0.35f)
                .background(
                    color = Color(0xFFFFD700).copy(alpha = 0.7f),
                    shape = CircleShape
                )
        )
    }
}

/**
 * Estrella brillante estilo anime simplificada
 */
@Composable
fun AnimeStar(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = Color.White
) {
    val infiniteTransition = rememberInfiniteTransition(label = "star_twinkle")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .size(size)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = alpha),
                        color.copy(alpha = alpha * 0.5f),
                        Color.Transparent
                    )
                ),
                shape = CircleShape
            )
    )
}

