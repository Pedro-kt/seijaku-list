package com.yumedev.seijakulist.ui.screens.onboarding.pages

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
 * PANTALLA 3: La Solución - El Refugio - VERSIÓN MEJORADA
 *
 * Visual:
 * - Mockup "fotográfico" de teléfono con la app
 * - Efecto 3D con sombras profundas
 * - Elementos flotantes (cherry blossoms)
 * - Gradiente cálido (cream → salvia)
 */
@Composable
fun RefugePage(modifier: Modifier = Modifier) {
    // Animación de float del mockup
    val infiniteTransition = rememberInfiniteTransition(label = "phone_float")
    val phoneOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    // Animación de rotación suave
    val phoneRotation by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Fondo con gradiente cálido (cream → salvia)
        MeshGradientBackground(
            colors = listOf(
                Color(0xFF3D2E28), // Cream profundo
                Color(0xFF2E3A2A), // Salvia oscuro
                MaterialTheme.colorScheme.background
            )
        )

        // Partículas orgánicas con movimiento
        OrganicParticles(
            count = 6,
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
                // Mockup de teléfono con efecto 3D y glow avanzado
                DramaticEntrance(delay = 400) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        // Glow avanzado detrás del teléfono
                        AdvancedGlow(
                            size = 240.dp,
                            color = MaterialTheme.colorScheme.primary,
                            layers = 3
                        )

                        // Mockup del teléfono
                        Box(
                            modifier = Modifier
                                .offset(y = phoneOffset.dp)
                                .size(width = 200.dp, height = 400.dp)
                                .shadow(
                                    elevation = 32.dp,
                                    shape = RoundedCornerShape(32.dp),
                                    ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                )
                                .clip(RoundedCornerShape(32.dp))
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
                                            MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                                        )
                                    )
                                )
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(32.dp)
                                )
                                .padding(12.dp)
                        ) {
                            // Pantalla del teléfono simulada
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(MaterialTheme.colorScheme.background),
                                contentAlignment = Alignment.Center
                            ) {
                                // Simulación de contenido de la app
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    // Kanji pequeño
                                    Text(
                                        text = "静",
                                        style = MaterialTheme.typography.displaySmall.copy(
                                            fontSize = 40.sp
                                        ),
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Mini card simulando contenido
                                    repeat(3) { index ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(40.dp)
                                                .padding(vertical = 4.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    MaterialTheme.colorScheme.surface.copy(
                                                        alpha = 0.3f + (index * 0.1f)
                                                    )
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(56.dp))

                // Copy en card de vidrio con entrada dramática
                DramaticEntrance(delay = 800) {
                    GlassCard(
                        backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.15f),
                        borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        cornerRadius = 24.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Copy principal (Lora, cream)
                            Text(
                                text = "seijaku es tu refugio",
                                style = SeijakuVoice.vozGrande,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Subcopy (Onest)
                            Text(
                                text = "sin likes, sin seguidores,\nsin la mirada de nadie",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center,
                                lineHeight = 27.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Línea final
                            Text(
                                text = "solo vos y lo que te hizo sentir",
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
