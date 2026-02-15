package com.yumedev.seijakulist.ui.screens.auth_screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AuthScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    settingsViewModel: com.yumedev.seijakulist.ui.screens.configuration.SettingsViewModel
) {
    val currentTheme by settingsViewModel.themeMode.collectAsState()
    var showThemeNotification by remember { mutableStateOf(false) }
    var notificationTheme by remember { mutableStateOf<com.yumedev.seijakulist.ui.theme.ThemeMode?>(null) }

    // Páginas del onboarding
    val pages = remember {
        listOf(
            OnboardingPage(
                title = "Bienvenido a\nSeijakuList",
                description = "Guarda y organiza todos tus animes en un solo lugar",
                icon = Icons.Outlined.Favorite,
                color = Color(0xFF6366F1)
            ),
            OnboardingPage(
                title = "Nunca Olvides\nDónde Te Quedaste",
                description = "Marca episodios vistos y sincroniza tu progreso en la nube",
                icon = Icons.Outlined.Collections,
                color = Color(0xFF8B5CF6)
            ),
            OnboardingPage(
                title = "Descubre Tus\nEstadísticas",
                description = "Analiza tus gustos y el tiempo invertido en tu pasión",
                icon = Icons.Outlined.Analytics,
                color = Color(0xFFEC4899)
            ),
            OnboardingPage(
                title = "Comparte Tu\nTop 5",
                description = "Crea tu lista de favoritos y muéstrala al mundo",
                icon = Icons.Outlined.Share,
                color = Color(0xFF06B6D4)
            )
        )
    }

    val pagerState = rememberPagerState(pageCount = { pages.size + 1 }) // +1 para la página de acciones
    val scope = rememberCoroutineScope()

    // Mostrar notificación cuando cambia el tema
    LaunchedEffect(currentTheme) {
        notificationTheme = currentTheme
        showThemeNotification = true
        delay(2000)
        showThemeNotification = false
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Notificación de cambio de tema
        AnimatedVisibility(
            visible = showThemeNotification,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ) + fadeIn(tween(300)),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ) + fadeOut(tween(300)),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
        ) {
            notificationTheme?.let { theme ->
                ThemeChangeNotification(theme = theme)
            }
        }

        // Toggle de tema en la esquina superior derecha
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 16.dp)
        ) {
            ThemeToggleButton(
                currentTheme = currentTheme,
                onThemeChange = { settingsViewModel.setThemeMode(it) }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Paginador horizontal
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                if (page < pages.size) {
                    // Páginas de onboarding
                    OnboardingPageContent(
                        page = pages[page],
                        pageIndex = page,
                        totalPages = pages.size
                    )
                } else {
                    // Página final con acciones
                    ActionsPage(navController)
                }
            }

            // Indicadores y botones de navegación
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Indicadores de página
                if (pagerState.currentPage < pages.size) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        repeat(pages.size) { index ->
                            PageIndicator(
                                isActive = index == pagerState.currentPage,
                                color = pages[pagerState.currentPage.coerceIn(0, pages.size - 1)].color
                            )
                        }
                    }

                    // Botón siguiente/comenzar
                    Button(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage < pages.size) {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = pages[pagerState.currentPage].color
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (pagerState.currentPage == pages.size - 1) "Comenzar" else "Siguiente",
                                fontFamily = PoppinsBold,
                                fontSize = 16.sp
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Botón saltar
                    if (pagerState.currentPage < pages.size - 1) {
                        TextButton(
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pages.size)
                                }
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                text = "Saltar",
                                fontFamily = PoppinsRegular,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    pageIndex: Int,
    totalPages: Int
) {
    var isVisible by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    // Animación de flotación suave
    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
    )

    LaunchedEffect(Unit) {
        delay(150)
        isVisible = true
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Elementos decorativos de fondo
        DecorativeBackground(page.color, pageIndex)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Número de página decorativo con efecto
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(400)) + scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(400)
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // Círculo de fondo con pulso
                    val pulseScale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "pulse"
                    )

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .scale(pulseScale)
                            .alpha(0.4f)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        page.color.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    Text(
                        text = "0${pageIndex + 1}",
                        fontFamily = PoppinsBold,
                        fontSize = 72.sp,
                        color = page.color.copy(alpha = 0.15f),
                        letterSpacing = (-2).sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Visual específico para cada página con flotación
            AnimatedVisibility(
                visible = isVisible,
                enter = scaleIn(
                    initialScale = 0.8f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) + fadeIn(tween(600, delayMillis = 100))
            ) {
                Box(
                    modifier = Modifier.graphicsLayer {
                        translationY = floatingOffset
                    }
                ) {
                    when (pageIndex) {
                        0 -> AnimeCardPreview(page.color)
                        1 -> ProgressTrackerPreview(page.color)
                        2 -> StatsChartPreview(page.color)
                        3 -> Top5Preview(page.color)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Título
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { 30 },
                    animationSpec = tween(600, delayMillis = 300, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(600, delayMillis = 300))
            ) {
                Text(
                    text = page.title,
                    fontFamily = PoppinsBold,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp,
                    letterSpacing = 0.3.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción con icono decorativo
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { 30 },
                    animationSpec = tween(600, delayMillis = 450, easing = FastOutSlowInEasing)
                ) + fadeIn(tween(600, delayMillis = 450))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Línea decorativa
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(3.dp)
                            .background(
                                page.color.copy(alpha = 0.5f),
                                RoundedCornerShape(2.dp)
                            )
                    )

                    Text(
                        text = page.description,
                        fontFamily = PoppinsRegular,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        letterSpacing = 0.2.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DecorativeBackground(color: Color, pageIndex: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "background")

    // Rotación suave
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Círculo decorativo superior derecha
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 100.dp, y = (-50).dp)
                .align(Alignment.TopEnd)
                .graphicsLayer {
                    rotationZ = rotation
                    alpha = 0.06f
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(color, Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )

        // Círculo decorativo inferior izquierda
        Box(
            modifier = Modifier
                .size(150.dp)
                .offset(x = (-50).dp, y = 50.dp)
                .align(Alignment.BottomStart)
                .graphicsLayer {
                    rotationZ = -rotation / 2
                    alpha = 0.04f
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(color, Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )

        // Formas pequeñas flotantes
        repeat(3) { index ->
            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 40f,
                animationSpec = infiniteRepeatable(
                    animation = tween((2500 + index * 500), easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "particle_$index"
            )

            Box(
                modifier = Modifier
                    .size(10.dp + index * 5.dp)
                    .offset(
                        x = (50 + index * 80).dp,
                        y = (100 + index * 150).dp
                    )
                    .align(Alignment.TopStart)
                    .graphicsLayer {
                        translationY = offsetY
                        alpha = 0.15f
                    }
                    .background(color, CircleShape)
            )
        }
    }
}

// Página 1: Preview de card de anime
@Composable
private fun AnimeCardPreview(accentColor: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "card")

    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )

    Surface(
        modifier = Modifier.size(width = 180.dp, height = 240.dp),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 12.dp,
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.3f),
                            accentColor.copy(alpha = 0.1f)
                        )
                    )
                )
        ) {
            // Efecto de brillo diagonal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.TopStart)
                    .graphicsLayer {
                        rotationZ = -45f
                        alpha = shimmerAlpha * 0.2f
                    }
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.6f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Badge de tipo
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    ) {
                        Text(
                            text = "TV",
                            fontFamily = PoppinsBold,
                            fontSize = 9.sp,
                            color = accentColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }

                    // Badge de rating
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = accentColor
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "8.5",
                                fontFamily = PoppinsBold,
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Título simulado con shimmer
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(18.dp)
                            .background(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f * shimmerAlpha),
                                RoundedCornerShape(4.dp)
                            )
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(16.dp)
                            .background(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f * shimmerAlpha),
                                RoundedCornerShape(4.dp)
                            )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Estado
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = accentColor.copy(alpha = 0.2f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(accentColor, CircleShape)
                                )
                                Text(
                                    text = "Viendo",
                                    fontFamily = PoppinsBold,
                                    fontSize = 11.sp,
                                    color = accentColor
                                )
                            }
                        }

                        // Episodios
                        Text(
                            text = "12/24",
                            fontFamily = PoppinsRegular,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

// Página 2: Progress tracker
@Composable
private fun ProgressTrackerPreview(accentColor: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "progress")

    val progress by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "progress_animation"
    )

    val cloudRotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cloud_rotation"
    )

    Surface(
        modifier = Modifier.size(width = 260.dp, height = 170.dp),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 12.dp,
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Demon Slayer",
                        fontFamily = PoppinsBold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = accentColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Temporada 2",
                                fontFamily = PoppinsRegular,
                                fontSize = 9.sp,
                                color = accentColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "12/24 episodios",
                            fontFamily = PoppinsRegular,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = accentColor.copy(alpha = 0.15f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Barra de progreso animada
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerHighest,
                            RoundedCornerShape(5.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        accentColor,
                                        accentColor.copy(alpha = 0.8f),
                                        accentColor.copy(alpha = 0.6f)
                                    )
                                ),
                                RoundedCornerShape(5.dp)
                            )
                    ) {
                        // Brillo en la barra de progreso
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .fillMaxHeight()
                                .align(Alignment.CenterEnd)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.White.copy(alpha = 0.3f)
                                        )
                                    ),
                                    RoundedCornerShape(5.dp)
                                )
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(accentColor, CircleShape)
                        )
                        Text(
                            text = "50% completado",
                            fontFamily = PoppinsBold,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier
                                .size(16.dp)
                                .graphicsLayer {
                                    rotationZ = cloudRotation
                                }
                        )
                        Text(
                            text = "Sincronizado",
                            fontFamily = PoppinsRegular,
                            fontSize = 10.sp,
                            color = accentColor.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

// Página 3: Stats chart
@Composable
private fun StatsChartPreview(accentColor: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "stats")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "chart_rotation"
    )

    val numberPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "number_pulse"
    )

    Surface(
        modifier = Modifier.size(220.dp),
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 12.dp,
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Círculo de stats (donut chart simplificado)
            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                // Anillo exterior con rotación
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .graphicsLayer {
                            rotationZ = rotation
                        }
                        .background(
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    accentColor,
                                    accentColor.copy(alpha = 0.7f),
                                    accentColor.copy(alpha = 0.4f),
                                    accentColor.copy(alpha = 0.2f),
                                    accentColor
                                )
                            ),
                            shape = CircleShape
                        )
                )

                // Anillo intermedio para efecto 3D
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerHigh,
                            CircleShape
                        )
                )

                // Anillo interior con gradiente
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.surfaceContainerHigh
                                )
                            ),
                            shape = CircleShape
                        )
                )

                // Centro con stats animado
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.scale(numberPulse)
                ) {
                    Text(
                        text = "127",
                        fontFamily = PoppinsBold,
                        fontSize = 36.sp,
                        color = accentColor,
                        letterSpacing = (-1).sp
                    )
                    Text(
                        text = "animes",
                        fontFamily = PoppinsRegular,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            // Etiquetas decorativas alrededor
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatLabelEnhanced("52 vistos", Icons.Default.Check, accentColor)
                    StatLabelEnhanced("48h total", Icons.Default.Timer, accentColor.copy(alpha = 0.7f))
                }
            }
        }
    }
}

