package com.yumedev.seijakulist.ui.screens.configuration

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.Icons
import androidx.compose.ui.res.painterResource
import com.yumedev.seijakulist.R
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.screens.auth_screen.AuthViewModel
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.ThemeMode
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yumedev.seijakulist.ui.components.confirm_dialog.ConfirmSignOutDialog
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel
) {
    val context = LocalContext.current
    val currentThemeMode by settingsViewModel.themeMode.collectAsState()

    // Obtener versión dinámica desde PackageManager
    val versionInfo = remember {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            "v${packageInfo.versionName} (${packageInfo.longVersionCode})"
        } catch (e: Exception) {
            "v1.0"
        }
    }

    // Obtener email del usuario desde Firebase Auth
    val userEmail = remember { Firebase.auth.currentUser?.email ?: "No disponible" }

    var showSignOutDialog by remember { mutableStateOf(false) }

    if (showSignOutDialog) {
        ConfirmSignOutDialog(
            onConfirm = {
                viewModel.signOut()
                navController.navigate(AppDestinations.AUTH_ROUTE) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            },
            onDismiss = { showSignOutDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.adp())
                .background(MaterialTheme.colorScheme.background)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left_line),
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.adp())
                )
            }
            Text(
                text = "Configuración",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 22.asp(),
                fontFamily = PoppinsBold,
                letterSpacing = 0.3.sp,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 56.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Sección: Apariencia
            item {
                SectionTitle(text = "Apariencia")
            }

            item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ThemePreviewCard(
                            title = "Sistema",
                            subtitle = "Usa el tema del sistema",
                            selected = currentThemeMode == ThemeMode.SYSTEM,
                            icon = Icons.Default.PhoneAndroid,
                            onClick = { settingsViewModel.setThemeMode(ThemeMode.SYSTEM) }
                        )

                        ThemePreviewCard(
                            title = "Claro",
                            subtitle = "Tema claro de la app",
                            selected = currentThemeMode == ThemeMode.LIGHT,
                            colors = listOf(
                                Color(0xFF1E88E5),
                                Color(0xFF64B5F6),
                                Color(0xFFF8F5F5)
                            ),
                            onClick = { settingsViewModel.setThemeMode(ThemeMode.LIGHT) }
                        )

                        ThemePreviewCard(
                            title = "Oscuro",
                            subtitle = "Tema oscuro de la app",
                            selected = currentThemeMode == ThemeMode.DARK,
                            colors = listOf(
                                Color(0xFF1E88E5),
                                Color(0xFF7EE787),
                                Color(0xFF131313)
                            ),
                            onClick = { settingsViewModel.setThemeMode(ThemeMode.DARK) }
                        )

                        ThemePreviewCard(
                            title = "Japonés",
                            subtitle = "Colores tradicionales de Japón",
                            selected = currentThemeMode == ThemeMode.JAPANESE,
                            colors = listOf(
                                Color(0xFFFF69B4),
                                Color(0xFFFFB7C5),
                                Color(0xFF6B9574)
                            ),
                            onClick = { settingsViewModel.setThemeMode(ThemeMode.JAPANESE) }
                        )
                    }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Sección: Cuenta
            item {
                SectionTitle(text = "Cuenta")
            }

            item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Información del usuario
                        SettingItemCard(
                            icon = Icons.Default.AccountCircle,
                            title = "Usuario",
                            subtitle = userEmail,
                            onClick = null
                        )

                        // Cerrar sesión
                        SettingItemCard(
                            icon = Icons.AutoMirrored.Filled.ExitToApp,
                            title = "Cerrar sesión",
                            subtitle = "Salir de tu cuenta",
                            onClick = {
                                if (userEmail != "No disponible") {
                                    showSignOutDialog = true
                                }
                            },
                            showArrow = true
                        )
                    }
            }

            // Sección: Soporte
            item {
                SectionTitle(text = "Soporte")
            }

            item {
                // Reportar error
                SettingItemCard(
                    icon = Icons.Default.BugReport,
                    title = "Reportar error",
                    subtitle = "Envíanos un correo con los detalles del problema",
                    onClick = {
                        navController.navigate(AppDestinations.REPORT_ERROR_ROUTE)
                    },
                    showArrow = true
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Sección: Información
            item {
                SectionTitle(text = "Información")
            }

            item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Política de Privacidad
                        SettingItemCard(
                            icon = Icons.Default.PrivacyTip,
                            title = "Política de Privacidad",
                            subtitle = "Lee nuestra política de privacidad",
                            onClick = {
                                navController.navigate(AppDestinations.POLICY_PRIVACY_ROUTE)
                            },
                            showArrow = true
                        )

                        // Versión
                        SettingItemCard(
                            icon = Icons.Default.Info,
                            title = "Versión",
                            subtitle = versionInfo,
                            onClick = null
                        )
                    }
            }

            // Espaciado final
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }

}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 21.asp(),
        fontFamily = PoppinsBold,
        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
        color = MaterialTheme.colorScheme.onSurface,
        letterSpacing = 0.3.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
    )
}

