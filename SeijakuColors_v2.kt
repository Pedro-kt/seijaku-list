package com.seijaku.list.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * SISTEMA DE COLOR DE SEIJAKU LIST
 * ---------------------------------
 * Principios que gobiernan toda decisión visual:
 *   1. QUIETUD  — el 90% de la pantalla es fondo + texto. El color entra solo donde importa.
 *   2. CALIDEZ  — nada es frío ni clínico. Dark: carbón TEMPLADO (tira a tinta, no a azul).
 *                 Light: PAPEL cálido (hueso, tipo cuaderno), nunca blanco puro.
 *   3. REFUGIO  — el color acompaña a lo que el usuario ama, no compite con ello.
 *
 * JERARQUÍA DE ACENTOS (regla de hierro, vale en AMBOS temas):
 *   - CREAM  = acento PRINCIPAL. "Lo tuyo, lo emocional": huellas, obra-refugio, voz,
 *              momentos que importan. El cream HABLA.
 *   - SALVIA = acento SECUNDARIO. "El aire, la calma": estados calmos, detalles, secciones.
 *              La salvia ACOMPAÑA.
 *   NUNCA cream y salvia al mismo nivel. Cream manda, salvia apoya. Siempre.
 *
 * IMPORTANTE — EL LIGHT NO ES EL DARK INVERTIDO:
 *   Sobre papel, el cream claro DESAPARECE. En light el cream se recalibra a un caramelo
 *   tostado (oscuro) para contrastar. Los estados también bajan a tono tierra sobre tintes
 *   claros. No reusar valores de un tema en el otro: cada tema tiene su set.
 *
 * ESTADOS SEMÁNTICOS: son la ÚNICA excepción de color "vivo" de la app, porque son
 * información funcional (escaneo de listas). Aun así, apagados y separados por TONO
 * (cada uno en su zona del círculo cromático) para distinguirse sin gritar.
 *
 * Construir SIEMPRE pensando en los dos temas desde el día uno (no hacer todo en dark
 * y portar después). Cada pantalla, componente y huella se testea en ambos modos.
 */
object SeijakuColors {

    // ═════════════════════════════════════════════════════════
    //  TEMA OSCURO · carbón templado
    // ═════════════════════════════════════════════════════════
    object Dark {
        // Fondo
        val fondoBase = Color(0xFF1A1917)      // página
        val fondoCard = Color(0xFF232220)      // cards
        val fondoElevado = Color(0xFF2E2B28)   // modales, sheets
        val borde = Color(0xFF3A3733)

        // Texto (cálido, nunca blanco puro)
        val textoPrimario = Color(0xFFECE7DE)
        val textoSecundario = Color(0xFFA39E95)
        val textoTenue = Color(0xFF6E6A63)

        // Acento principal · cream
        val cream = Color(0xFFD9C4A3)
        val creamClaro = Color(0xFFE7D8BC)
        val creamProfundo = Color(0xFFB89B72)
        val sobreCream = Color(0xFF3A2F1B)     // texto encima de superficie cream

        // Acento secundario · salvia
        val salvia = Color(0xFF93A088)
        val salviaClara = Color(0xFFA9B49F)
        val salviaProfunda = Color(0xFF6F7C66)
        val sobreSalvia = Color(0xFF232A1E)

        // Estados (vivos-funcionales, separados por tono)
        val estadoPlaneado = Color(0xFF9B86C4)     // lavanda
        val estadoViendo = Color(0xFF6FA9C7)       // celeste
        val estadoCompletado = Color(0xFF7FB56E)   // verde
        val estadoPausado = Color(0xFFD6A94E)      // ámbar
        val estadoAbandonado = Color(0xFFD2705A)   // terracota
        // Tintes de fondo para chips de estado (pill background)
        val estadoPlaneadoBg = Color(0xFF2E2A38)
        val estadoViendoBg = Color(0xFF22303A)
        val estadoCompletadoBg = Color(0xFF26321F)
        val estadoPausadoBg = Color(0xFF362D1B)
        val estadoAbandonadoBg = Color(0xFF38241E)

        val error = Color(0xFFD2705A)
        val sobreError = Color(0xFF38241E)
    }

    // ═════════════════════════════════════════════════════════
    //  TEMA CLARO · papel suave (recalibrado, NO invertido)
    // ═════════════════════════════════════════════════════════
    object Light {
        // Fondo (papel cálido, no blanco)
        val fondoBase = Color(0xFFF2EDE3)      // página · papel suave
        val fondoCard = Color(0xFFFBF7EF)      // cards (un pelín más claras que el fondo)
        val fondoElevado = Color(0xFFFFFFFF)   // modales, sheets
        val borde = Color(0xFFE0D8C8)

