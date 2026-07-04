package com.yumedev.seijakulist.ui.screens.configuration

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.IconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.ui.components.confirm_dialog.ConfirmSignOutDialog
import com.yumedev.seijakulist.ui.navigation.AppDestinations
import com.yumedev.seijakulist.ui.theme.ThemeMode

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ConfigurationScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel
) {
    val context = LocalContext.current
    val currentThemeMode by settingsViewModel.themeMode.collectAsState()

    val versionInfo = remember {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            "v${packageInfo.versionName} (${packageInfo.longVersionCode})"
        } catch (e: Exception) {
            "v1.0"
        }
    }

    val userEmail = remember { Firebase.auth.currentUser?.email ?: "No disponible" }
    var showSignOutDialog by remember { mutableStateOf(false) }

    if (showSignOutDialog) {
        ConfirmSignOutDialog(
            onConfirm = {
                // Aquí iría la lógica de sign out
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
        modifier = Modifier.fillMaxSize()
    ) {
        // Header con flecha de navegación
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Configuración",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

        // Sección: Apariencia
        item {
            SettingsSection(title = "Apariencia") {
                SettingsItem(
                    icon = Icons.Outlined.BrightnessAuto,
                    title = "Tema",
                    subtitle = "Personaliza la apariencia de la app"
                ) {
                    ThemeSelector(
                        selectedTheme = currentThemeMode,
                        onThemeSelected = { settingsViewModel.setThemeMode(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    )
                }
            }
        }

        // Sección: Cuenta
        item {
            SettingsSection(title = "Cuenta") {
                SettingsItemInfo(
                    icon = Icons.Default.AccountCircle,
                    title = "Usuario",
                    subtitle = userEmail
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 56.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                SettingsItemNavigable(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    title = "Cerrar sesión",
                    subtitle = "Salir de tu cuenta",
                    onClick = {
                        if (userEmail != "No disponible") {
                            showSignOutDialog = true
                        }
                    }
                )
            }
        }

        // Sección: Soporte
        item {
            SettingsSection(title = "Soporte") {
                SettingsItemNavigable(
                    icon = Icons.Default.BugReport,
                    title = "Reportar error",
                    subtitle = "Envíanos un correo con los detalles",
                    onClick = {
                        navController.navigate(AppDestinations.REPORT_ERROR_ROUTE)
                    }
                )
            }
        }

        // Sección: Información
        item {
            SettingsSection(title = "Información") {
                SettingsItemNavigable(
                    icon = Icons.Default.PrivacyTip,
                    title = "Política de Privacidad",
                    subtitle = "Lee nuestra política de privacidad",
                    onClick = {
                        navController.navigate(AppDestinations.POLICY_PRIVACY_ROUTE)
                    }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 56.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                SettingsItemInfo(
                    icon = Icons.Default.Info,
                    title = "Versión",
                    subtitle = versionInfo
                )
            }
        }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 1.dp
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
        }
        content()
    }
}

@Composable
private fun SettingsItemNavigable(
    icon: ImageVector,
    title: String,
    subtitle: String,
    endText: String? = null,
    showChevron: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        }
        if (endText != null) {
            Text(
                text = endText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
            if (showChevron) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
        if (showChevron) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SettingsItemInfo(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ThemeSelector(
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Primera fila: Claro, Oscuro, Japonés
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant
            ),
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                ThemeOption(
                    text = "Claro",
                    icon = Icons.Outlined.LightMode,
                    isSelected = selectedTheme == ThemeMode.LIGHT,
                    onClick = { onThemeSelected(ThemeMode.LIGHT) },
                    modifier = Modifier.weight(1f),
                    cornerRadius = RoundedCornerShape(
                        topStart = 20.dp,
                        bottomStart = 20.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    )
                )

                if (selectedTheme != ThemeMode.LIGHT) {
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }

                ThemeOption(
                    text = "Oscuro",
                    icon = Icons.Outlined.DarkMode,
                    isSelected = selectedTheme == ThemeMode.DARK,
                    onClick = { onThemeSelected(ThemeMode.DARK) },
                    modifier = Modifier.weight(1f),
                    cornerRadius = RoundedCornerShape(0.dp)
                )

                if (selectedTheme != ThemeMode.DARK) {
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }

                ThemeOption(
                    text = "Japonés",
                    icon = Icons.Outlined.BrightnessAuto,
                    isSelected = selectedTheme == ThemeMode.JAPANESE,
                    onClick = { onThemeSelected(ThemeMode.JAPANESE) },
                    modifier = Modifier.weight(1f),
                    cornerRadius = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 20.dp,
                        bottomEnd = 20.dp
                    )
                )
            }
        }

        // Segunda fila: Sistema
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant
            ),
            color = Color.Transparent
        ) {
            ThemeOption(
                text = "Sistema",
                icon = Icons.Default.PhoneAndroid,
                isSelected = selectedTheme == ThemeMode.SYSTEM,
                onClick = { onThemeSelected(ThemeMode.SYSTEM) },
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
private fun ThemeOption(
    text: String,
    icon: ImageVector?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: RoundedCornerShape
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick),
        shape = cornerRadius,
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null && isSelected) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
