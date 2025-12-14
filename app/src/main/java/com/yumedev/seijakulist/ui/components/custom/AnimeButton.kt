package com.yumedev.seijakulist.ui.components.custom

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.ui.theme.RobotoBold

/**
 * Botón personalizado con estilo Anime/Manga
 *
 * Características:
 * - Gradiente vibrante
 * - Efecto de brillo animado
 * - Bordes gruesos tipo manga
 * - Animación de escala al presionar
 * - Sombra dinámica
 */
@Composable
fun AnimeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    variant: AnimeButtonVariant = AnimeButtonVariant.Primary
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale = remember { Animatable(1f) }

    // Animación de escala al presionar
    LaunchedEffect(isPressed) {
        if (isPressed) {
            scale.animateTo(
                targetValue = 0.95f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        } else {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
    }

    // Colores según variante
    val (gradientColors, borderColor, textColor) = when (variant) {
        AnimeButtonVariant.Primary -> Triple(
            listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            ),
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.onPrimary
        )
        AnimeButtonVariant.Secondary -> Triple(
            listOf(
                MaterialTheme.colorScheme.secondaryContainer,
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
            ),
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.onSecondaryContainer
        )
        AnimeButtonVariant.Outlined -> Triple(
            listOf(
                Color.Transparent,
                Color.Transparent
            ),
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .scale(scale.value)
            .shadow(
                elevation = if (isPressed) 2.dp else 8.dp,
                shape = RoundedCornerShape(14.dp),
                spotColor = gradientColors[0].copy(alpha = 0.4f)
            )
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = if (enabled) gradientColors else listOf(
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            )
            .border(
                width = 3.dp,
                color = if (enabled) borderColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(14.dp)
            )
            // Efecto de brillo superior (glassmorphism)
            .drawBehind {
                if (enabled) {
                    val shineGradient = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.Transparent
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height * 0.5f)
                    )
                    drawRect(
                        brush = shineGradient,
                        size = size.copy(height = size.height * 0.5f)
                    )
                }
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (enabled) textColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Text(
                text = text,
                fontSize = 18.sp,
                fontFamily = RobotoBold,
                color = if (enabled) textColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                letterSpacing = 0.5.sp
            )
        }

        // Efecto de partículas/estrellas al presionar (opcional)
        if (isPressed && enabled) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}

/**
 * Variantes del botón Anime
 */
enum class AnimeButtonVariant {
    Primary,    // Gradiente del color primario
    Secondary,  // Gradiente del color secundario
    Outlined    // Sin relleno, solo borde
}
