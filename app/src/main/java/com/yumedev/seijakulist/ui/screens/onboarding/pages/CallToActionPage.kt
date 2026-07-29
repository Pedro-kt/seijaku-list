package com.yumedev.seijakulist.ui.screens.onboarding.pages

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.ui.screens.onboarding.components.FloatingParticles
import com.yumedev.seijakulist.ui.screens.onboarding.components.GlassCard
import com.yumedev.seijakulist.ui.screens.onboarding.components.MeshGradientBackground
import com.yumedev.seijakulist.ui.screens.onboarding.components.DramaticEntrance
import com.yumedev.seijakulist.ui.screens.onboarding.components.OrganicParticles
import com.yumedev.seijakulist.ui.screens.onboarding.components.AdvancedGlow
import com.yumedev.seijakulist.ui.theme.SeijakuVoice

/**
 * PANTALLA 5: Call to Action - VERSIÓN MEJORADA
 *
 * Visual:
 * - Escena acogedora con gradientes cálidos
 * - Kanji decorativo con glow
 * - Botones con mejor jerarquía y efectos
 * - Diseño más moderno y atractivo
 */
@Composable
fun CallToActionPage(
    modifier: Modifier = Modifier,
    onCreateAccount: () -> Unit,
    onLogin: () -> Unit,
    onExplore: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Fondo con gradiente cálido acogedor
        MeshGradientBackground(
            colors = listOf(
                Color(0xFF2D1F28), // Púrpura cálido
                Color(0xFF3A2A28), // Marrón cálido
                Color(0xFF2A3028), // Verde oscuro cálido
                MaterialTheme.colorScheme.background
            )
        )

        // Partículas orgánicas con física
        OrganicParticles(
            count = 10,
            color = MaterialTheme.colorScheme.primary
        )

        DramaticEntrance(delay = 200) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
            ) {
                // Kanji decorativo con glow avanzado multi-capa
                DramaticEntrance(delay = 400) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        // Glow avanzado multi-capa
                        AdvancedGlow(
                            size = 140.dp,
                            color = MaterialTheme.colorScheme.primary,
                            layers = 4
                        )

                        Text(
                            text = "静",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = 64.sp,
                                letterSpacing = 0.sp
                            ),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Copy principal en card de vidrio con entrada dramática
                DramaticEntrance(delay = 600) {
                    GlassCard(
                        backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.12f),
                        borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        cornerRadius = 24.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Copy principal (Lora, cream)
                            Text(
                                text = "bienvenido a tu refugio",
                                style = SeijakuVoice.vozGrande,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Subcopy emocional (Lora, voz media)
                            Text(
                                text = "comenzá a guardar\nlo que te hizo sentir",
                                style = SeijakuVoice.vozMedia,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center,
                                lineHeight = 27.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Botones con mejor diseño y jerarquía con entrada escalonada
                DramaticEntrance(delay = 800) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Primario: Crear cuenta (Cream filled con gradiente)
                        Button(
                            onClick = onCreateAccount,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(58.dp),
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
                                text = "crear cuenta",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 16.sp,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }

                        // Secundario: Iniciar sesión (Cream outlined con efecto glass)
                        OutlinedButton(
                            onClick = onLogin,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.05f),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(
                                width = 1.5.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "iniciar sesión",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 15.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Terciario: Explorar sin cuenta (text button, salvia)
                        TextButton(
                            onClick = onExplore,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "explorar sin cuenta",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}
