package com.yumedev.seijakulist.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.screens.onboarding.pages.*

/**
 * ONBOARDING DE SEIJAKU LIST - NUEVA VERSIÓN
 * ============================================
 *
 * Filosofía:
 * - Emocional primero, función después
 * - Navegación con botones (no swipe)
 * - 5 pantallas equilibradas: bienvenida → problema → refugio → huellas → CTA
 * - Se muestra solo la primera vez que el usuario intenta crear cuenta o iniciar sesión
 * - Respeta el espacio del usuario: no aparece al abrir la app
 *
 * Flujo:
 * Usuario va a Perfil → toca "Crear cuenta" o "Iniciar sesión"
 * → (primera vez) muestra onboarding → luego va a la pantalla elegida
 * → (siguientes veces) va directo a login/registro
 */
@Composable
fun OnboardingScreen(
    navController: NavController,
    destination: String = "register", // "register" o "login"
    modifier: Modifier = Modifier
) {
    var currentPage by remember { mutableIntStateOf(0) }
    val totalPages = 5

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Contenido principal con transiciones animadas
        AnimatedContent(
            targetState = currentPage,
            label = "onboarding_pages",
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(800, easing = FastOutSlowInEasing)
                ) + slideInHorizontally(
                    initialOffsetX = { it / 3 },
                    animationSpec = tween(800, easing = FastOutSlowInEasing)
                ) togetherWith fadeOut(
                    animationSpec = tween(400)
                ) + slideOutHorizontally(
                    targetOffsetX = { -it / 3 },
                    animationSpec = tween(400)
                )
            }
        ) { page ->
            when (page) {
                0 -> WelcomePage()
                1 -> ProblemPage()
                2 -> RefugePage()
                3 -> HuellasPage()
                4 -> CallToActionPage(
                    onCreateAccount = {
                        // Navegar a registro
                        navController.navigate(AppDestinations.REGISTER_ROUTE) {
                            popUpTo(AppDestinations.ONBOARDING_ROUTE) { inclusive = true }
                        }
                    },
                    onLogin = {
                        // Navegar a login
                        navController.navigate(AppDestinations.LOGIN_ROUTE) {
                            popUpTo(AppDestinations.ONBOARDING_ROUTE) { inclusive = true }
                        }
                    },
                    onExplore = {
                        // Volver a home
                        navController.navigate(AppDestinations.HOME) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        // Indicadores de página (solo en páginas 0-3)
        if (currentPage < 4) {
            PageIndicators(
                pageCount = 4,
                currentPage = currentPage,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 120.dp)
            )
        }

        // Botón "Siguiente" (solo en páginas 0-2)
        if (currentPage < 3) {
            Button(
                onClick = {
                    if (currentPage < totalPages - 1) {
                        currentPage++
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "siguiente",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        // Botón "Siguiente" en página 3 (antes del CTA) lleva a página 4
        if (currentPage == 3) {
            Button(
                onClick = { currentPage = 4 },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "siguiente",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        // Botón "Saltar" solo en primera pantalla
        if (currentPage == 0) {
            TextButton(
                onClick = { currentPage = 4 }, // Va directo al CTA
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 24.dp, end = 24.dp)
            ) {
                Text(
                    text = "saltar",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Indicadores de página (dots)
 * Salvia cuando inactivo, Cream cuando activo
 */
@Composable
private fun PageIndicators(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage
            Box(
                modifier = Modifier
                    .size(if (isActive) 10.dp else 8.dp)
                    .background(
                        color = if (isActive)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
                    .scale(
                        animateFloatAsState(
                            targetValue = if (isActive) 1f else 0.8f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy
                            ),
                            label = "dotScale"
                        ).value
                    )
            )
        }
    }
}
