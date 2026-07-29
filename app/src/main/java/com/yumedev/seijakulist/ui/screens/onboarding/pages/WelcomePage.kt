package com.yumedev.seijakulist.ui.screens.onboarding.pages

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.yumedev.seijakulist.ui.screens.onboarding.components.ParallaxLayer
import com.yumedev.seijakulist.ui.theme.SeijakuVoice

/**
 * PANTALLA 1: Bienvenida Emocional - VERSIÓN MEJORADA
 *
 * Estética: Lofi / Chill con gradientes mesh
 *
 * Visual:
 * - Fondo: Gradiente mesh (azul nocturno → púrpura oscuro → cream profundo)
 * - Partículas flotantes decorativas
 * - Kanji 静 en GlassCard con glow animado
 * - Texto "seijaku" debajo
 * - Copy emocional en card de vidrio
 */
@Composable
fun WelcomePage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Fondo con gradiente mesh (estilo lofi nocturno) con parallax
        ParallaxLayer(speedFactor = 0.5f) { offset ->
            MeshGradientBackground(
                colors = listOf(
                    Color(0xFF1A1625), // Azul nocturno oscuro
                    Color(0xFF2D1B3D), // Púrpura oscuro
                    Color(0xFF3D2A2E), // Marrón oscuro cálido
                    MaterialTheme.colorScheme.background
                )
            )
        }

        // Partículas orgánicas con física
        OrganicParticles(
            count = 8,
            color = MaterialTheme.colorScheme.primary
        )

        // Contenido principal centrado con entrada dramática
        DramaticEntrance(delay = 200) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
            ) {
                // Card de vidrio para el kanji con entrada escalonada
                DramaticEntrance(delay = 400) {
                    GlassCard(
                        backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.15f),
                        borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        cornerRadius = 32.dp,
                        modifier = Modifier.size(180.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            // Glow avanzado multi-capa detrás del kanji
                            AdvancedGlow(
                                size = 120.dp,
                                color = MaterialTheme.colorScheme.primary,
                                layers = 4
                            )

                            // Kanji 静 en cream
                            Text(
                                text = "静",
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontSize = 80.sp,
                                    letterSpacing = 0.sp
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Texto "seijaku" con estilo y entrada dramática
                DramaticEntrance(delay = 600) {
                    Text(
                        text = "seijaku",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 28.sp,
                            letterSpacing = 4.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(56.dp))

                // Copy principal en card de vidrio con entrada dramática
                DramaticEntrance(delay = 800) {
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
                                text = "bienvenido a seijaku",
                                style = SeijakuVoice.vozGrande,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Subcopy emocional (Onest)
                            Text(
                                text = "un lugar que no te persigue,\nque no te pide nada,\nque solo está cuando lo necesitás",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center,
                                lineHeight = 27.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
