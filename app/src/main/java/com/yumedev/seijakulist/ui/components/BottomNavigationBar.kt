package com.yumedev.seijakulist.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.util.navigation_tools.BottomNavItem

@Composable
fun BottomNavigationBar(navController: NavController, navItems: List<BottomNavItem>) {
    val currentRoute by navController.currentBackStackEntryAsState()
    val route = currentRoute?.destination?.route
    val haptic = LocalHapticFeedback.current

    // 1. ANIMACIÓN DE ENTRADA: La barra sube desde abajo al aparecer
    // Usamos LaunchedEffect para disparar el cambio de valor al inicio
    var isBarVisible by remember { mutableStateOf(false) }
    val barOffset by animateDpAsState(
        targetValue = if (isBarVisible) 0.dp else 100.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "bar_entrance"
    )


    LaunchedEffect(Unit) {
        isBarVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = barOffset)
            .padding(bottom = 12.dp), // Separación estética del borde inferior
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f) // Efecto flotante (no toca los bordes laterales)
                .height(72.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(36.dp),
                    ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                ),
            shape = RoundedCornerShape(36.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f), // Glassmorphism ligero
            border = BorderStroke(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navItems.forEach { item ->
                    val isSelected = route?.startsWith(item.route) == true

                    // --- GESTIÓN DE INTERACCIÓN ---
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()

                    // 2. EFECTO SQUISHY: Escala dinámica al presionar
                    val scale by animateFloatAsState(
                        targetValue = if (isPressed) 0.88f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium),
                        label = "scale_animation"
                    )

                    // 3. SALTO DEL ICONO: Sube un poco cuando está seleccionado
                    val iconOffset by animateDpAsState(
                        targetValue = 0.dp,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioHighBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "icon_jump"
                    )

                    // 4. COLORES ANIMADOS
                    val backgroundColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        animationSpec = tween(400),
                        label = "background_color"
                    )

                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        label = "content_color"
                    )

                    // 5. PESO DINÁMICO: el item seleccionado toma más espacio para mostrar el label
                    val itemWeight by animateFloatAsState(
                        targetValue = if (isSelected) 2.5f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "item_weight"
                    )

                    val itemPaddingHorizontal by animateDpAsState(
                        targetValue = if (isSelected) 12.dp else 0.dp,
                        animationSpec = tween(300),
                        label = "item_padding"
                    )

                    Box(
                        modifier = Modifier
                            .weight(itemWeight)
                            .graphicsLayer(scaleX = scale, scaleY = scale)
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(backgroundColor)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    if (!isSelected) {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                            .padding(horizontal = itemPaddingHorizontal),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = if (isSelected) item.selectedIcon else item.unselectedIcon),
                                contentDescription = item.name,
                                tint = contentColor,
                                modifier = Modifier
                                    .size(24.dp)
                                    .offset(y = iconOffset) // Aplicamos el salto
                            )

                            // 5. EXPANSIÓN DEL TEXTO
                            AnimatedVisibility(
                                visible = isSelected,
                                enter = fadeIn() + expandHorizontally(
                                    expandFrom = Alignment.Start,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                ),
                                exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.Start)
                            ) {
                                Text(
                                    text = item.name,
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontFamily = PoppinsMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    ),
                                    color = contentColor,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}