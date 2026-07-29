package com.yumedev.seijakulist.ui.screens.auth_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.screens.configuration.SettingsViewModel
import com.yumedev.seijakulist.ui.theme.SeijakuVoice

/**
 * AuthScreen - Pantalla de Selección entre Login y Registro
 *
 * Esta es una pantalla SIMPLE que solo ofrece dos opciones:
 * - Iniciar sesión
 * - Crear cuenta
 *
 * El onboarding ya se mostró antes de llegar aquí (en ONBOARDING_ROUTE).
 */
@Composable
fun AuthScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1625),
                        Color(0xFF2D1B3D),
                        MaterialTheme.colorScheme.background
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            // Kanji decorativo
            Text(
                text = "静",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 64.sp,
                    letterSpacing = 0.sp
                ),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Texto "seijaku"
            Text(
                text = "seijaku",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 24.sp,
                    letterSpacing = 3.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(56.dp))

            // Mensaje de bienvenida
            Text(
                text = "¿ya tenés cuenta?",
                style = SeijakuVoice.vozMedia,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botones
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón Iniciar Sesión
                Button(
                    onClick = {
                        navController.navigate(AppDestinations.LOGIN_ROUTE)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Text(
                        text = "iniciar sesión",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 16.sp,
                            letterSpacing = 0.5.sp
                        )
                    )
                }

                // Botón Crear Cuenta
                OutlinedButton(
                    onClick = {
                        navController.navigate(AppDestinations.REGISTER_ROUTE)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "crear cuenta",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 15.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón volver
                TextButton(
                    onClick = {
                        navController.navigate(AppDestinations.HOME) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "volver al inicio",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
