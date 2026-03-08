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
        versionName = "1.0 (7)",
        versionCode = 7,
        changes = listOf(
            WhatsNewChange(
                type = ChangeType.NEW,
                title = "Prioridad para animes Planeados",
                description = "Ahora podés asignar prioridad Alta, Media o Baja a los animes que tenés planeados ver, y agregar notas personales a cada uno."
            ),
            WhatsNewChange(
                type = ChangeType.NEW,
                title = "Recomendaciones en el detalle",
                description = "Nueva sección \"Si te gustó este anime...\" en la pantalla de detalle con animes relacionados para que sigas descubriendo."
            ),
            WhatsNewChange(
                type = ChangeType.NEW,
                title = "Foro del anime",
                description = "Nueva pestaña en el detalle de cada anime con los temas del foro de MyAnimeList."
            ),
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Estado del anime en español",
                description = "\"En emisión\", \"Finalizado\" y \"Próximamente\" reemplazan los textos en inglés en la ficha de cada anime."
            ),
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Búsqueda de personajes animada",
                description = "El botón de búsqueda en la pestaña de personajes ahora se transforma en una barra de búsqueda con una animación deslizante."
            ),
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Banner de novedades rediseñado",
                description = "El banner que aparece al actualizar tiene un nuevo diseño con gradiente de color e ícono animado."
            ),
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Estados de animes",
                description = "La pantalla \"Mis Animes\" tiene un nuevo layout para los estados, mas moderno, con colores mas intensos y badge de prioridad en caso de tenerlo."
            ),
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Perfil rediseñado",
                description = "La pantalla de perfil fue completamente renovada con un layout más limpio y estadísticas mejor presentadas. (Sujeto a posibles cambios en versiones posteriores)"
            ),
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Fecha de inicio y finalizacion de animes",
                description = "Ahora puedes modificar la fecha de inicio y finalización en la que viste un anime desde la pantalla de Detalle del anime en mi lista"
            ),
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Detalle del anime",
                description = "Se ha renovado la sección del Header del detalle del anime para que sea mas limpio, organizado y con mas detalles!."
            ),
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Subtitulos en el detalle del anime",
                description = "Hemos renovado los subtitulos del detalle del anime para que sean mas claros y con un mejor diseño"
            ),
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Layout de Logros mejorado",
                description = "Se renovaron los iconos de los Logros, ahora mas claros y con un mejor diseño"
            ),
            WhatsNewChange(
                type = ChangeType.FIX,
                title = "Cierre de sesión limpia la lista local",
                description = "Al desloguearte, la base de datos local queda vacía. Así tu lista no queda visible si otra persona usa la app en tu dispositivo."
            ),
            WhatsNewChange(
                type = ChangeType.FIX,
                title = "Top 5 Animes",
                description = "Se realizaron algunas correcciones de UI en la pantalla de Selección de Top 5 Animes."
            ),
            WhatsNewChange(
                type = ChangeType.FIX,
                title = "Aviso al cambiar estado desde Planeado",
                description = "Si un anime tiene prioridad o notas asignadas y cambiás su estado, la app te avisa antes de proceder para que no pierdas esos datos."
            ),
            WhatsNewChange(
                type = ChangeType.FIX,
                title = "Pantalla de error con reintento",
                description = "Cuando falla la carga de un anime por conexión, ahora aparece un botón \"Reintentar\" para no tener que salir y volver a entrar."
            )
        )
    ),
    WhatsNewVersion(
        versionName = "1.0 (6)",
        versionCode = 6,
        changes = listOf(
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Barra de navegación flotante",
                description = "Se ha mejorado el diseño de la Barra de navegación para una experiencia más fluida"
            ),
            WhatsNewChange(
                type = ChangeType.NEW,
                title = "Anime del día",
                description = "Nueva tarjeta en el home que muestra un anime aleatorio para descubrir."
            ),
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Selector de pestañas rediseñado",
                description = "Las pestañas Anime/Manga del home usan el nuevo componente!"
            ),
            WhatsNewChange(
                type = ChangeType.IMPROVED,
                title = "Estadisticas del Perfil",
                description = "Se ha rediseñado el componente de las estadísticas del perfil"
            )
        )
    ),
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