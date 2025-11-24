package com.yumedev.seijakulist.ui.screens.auth_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArtTrack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.screens.auth_screen.AuthResult
import com.yumedev.seijakulist.ui.screens.auth_screen.AuthViewModel
import com.yumedev.seijakulist.ui.theme.RobotoBold
import com.yumedev.seijakulist.ui.theme.RobotoRegular
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// Paleta de colores japoneses tradicionales
object JapaneseColors {
    val Sakura = Color(0xFFFFB7C5)          // Rosa cerezo
    val SakuraDark = Color(0xFFFF8FA3)      // Rosa cerezo oscuro
    val Matcha = Color(0xFF88B04B)          // Verde té matcha
    val Sumi = Color(0xFF2C2C2C)            // Negro tinta sumi
    val Washi = Color(0xFFFFF8E7)           // Blanco papel washi
    val Koi = Color(0xFFE63946)             // Rojo koi
    val Fuji = Color(0xFF9FA8DA)            // Azul monte Fuji
    val FujiLight = Color(0xFFC5CAE9)       // Azul claro
    val Bamboo = Color(0xFF6B8E23)          // Verde bambú
    val Sunset = Color(0xFFFF9A76)          // Naranja atardecer
    val Indigo = Color(0xFF5C6BC0)          // Índigo tradicional
    val Gold = Color(0xFFFFD700)            // Dorado templo
    val Cream = Color(0xFFFFF5E1)           // Crema suave
}

@Composable
fun AuthScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var isVisible by remember { mutableStateOf(false) }
    var floatingOffset by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        delay(200)
        isVisible = true
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Fondo principal - degradado de atardecer japonés
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            JapaneseColors.Fuji,
                            JapaneseColors.FujiLight,
                            JapaneseColors.Sakura.copy(alpha = 0.3f),
                            JapaneseColors.Cream,
                            JapaneseColors.Washi
                        ),
                        startY = 0f,
                        endY = 2000f
                    )
                )
        )

        // Círculos decorativos flotantes (estilo sol naciente y lunas)
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Sol grande arriba a la derecha
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        JapaneseColors.Sunset.copy(alpha = 0.6f),
                        JapaneseColors.Sakura.copy(alpha = 0.3f),
                        Color.Transparent
                    ),
                    radius = 300.dp.toPx()
                ),
                radius = 300.dp.toPx(),
                center = Offset(size.width * 0.8f, size.height * 0.15f)
            )

            // Luna/círculo pequeño arriba izquierda
            drawCircle(
                color = JapaneseColors.Washi.copy(alpha = 0.4f),
                radius = 80.dp.toPx(),
                center = Offset(size.width * 0.2f, size.height * 0.12f)
            )

            // Círculo medio abajo
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        JapaneseColors.Gold.copy(alpha = 0.3f),
                        Color.Transparent
                    ),
                    radius = 200.dp.toPx()
                ),
                radius = 200.dp.toPx(),
                center = Offset(size.width * 0.3f, size.height * 0.85f)
            )
        }

        // Patrón de olas japonesas (seigaiha) muy sutil
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.06f)
        ) {
            val radius = 50.dp.toPx()
            val spacing = radius * 1.2f

            for (y in 0..size.height.toInt() / spacing.toInt() + 2) {
                for (x in 0..size.width.toInt() / spacing.toInt() + 2) {
                    val offset = if (y % 2 == 0) 0f else spacing / 2f

                    // Tres arcos concéntricos para crear el patrón seigaiha
                    for (i in 0..2) {
                        drawArc(
                            color = JapaneseColors.Indigo,
                            startAngle = 0f,
                            sweepAngle = 180f,
                            useCenter = false,
                            topLeft = Offset(
                                x * spacing + offset - radius * (1f + i * 0.3f),
                                y * spacing - radius * (1f + i * 0.3f)
                            ),
                            size = Size(
                                radius * 2 * (1f + i * 0.3f),
                                radius * 2 * (1f + i * 0.3f)
                            ),
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    }
                }
            }
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Sección superior con animación
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(1000)) + slideInVertically(
                    initialOffsetY = { -it / 2 },
                    animationSpec = tween(1000, easing = FastOutSlowInEasing)
                )
            ) {
                TopSection()
            }

            Spacer(modifier = Modifier.weight(1f))

            // Sección de botones con animación
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(1000, delayMillis = 300)) + slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(1000, easing = FastOutSlowInEasing)
                )
            ) {
                ButtonSection(navController = navController)
            }
        }
    }
}

