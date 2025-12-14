package com.yumedev.seijakulist.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.ui.theme.RobotoBold
import com.yumedev.seijakulist.ui.theme.RobotoRegular
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

    LaunchedEffect(data) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(200.dp)) {
                val total = data.sumOf { it.value }.toFloat()
                if (total == 0f) return@Canvas

                val strokeWidth = 40.dp.toPx()
                val radius = (min(size.width, size.height) / 2) - strokeWidth / 2

                var startAngle = -90f

                data.forEach { item ->
                    val sweepAngle = (item.value / total) * 360f * animatedProgress.value

                    drawArc(
                        color = item.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(
                            size.width / 2 - radius - strokeWidth / 2,
                            size.height / 2 - radius - strokeWidth / 2
                        ),
                        size = Size((radius + strokeWidth / 2) * 2, (radius + strokeWidth / 2) * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                    )

                    startAngle += sweepAngle
                }
            }

            // Texto en el centro
            if (centerText != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = centerText,
                        fontSize = 32.sp,
                        fontFamily = RobotoBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (centerSubtext != null) {
                        Text(
                            text = centerSubtext,
                            fontSize = 14.sp,
                            fontFamily = RobotoRegular,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Leyenda
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            data.forEach { item ->
                ChartLegendItem(
                    color = item.color,
                    label = item.label,
                    value = item.value.toString()
                )
            }
        }
    }
}

@Composable
private fun ChartLegendItem(
    color: Color,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(color = color, shape = CircleShape)
            )
            Text(
                text = label,
                fontSize = 15.sp,
                fontFamily = RobotoRegular,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = value,
            fontSize = 15.sp,
            fontFamily = RobotoBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