@Composable
private fun StatLabelEnhanced(text: String, icon: ImageVector, color: Color) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = text,
                fontFamily = PoppinsBold,
                fontSize = 10.sp,
                color = color
            )
        }
    }
}

@Composable
private fun StatLabel(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = text,
            fontFamily = PoppinsRegular,
            fontSize = 10.sp,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

// Página 4: Top 5 preview
@Composable
private fun Top5Preview(accentColor: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "top5")

    Column(
        modifier = Modifier.width(280.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth(),
                //.padding(horizontal = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Mi Top 5",
                    fontFamily = PoppinsBold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = accentColor.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "2026",
                    fontFamily = PoppinsBold,
                    fontSize = 10.sp,
                    color = accentColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        repeat(3) { index ->
            val shimmerAlpha by infiniteTransition.animateFloat(
                initialValue = 0.8f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween((1200 + index * 200), easing = FastOutSlowInEasing, delayMillis = index * 100),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "shimmer_$index"
            )

            val medalColor = when (index) {
                0 -> Color(0xFFFFD700)  // Oro
                1 -> Color(0xFFC0C0C0)  // Plata
                else -> Color(0xFFCD7F32)  // Bronce
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                shadowElevation = if (index == 0) 8.dp else 4.dp,
                tonalElevation = if (index == 0) 2.dp else 0.dp,
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Medalla con efecto
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        // Glow de fondo
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .scale(shimmerAlpha)
                                .alpha(0.3f)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            medalColor.copy(alpha = 0.4f),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )

                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = medalColor,
                            shadowElevation = 4.dp
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    fontFamily = PoppinsBold,
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        shadow = androidx.compose.ui.graphics.Shadow(
                                            color = Color.Black.copy(alpha = 0.3f),
                                            offset = androidx.compose.ui.geometry.Offset(0f, 2f),
                                            blurRadius = 4f
                                        )
                                    )
                                )
                            }
                        }
                    }

                    // Placeholder del título con efecto shimmer
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(if (index == 0) 0.75f else if (index == 1) 0.85f else 0.65f)
                                .height(14.dp)
                                .background(
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f * shimmerAlpha),
                                    RoundedCornerShape(4.dp)
                                )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.45f)
                                .height(11.dp)
                                .background(
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f * shimmerAlpha),
                                    RoundedCornerShape(4.dp)
                                )
                        )
                    }

                    // Rating con animación
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = accentColor.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = when (index) {
                                    0 -> "9.5"
                                    1 -> "9.0"
                                    else -> "8.0"
                                },
                                fontFamily = PoppinsBold,
                                fontSize = 11.sp,
                                color = accentColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionsPage(navController: NavController) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(150)
        isVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título de bienvenida
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(tween(800)) + slideInVertically(
                initialOffsetY = { -50 },
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¡Listo para\nComenzar!",
                    fontFamily = PoppinsBold,
                    fontSize = 36.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    lineHeight = 42.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Elige cómo quieres continuar",
                    fontFamily = PoppinsRegular,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Botones de acción
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(tween(1000, delayMillis = 300)) + scaleIn(
                initialScale = 0.8f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Botón principal - Login
                ModernAuthButton(
                    label = "Iniciar Sesión",
                    subtitle = "Ya tengo una cuenta",
                    icon = Icons.AutoMirrored.Outlined.Login,
                    isPrimary = true,
                    onClick = { navController.navigate(AppDestinations.LOGIN_ROUTE) }
                )

                // Botón secundario - Register
                ModernAuthButton(
                    label = "Crear Cuenta Nueva",
                    subtitle = "Soy nuevo aquí",
                    icon = Icons.Outlined.PersonAdd,
                    isPrimary = false,
                    onClick = { navController.navigate(AppDestinations.REGISTER_ROUTE) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botón de explorar
                ExploreButton(navController)
            }
        }
    }
}