@Composable
private fun TopSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Kanji principal gigante
        Text(
            text = "静寂",
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = JapaneseColors.Sumi.copy(alpha = 0.9f),
            letterSpacing = 8.sp,
            modifier = Modifier.offset(y = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Nombre de la app
        Text(
            text = "Seijaku List",
            fontFamily = RobotoBold,
            fontSize = 32.sp,
            color = JapaneseColors.Koi,
            letterSpacing = 3.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Línea decorativa triple (inspirada en banderas japonesas)
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(3.dp)
                    .background(
                        JapaneseColors.Koi,
                        RoundedCornerShape(2.dp)
                    )
            )

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(JapaneseColors.Gold, CircleShape)
            )

            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(3.dp)
                    .background(
                        JapaneseColors.Koi,
                        RoundedCornerShape(2.dp)
                    )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Descripción
        Text(
            text = "Tu colección personal de\nanime y manga",
            fontFamily = RobotoRegular,
            fontSize = 17.sp,
            color = JapaneseColors.Sumi.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 26.sp,
            letterSpacing = 0.8.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Elementos decorativos flotantes (pétalos de sakura estilizados)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(5) { index ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .rotate(45f * index)
                        .background(
                            JapaneseColors.Sakura.copy(alpha = 0.5f),
                            RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun ButtonSection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Botón de Iniciar Sesión - Estilo rojo koi tradicional
        Button(
            onClick = { navController.navigate(AppDestinations.LOGIN_ROUTE) },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = JapaneseColors.Koi
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Login,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Iniciar sesión",
                    fontFamily = RobotoBold,
                    fontSize = 18.sp,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }
        }

        // Botón de Crear Cuenta - Estilo índigo tradicional
        Button(
            onClick = { navController.navigate(AppDestinations.REGISTER_ROUTE) },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = JapaneseColors.Indigo
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Crear cuenta",
                    fontFamily = RobotoBold,
                    fontSize = 18.sp,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Separador decorativo con símbolo japonés
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.5.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                JapaneseColors.Sakura.copy(alpha = 0.6f)
                            )
                        )
                    )
            )

            // Símbolo decorativo central
            Canvas(modifier = Modifier.size(32.dp)) {
                // Flor de sakura estilizada
                val center = Offset(size.width / 2, size.height / 2)
                val petalRadius = size.minDimension / 6

                for (i in 0 until 5) {
                    val angle = (i * 72 - 90) * (PI / 180).toFloat()
                    val petalCenter = Offset(
                        center.x + cos(angle) * petalRadius * 1.5f,
                        center.y + sin(angle) * petalRadius * 1.5f
                    )
                    drawCircle(
                        color = JapaneseColors.Sakura.copy(alpha = 0.7f),
                        radius = petalRadius,
                        center = petalCenter
                    )
                }

                // Centro dorado
                drawCircle(
                    color = JapaneseColors.Gold.copy(alpha = 0.8f),
                    radius = petalRadius * 0.6f,
                    center = center
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.5.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                JapaneseColors.Sakura.copy(alpha = 0.6f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de Explorar - Estilo minimalista
        TextButton(
            onClick = {
                navController.navigate(AppDestinations.HOME) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Explore,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = JapaneseColors.Sumi.copy(alpha = 0.75f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Explorar sin cuenta",
                fontFamily = RobotoBold,
                fontSize = 17.sp,
                color = JapaneseColors.Sumi.copy(alpha = 0.75f),
                letterSpacing = 0.8.sp
            )
        }
    }
}
