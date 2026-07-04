package com.yumedev.seijakulist.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.R

/**
 * SISTEMA TIPOGRÁFICO DE SEIJAKU LIST
 * -----------------------------------
 * Dos familias con roles estrictos, gemelo de la jerarquía de color (cream/salvia):
 *
 *   ONEST  → INTERFAZ. El 90% funcional: títulos, cuerpo, labels, botones, metadata, listas.
 *            Sans humanista cálida que "desaparece": sostiene el contenido sin llamar la atención.
 *            Es el CUERPO de la app.
 *
 *   LORA   → VOZ. Solo la voz de la app y las huellas del usuario. Serif cálida que abraza.
 *            Es el ALMA de la app. Donde Seijaku muestra el corazón.
 *
 * REGLA DE HIERRO (no romper):
 *   Lora aparece ÚNICAMENTE en la voz y las huellas. NUNCA en botones, metadata,
 *   labels ni ningún elemento funcional. Si la serif se escapa a la interfaz, se rompe
 *   el encanto y queda amateur. Igual que la serif italic: solo se usa para lo que el
 *   usuario escribe en sus huellas, jamás en interfaz.
 *
 * CONVENCIÓN DE VOZ:
 *   Los textos de VOZ de la app van SIEMPRE en minúscula ("lo terminaste… cuesta soltarlo, ¿no?").
 *   La minúscula es más íntima y menos declarativa — es parte de la identidad de la voz,
 *   no un capricho. (La interfaz sí usa mayúscula de oración normal.)
 *
 * PRINCIPIO GENERAL: redonda y tranquila, con aire. Nada de itálicas como recurso de
 * estilo en la interfaz (el itálico bold de la versión vieja daba urgencia deportiva:
 * lo opuesto a la quietud).
 *
 * Fuentes esperadas en res/font (empaquetadas localmente, no descarga en runtime,
 * para que la app se vea igual siempre y no haya parpadeo de fuente):
 *   onest_regular (400), onest_medium (500), onest_semibold (600)
 *   lora_regular (400), lora_italic (400 italic)
 */

// ─────────────────────────────────────────────────────────
// FAMILIAS
// ─────────────────────────────────────────────────────────
val Onest = FontFamily(
    Font(R.font.onest_regular, FontWeight.Normal),
    Font(R.font.onest_medium, FontWeight.Medium),
    Font(R.font.onest_semibold, FontWeight.SemiBold),
)

val Lora = FontFamily(
    Font(R.font.lora_regular, FontWeight.Normal),
    Font(R.font.lora_italic, FontWeight.Normal, FontStyle.Italic),
)

// ─────────────────────────────────────────────────────────
// INTERFAZ · escala de Material 3 (todo en Onest)
// Line-heights relajados y letter-spacing suave: la app respira, no aprieta.
// ─────────────────────────────────────────────────────────
val SeijakuTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = Onest, fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp, lineHeight = 40.sp, letterSpacing = (-0.2).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = Onest, fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp, lineHeight = 36.sp, letterSpacing = (-0.2).sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = Onest, fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp, lineHeight = 32.sp, letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = Onest, fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp, lineHeight = 28.sp, letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = Onest, fontWeight = FontWeight.Medium,
        fontSize = 18.sp, lineHeight = 26.sp, letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Onest, fontWeight = FontWeight.Medium,
        fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.1.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = Onest, fontWeight = FontWeight.Medium,
        fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Onest, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.15.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Onest, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.15.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = Onest, fontWeight = FontWeight.Normal,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.2.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = Onest, fontWeight = FontWeight.Medium,
        fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = Onest, fontWeight = FontWeight.Medium,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = Onest, fontWeight = FontWeight.Medium,
        fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp,
    ),
)

/**
 * VOZ · estilos en Lora (fuera de Material 3 a propósito).
 * Usar SOLO para la voz de la app y las huellas. Recordá: textos de voz en minúscula.
 * El color se aplica en el composable (cream para lo emocional, textoPrimario para la voz base).
 */
object SeijakuVoice {

    /** Voz de la app en momentos que pesan (el final, un eco). Ej: "lo terminaste… cuesta soltarlo, ¿no?". */
    val vozGrande = TextStyle(
        fontFamily = Lora, fontWeight = FontWeight.Normal,
        fontSize = 22.sp, lineHeight = 31.sp, letterSpacing = 0.sp,
    )

    /** Voz de la app en momentos livianos / mensajes de cierre. */
    val vozMedia = TextStyle(
        fontFamily = Lora, fontWeight = FontWeight.Normal,
        fontSize = 18.sp, lineHeight = 27.sp, letterSpacing = 0.sp,
    )

    /** El nombre del sentimiento de una huella (ej: "el vacío"). Suele ir en color cream. */
    val huellaEmocion = TextStyle(
        fontFamily = Lora, fontWeight = FontWeight.Medium,
        fontSize = 17.sp, lineHeight = 24.sp, letterSpacing = 0.sp,
    )

    /** El texto que el usuario escribió en su huella. Italic = se siente escrito a mano.
     *  Esta es la ÚNICA italic permitida en toda la app. */
    val huellaTexto = TextStyle(
        fontFamily = Lora, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Italic,
        fontSize = 16.sp, lineHeight = 26.sp, letterSpacing = 0.sp,
    )
}
