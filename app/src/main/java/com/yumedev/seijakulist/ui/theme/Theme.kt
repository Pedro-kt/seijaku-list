package com.yumedev.seijakulist.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    background = DarkBackground,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceDim = DarkSurfaceDim,
    surfaceBright = DarkSurfaceBright,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest,
    surfaceContainerLow = DarkSurfaceContainerLow,
    surfaceContainerLowest = DarkSurfaceContainerLowest,
    inversePrimary = DarkInversePrimary,
    inverseSurface = DarkInverseSurface,
    inverseOnSurface = DarkInverseOnSurface,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    scrim = DarkScrim
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    background = LightBackground,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceDim = LightSurfaceDim,
    surfaceBright = LightSurfaceBright,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest,
    surfaceContainerLow = LightSurfaceContainerLow,
    surfaceContainerLowest = LightSurfaceContainerLowest,
    inversePrimary = LightInversePrimary,
    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    scrim = LightScrim
)

// Tema Japonés Oscuro
private val JapaneseDarkColorScheme = darkColorScheme(
    primary = JapaneseDarkPrimary,
    onPrimary = JapaneseDarkOnPrimary,
    background = JapaneseDarkBackground,
    primaryContainer = JapaneseDarkPrimaryContainer,
    onPrimaryContainer = JapaneseDarkOnPrimaryContainer,
    secondary = JapaneseDarkSecondary,
    onSecondary = JapaneseDarkOnSecondary,
    secondaryContainer = JapaneseDarkSecondaryContainer,
    onSecondaryContainer = JapaneseDarkOnSecondaryContainer,
    tertiary = JapaneseDarkTertiary,
    onTertiary = JapaneseDarkOnTertiary,
    tertiaryContainer = JapaneseDarkTertiaryContainer,
    onTertiaryContainer = JapaneseDarkOnTertiaryContainer,
    error = JapaneseDarkError,
    onError = JapaneseDarkOnError,
    errorContainer = JapaneseDarkErrorContainer,
    onErrorContainer = JapaneseDarkOnErrorContainer,
    onBackground = JapaneseDarkOnSurface,
    surface = JapaneseDarkSurface,
    onSurface = JapaneseDarkOnSurface,
    surfaceDim = JapaneseDarkSurfaceDim,
    surfaceBright = JapaneseDarkSurfaceBright,
    surfaceContainer = JapaneseDarkSurfaceContainer,
    surfaceContainerHigh = JapaneseDarkSurfaceContainerHigh,
    surfaceContainerHighest = JapaneseDarkSurfaceContainerHighest,
    surfaceContainerLow = JapaneseDarkSurfaceContainerLow,
    surfaceContainerLowest = JapaneseDarkSurfaceContainerLowest,
    inversePrimary = JapaneseDarkInversePrimary,
    inverseSurface = JapaneseDarkInverseSurface,
    inverseOnSurface = JapaneseDarkInverseOnSurface,
    outline = JapaneseDarkOutline,
    outlineVariant = JapaneseDarkOutlineVariant,
    scrim = JapaneseDarkScrim
)

// Tema Japonés Claro
private val JapaneseLightColorScheme = lightColorScheme(
    primary = JapaneseLightPrimary,
    onPrimary = JapaneseLightOnPrimary,
    background = JapaneseLightBackground,
    primaryContainer = JapaneseLightPrimaryContainer,
    onPrimaryContainer = JapaneseLightOnPrimaryContainer,
    secondary = JapaneseLightSecondary,
    onSecondary = JapaneseLightOnSecondary,
    secondaryContainer = JapaneseLightSecondaryContainer,
    onSecondaryContainer = JapaneseLightOnSecondaryContainer,
    tertiary = JapaneseLightTertiary,
    onTertiary = JapaneseLightOnTertiary,
    tertiaryContainer = JapaneseLightTertiaryContainer,
    onTertiaryContainer = JapaneseLightOnTertiaryContainer,
    error = JapaneseLightError,
    onError = JapaneseLightOnError,
    errorContainer = JapaneseLightErrorContainer,
    onErrorContainer = JapaneseLightOnErrorContainer,
    onBackground = JapaneseLightOnSurface,
    surface = JapaneseLightSurface,
    onSurface = JapaneseLightOnSurface,
    surfaceDim = JapaneseLightSurfaceDim,
    surfaceBright = JapaneseLightSurfaceBright,
    surfaceContainer = JapaneseLightSurfaceContainer,
    surfaceContainerHigh = JapaneseLightSurfaceContainerHigh,
    surfaceContainerHighest = JapaneseLightSurfaceContainerHighest,
    surfaceContainerLow = JapaneseLightSurfaceContainerLow,
    surfaceContainerLowest = JapaneseLightSurfaceContainerLowest,
    inversePrimary = JapaneseLightInversePrimary,
    inverseSurface = JapaneseLightInverseSurface,
    inverseOnSurface = JapaneseLightInverseOnSurface,
    outline = JapaneseLightOutline,
    outlineVariant = JapaneseLightOutlineVariant,
    scrim = JapaneseLightScrim
)

@Composable
fun SeijakuListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Para desactivar el color dinámico y usar tu paleta, cambia a 'false'
    dynamicColor: Boolean = true,
    // Nuevo parámetro para activar el tema japonés
    useJapaneseTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Si el tema japonés está activado, SIEMPRE usar la paleta japonesa (ignora dynamicColor)
        useJapaneseTheme -> {
            if (darkTheme) JapaneseDarkColorScheme else JapaneseLightColorScheme
        }
        // Si no está activado el tema japonés, usar el comportamiento normal (dinámico o estándar)
        !useJapaneseTheme && dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}