        // Texto (marrón oscuro cálido, nunca negro puro)
        val textoPrimario = Color(0xFF2A2621)
        val textoSecundario = Color(0xFF6E685D)
        val textoTenue = Color(0xFF9A9184)

        // Acento principal · cream RECALIBRADO a caramelo tostado (para verse sobre papel)
        val cream = Color(0xFF8A6A38)          // el "cream" del light: dorado oscuro
        val creamClaro = Color(0xFFA9854D)     // variante (hover / brillos)
        val creamProfundo = Color(0xFF6B5228)  // más peso
        val sobreCream = Color(0xFFFBF7EF)     // texto encima de superficie cream (clara)

        // Acento secundario · salvia (bajada de luminosidad)
        val salvia = Color(0xFF5E7150)
        val salviaClara = Color(0xFF7A8C6C)
        val salviaProfunda = Color(0xFF44532F)
        val sobreSalvia = Color(0xFFF2EDE3)

        // Estados (tono tierra sobre tintes claros)
        val estadoPlaneado = Color(0xFF6E5EA0)     // lavanda profunda
        val estadoViendo = Color(0xFF3F7E9C)       // celeste profundo
        val estadoCompletado = Color(0xFF40632E)   // verde tierra
        val estadoPausado = Color(0xFF9A7420)      // ámbar tostado
        val estadoAbandonado = Color(0xFFA5502F)   // terracota profunda
        // Tintes de fondo para chips de estado
        val estadoPlaneadoBg = Color(0xFFE7E1F0)
        val estadoViendoBg = Color(0xFFDDE8EE)
        val estadoCompletadoBg = Color(0xFFDCE6D2)
        val estadoPausadoBg = Color(0xFFF0E6CC)
        val estadoAbandonadoBg = Color(0xFFF2DED4)

        val error = Color(0xFFA5502F)
        val sobreError = Color(0xFFF2DED4)
    }
}

/**
 * ColorScheme de Material 3 · TEMA OSCURO.
 * primary = cream (el que habla). secondary = salvia (el que acompaña).
 */
val SeijakuDarkColorScheme: ColorScheme = darkColorScheme(
    primary = SeijakuColors.Dark.cream,
    onPrimary = SeijakuColors.Dark.sobreCream,
    primaryContainer = SeijakuColors.Dark.creamProfundo,
    onPrimaryContainer = SeijakuColors.Dark.sobreCream,
    secondary = SeijakuColors.Dark.salvia,
    onSecondary = SeijakuColors.Dark.sobreSalvia,
    secondaryContainer = SeijakuColors.Dark.salviaProfunda,
    onSecondaryContainer = SeijakuColors.Dark.sobreSalvia,
    tertiary = SeijakuColors.Dark.creamClaro,
    onTertiary = SeijakuColors.Dark.sobreCream,
    background = SeijakuColors.Dark.fondoBase,
    onBackground = SeijakuColors.Dark.textoPrimario,
    surface = SeijakuColors.Dark.fondoCard,
    onSurface = SeijakuColors.Dark.textoPrimario,
    surfaceVariant = SeijakuColors.Dark.fondoElevado,
    onSurfaceVariant = SeijakuColors.Dark.textoSecundario,
    outline = SeijakuColors.Dark.borde,
    outlineVariant = SeijakuColors.Dark.textoTenue,
    error = SeijakuColors.Dark.error,
    onError = SeijakuColors.Dark.sobreError,
)

/**
 * ColorScheme de Material 3 · TEMA CLARO (papel).
 * Mismos roles, valores recalibrados para papel. NO reusa los del dark.
 */
val SeijakuLightColorScheme: ColorScheme = lightColorScheme(
    primary = SeijakuColors.Light.cream,
    onPrimary = SeijakuColors.Light.sobreCream,
    primaryContainer = SeijakuColors.Light.creamClaro,
    onPrimaryContainer = SeijakuColors.Light.sobreCream,
    secondary = SeijakuColors.Light.salvia,
    onSecondary = SeijakuColors.Light.sobreSalvia,
    secondaryContainer = SeijakuColors.Light.salviaClara,
    onSecondaryContainer = SeijakuColors.Light.sobreSalvia,
    tertiary = SeijakuColors.Light.creamProfundo,
    onTertiary = SeijakuColors.Light.sobreCream,
    background = SeijakuColors.Light.fondoBase,
    onBackground = SeijakuColors.Light.textoPrimario,
    surface = SeijakuColors.Light.fondoCard,
    onSurface = SeijakuColors.Light.textoPrimario,
    surfaceVariant = SeijakuColors.Light.fondoElevado,
    onSurfaceVariant = SeijakuColors.Light.textoSecundario,
    outline = SeijakuColors.Light.borde,
    outlineVariant = SeijakuColors.Light.textoTenue,
    error = SeijakuColors.Light.error,
    onError = SeijakuColors.Light.sobreError,
)