@Composable
private fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)?,
    showArrow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícono con gradiente
        Box(
            modifier = Modifier
                .size(40.adp())
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.adp())
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Texto
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.asp(),
                fontWeight = FontWeight.Medium,
                fontFamily = PoppinsBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 13.asp(),
                    fontFamily = PoppinsRegular,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        // Flecha (si es clickeable)
        if (showArrow && onClick != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(20.adp())
            )
        }
    }
}

@Composable
private fun SettingItemCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)?,
    showArrow: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick ?: {},
        enabled = onClick != null,
        interactionSource = interactionSource
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono con gradiente
            Box(
                modifier = Modifier
                    .size(40.adp())
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                            )
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.adp())
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Texto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.asp(),
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (subtitle.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontSize = 13.asp(),
                        fontFamily = PoppinsRegular,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            // Flecha (si es clickeable)
            if (showArrow && onClick != null) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.adp())
                )
            }
        }
    }
}

@Composable
private fun SettingItemWithSwitch(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícono con gradiente
        Box(
            modifier = Modifier
                .size(40.adp())
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.adp())
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Texto
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.asp(),
                fontWeight = FontWeight.Medium,
                fontFamily = PoppinsBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 13.asp(),
                    fontFamily = PoppinsRegular,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        // Switch
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}

@Composable
private fun ThemePreviewCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    colors: List<Color>? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_scale"
    )

    // Animación para el checkmark
    val checkmarkScale by animateFloatAsState(
        targetValue = if (selected) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "checkmark_scale"
    )

    val checkmarkAlpha by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "checkmark_alpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        shape = RoundedCornerShape(16.dp),
        border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        onClick = onClick,
        interactionSource = interactionSource
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Texto a la izquierda
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.asp(),
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 13.asp(),
                    fontFamily = PoppinsRegular,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Mostrar ícono + texto "Adaptativo" o círculos de colores
            if (icon != null) {
                // Ícono con fondo + texto
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.adp())
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.adp())
                        )
                    }
                    Text(
                        text = "Adaptativo",
                        fontSize = 13.asp(),
                        fontFamily = PoppinsMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            } else if (colors != null) {
                // Círculos de colores
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(32.adp())
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Checkmark con animación
            Box(
                modifier = Modifier
                    .size(28.adp())
                    .graphicsLayer {
                        scaleX = checkmarkScale
                        scaleY = checkmarkScale
                        alpha = checkmarkAlpha
                    }
                    .clip(CircleShape)
                    .background(
                        if (selected) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (selected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Seleccionado",
                        tint = Color.White,
                        modifier = Modifier.size(18.adp())
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeOption(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Radio button
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Texto
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.asp(),
                fontWeight = FontWeight.Medium,
                fontFamily = PoppinsBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 13.asp(),
                    fontFamily = PoppinsRegular,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}