@Composable
private fun PageIndicator(
    isActive: Boolean,
    color: Color
) {
    val width by animateDpAsState(
        targetValue = if (isActive) 32.dp else 8.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "indicatorWidth"
    )

    Box(
        modifier = Modifier
            .height(8.dp)
            .width(width)
            .background(
                color = if (isActive) color else MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = CircleShape
            )
    )
}

@Composable
private fun ModernAuthButton(
    label: String,
    subtitle: String,
    icon: ImageVector,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "buttonScale",
        finishedListener = {
            if (isPressed) {
                isPressed = false
            }
        }
    )

    val containerColor = if (isPrimary) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceContainerHighest
    }

    val contentColor = if (isPrimary) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    isPressed = true
                    onClick()
                }
            ),
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        shadowElevation = if (isPrimary) 8.dp else 0.dp,
        tonalElevation = if (isPrimary) 0.dp else 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Contenido de texto
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = label,
                    fontFamily = PoppinsBold,
                    fontSize = 18.sp,
                    color = contentColor,
                    letterSpacing = 0.3.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    fontFamily = PoppinsRegular,
                    fontSize = 13.sp,
                    color = contentColor.copy(alpha = 0.7f),
                    letterSpacing = 0.2.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Icono con efecto
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = if (isPrimary) {
                            Color.White
                        } else {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isPrimary) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
