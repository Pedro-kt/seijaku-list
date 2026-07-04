package com.yumedev.seijakulist.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.R

// ============================================
// SISTEMA SEIJAKU - ONEST FONT FAMILY
// ============================================
// Onest es la fuente de interfaz del sistema Seijaku
// Sans humanista cálida que sostiene el contenido sin llamar la atención

private val OnestFontFamily = FontFamily(
    Font(R.font.onest_regular, FontWeight.Normal),
    Font(R.font.onest_medium, FontWeight.Medium),
    Font(R.font.onest_semibold, FontWeight.SemiBold),
)

// ============================================
// COMPATIBILIDAD CON CÓDIGO LEGACY
// ============================================
// Estas definiciones mantienen compatibilidad con el código existente
// que usa Poppins/Roboto, pero ahora apuntan a Onest del sistema Seijaku
// TODO: Migrar gradualmente a usar MaterialTheme.typography en lugar de estas

/**
 * @deprecated Usar MaterialTheme.typography.bodyLarge/bodyMedium en su lugar.
 * Esta definición ahora usa Onest (sistema Seijaku) en lugar de Poppins.
 */
@Deprecated("Use MaterialTheme.typography instead", ReplaceWith("Onest"))
val PoppinsRegular = OnestFontFamily

/**
 * @deprecated Usar MaterialTheme.typography.titleMedium/labelLarge en su lugar.
 * Esta definición ahora usa Onest (sistema Seijaku) en lugar de Poppins.
 */
@Deprecated("Use MaterialTheme.typography instead", ReplaceWith("Onest"))
val PoppinsMedium = OnestFontFamily

/**
 * @deprecated Usar MaterialTheme.typography.headlineMedium/titleLarge en su lugar.
 * Esta definición ahora usa Onest (sistema Seijaku) en lugar de Poppins.
 */
@Deprecated("Use MaterialTheme.typography instead", ReplaceWith("Onest"))
val PoppinsBold = OnestFontFamily

/**
 * @deprecated Usar MaterialTheme.typography.bodyLarge en su lugar.
 * Esta definición ahora usa Onest (sistema Seijaku).
 */
@Deprecated("Use MaterialTheme.typography instead", ReplaceWith("PoppinsRegular"))
val RobotoRegular = PoppinsRegular

/**
 * @deprecated Usar MaterialTheme.typography.titleLarge en su lugar.
 * Esta definición ahora usa Onest (sistema Seijaku).
 */
@Deprecated("Use MaterialTheme.typography instead", ReplaceWith("PoppinsBold"))
val RobotoBold = PoppinsBold

/**
 * Typography antigua - mantenida solo para compatibilidad
 * @deprecated El tema ahora usa SeijakuTypography automáticamente
 */
@Deprecated("This is automatically applied via Theme.kt", ReplaceWith("SeijakuTypography"))
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = OnestFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = OnestFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
)