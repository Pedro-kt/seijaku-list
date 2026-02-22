package com.yumedev.seijakulist.ui.components

// ─────────────────────────────────────────────────────────────────────────────
// Datos del sistema de novedades
// Para cada release: añadir un WhatsNewVersion al INICIO de WHATS_NEW_HISTORY
// y subir el versionCode en build.gradle.kts
// ─────────────────────────────────────────────────────────────────────────────

enum class ChangeType {
    NEW,      // Nueva funcionalidad
    IMPROVED, // Mejora sobre algo existente
    FIX       // Corrección de bug
}

data class WhatsNewChange(
    val type: ChangeType,
    val title: String,
    val description: String
)

data class WhatsNewVersion(
    val versionName: String,
    val versionCode: Int,
    val changes: List<WhatsNewChange>
)

// Historial de versiones — el más reciente va PRIMERO
val WHATS_NEW_HISTORY = listOf(
    WhatsNewVersion(
        versionName = "1.0 (5)",
        versionCode = 5,
        changes = listOf(
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Navegación sin recargas",
                description = "Las listas del home se mantienen al volver de una pantalla de detalle."
            ),
            WhatsNewChange(
                type = ChangeType.NEW,
                title = "Confirmación al cerrar sesión",
                description = "Diálogo de confirmación para evitar cierres de sesión accidentales."
            ),
            WhatsNewChange(
                type = ChangeType.NEW,
                title = "Pantalla de novedades",
                description = "Historial de actualizaciones accesible desde el banner al actualizar."
            ),
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Carga del perfil más rápida",
                description = "Las estadísticas ahora se calculan en una sola operación."
            )
        )
    ),
    WhatsNewVersion(
        versionName = "1.0 (4)",
        versionCode = 4,
        changes = listOf(
            WhatsNewChange(
                type = ChangeType.NEW,
                title = "Sincronización en la nube",
                description = "Accede a tu lista desde cualquier dispositivo."
            ),
            WhatsNewChange(
                type = ChangeType.NEW,
                title = "Mejoras en la interfaz",
                description = "Colores de estado corregidos"
            ),
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Pantalla de bienvenida rediseñada",
                description = "Se ha rediseñado por completo la pantalla de bienvenida, login y register"
            ),
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Nuevo sistema de iconos",
                description = "Iconografía renovada en la navegacion para una experiencia más coherente."
            ),
            WhatsNewChange(
                type = ChangeType.FIX,
                title = "Correcciones generales",
                description = "Mejoras de rendimiento y estabilidad."
            )
        )
    )
)

val CURRENT_WHATS_NEW: WhatsNewVersion get() = WHATS_NEW_HISTORY.first()