private fun ExploreButton(navController: NavController) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    isPressed = true
                    navController.navigate(AppDestinations.HOME) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            ),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.6f),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Explore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Explorar sin cuenta",
                fontFamily = PoppinsBold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = 0.3.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun ThemeToggleButton(
    currentTheme: com.yumedev.seijakulist.ui.theme.ThemeMode,
    onThemeChange: (com.yumedev.seijakulist.ui.theme.ThemeMode) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "themeButtonScale",
        finishedListener = {
            if (isPressed) isPressed = false
        }
    )

    val rotation by animateFloatAsState(
        targetValue = if (isPressed) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "themeButtonRotation"
    )

    // Determinar icono y descripción según el tema actual
    val (icon, description) =
        when (currentTheme) {
        com.yumedev.seijakulist.ui.theme.ThemeMode.SYSTEM -> Icons.Outlined.PhoneAndroid to "Sistema"
        com.yumedev.seijakulist.ui.theme.ThemeMode.LIGHT -> Icons.Outlined.LightMode to "Claro"
        com.yumedev.seijakulist.ui.theme.ThemeMode.DARK -> Icons.Outlined.DarkMode to "Oscuro"
        com.yumedev.seijakulist.ui.theme.ThemeMode.JAPANESE -> Icons.Outlined.Star to "Japonés"
    }

    Surface(
        modifier = Modifier
            .size(48.dp)
            .scale(scale),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.8f),
        tonalElevation = 4.dp,
        shadowElevation = 2.dp
    ) {
        IconButton(
            onClick = {
                isPressed = true
                // Ciclar al siguiente tema
                val nextTheme = when (currentTheme) {
                    com.yumedev.seijakulist.ui.theme.ThemeMode.SYSTEM -> com.yumedev.seijakulist.ui.theme.ThemeMode.LIGHT
                    com.yumedev.seijakulist.ui.theme.ThemeMode.LIGHT -> com.yumedev.seijakulist.ui.theme.ThemeMode.DARK
                    com.yumedev.seijakulist.ui.theme.ThemeMode.DARK -> com.yumedev.seijakulist.ui.theme.ThemeMode.JAPANESE
                    com.yumedev.seijakulist.ui.theme.ThemeMode.JAPANESE -> com.yumedev.seijakulist.ui.theme.ThemeMode.SYSTEM
                }
                onThemeChange(nextTheme)
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = description,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        rotationZ = rotation
                    }
            )
        }
    }
}

@Composable
private fun ThemeChangeNotification(theme: com.yumedev.seijakulist.ui.theme.ThemeMode) {
    val themeName = when (theme) {
        com.yumedev.seijakulist.ui.theme.ThemeMode.SYSTEM -> "Sistema"
        com.yumedev.seijakulist.ui.theme.ThemeMode.LIGHT -> "Claro"
        com.yumedev.seijakulist.ui.theme.ThemeMode.DARK -> "Oscuro"
        com.yumedev.seijakulist.ui.theme.ThemeMode.JAPANESE -> "Japonés"
    }

    Surface(
        modifier = Modifier,
        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Palette,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Tema cambiado a: $themeName",
                fontFamily = PoppinsBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                letterSpacing = 0.2.sp
            )
        }
    }
}
