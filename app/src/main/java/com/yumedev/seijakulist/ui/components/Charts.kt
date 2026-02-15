package com.yumedev.seijakulist.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import kotlin.math.min

data class PieChartData(
    val label: String,
    val value: Int,
    val color: Color
)

@Composable
fun DonutChart(
    data: List<PieChartData>,
    modifier: Modifier = Modifier,
    centerText: String? = null,
    centerSubtext: String? = null
) {
    val animatedProgress = remember { Animatable(0f) }
    val total = data.sumOf { it.value }.toFloat()

    LaunchedEffect(data) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Efecto de glow/brillo de fondo
            Canvas(
                modifier = Modifier
                    .size(220.dp)
            ) {
                if (total > 0f) {
                    // Glow effect - múltiples círculos con opacidad decreciente
                    for (i in 1..3) {
                        val glowRadius = (min(size.width, size.height) / 2) + (i * 8.dp.toPx())
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    data.firstOrNull()?.color?.copy(alpha = 0.15f / i) ?: Color.Transparent,
                                    Color.Transparent
                                ),
                                radius = glowRadius
                            ),
                            radius = glowRadius
                        )
                    }
                }
            }

            // Gráfico principal
            Canvas(
                modifier = Modifier.size(200.dp)
            ) {
                if (total == 0f) return@Canvas

                val strokeWidth = 48.dp.toPx()
                val radius = (min(size.width, size.height) / 2) - strokeWidth / 2

                var startAngle = -90f

                // Dibujar fondo del gráfico (anillo gris claro)
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color.LightGray.copy(alpha = 0.08f),
                            Color.LightGray.copy(alpha = 0.12f),
                            Color.LightGray.copy(alpha = 0.08f)
                        )
                    ),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(
                        size.width / 2 - radius - strokeWidth / 2,
                        size.height / 2 - radius - strokeWidth / 2
                    ),
                    size = Size((radius + strokeWidth / 2) * 2, (radius + strokeWidth / 2) * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Dibujar cada segmento con gradiente mejorado
                data.forEach { item ->
                    val sweepAngle = (item.value / total) * 360f * animatedProgress.value

                    // Gradiente más vibrante con tres puntos de color
                    val darkColor = Color(
                        red = (item.color.red * 0.7f).coerceIn(0f, 1f),
                        green = (item.color.green * 0.7f).coerceIn(0f, 1f),
                        blue = (item.color.blue * 0.7f).coerceIn(0f, 1f),
                        alpha = 0.9f
                    )
                    val lightColor = Color(
                        red = (item.color.red * 1.2f).coerceIn(0f, 1f),
                        green = (item.color.green * 1.2f).coerceIn(0f, 1f),
                        blue = (item.color.blue * 1.2f).coerceIn(0f, 1f),
                        alpha = 0.85f
                    )

                    val brush = Brush.sweepGradient(
                        0f to lightColor,
                        0.3f to item.color,
                        0.6f to darkColor,
                        0.8f to item.color,
                        1f to lightColor
                    )

                    drawArc(
                        brush = brush,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(
                            size.width / 2 - radius - strokeWidth / 2,
                            size.height / 2 - radius - strokeWidth / 2
                        ),
                        size = Size((radius + strokeWidth / 2) * 2, (radius + strokeWidth / 2) * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    startAngle += sweepAngle
                }
            }

            // Círculo central con gradiente para dar profundidad
            Surface(
                modifier = Modifier
                    .size(120.dp)
                    .shadow(4.dp, CircleShape),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Texto en el centro
                    if (centerText != null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = centerText,
                                fontSize = 38.sp,
                                fontFamily = PoppinsBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (centerSubtext != null) {
                                Text(
                                    text = centerSubtext,
                                    fontSize = 12.sp,
                                    fontFamily = PoppinsRegular,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Leyenda mejorada con barras de progreso
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            data.forEachIndexed { index, item ->
                val percentage = if (total > 0) (item.value / total) * 100 else 0f
                ChartLegendItem(
                    color = item.color,
                    label = item.label,
                    value = item.value,
                    percentage = percentage,
                    animatedProgress = animatedProgress.value,
                    delay = index * 100L
                )
            }
        }
    }
}

@Composable
private fun ChartLegendItem(
    color: Color,
    label: String,
    value: Int,
    percentage: Float,
    animatedProgress: Float,
    delay: Long = 0L
) {
    val progressAnimation = remember { Animatable(0f) }

    LaunchedEffect(animatedProgress) {
        kotlinx.coroutines.delay(delay)
        progressAnimation.animateTo(
            targetValue = percentage / 100f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Fila superior: icono, nombre y valores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Cuadrado de color con gradiente
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .shadow(2.dp, RoundedCornerShape(4.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    color.copy(alpha = 0.8f),
                                    color
                                )
                            ),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontFamily = PoppinsRegular,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }
            // Mostrar porcentaje y valor
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = String.format("%.1f%%", percentage),
                    fontSize = 16.sp,
                    fontFamily = PoppinsBold,
                    color = color
                )
                Text(
                    text = "$value animes",
                    fontSize = 12.sp,
                    fontFamily = PoppinsRegular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Barra de progreso animada
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressAnimation.value)
                    .height(8.dp)
                    .shadow(4.dp, RoundedCornerShape(4.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                color.copy(alpha = 0.7f),
                                color
                            )
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}
