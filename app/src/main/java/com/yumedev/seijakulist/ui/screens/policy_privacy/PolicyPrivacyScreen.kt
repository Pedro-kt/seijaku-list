package com.yumedev.seijakulist.ui.screens.policy_privacy

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

@Composable
fun PolicyPrivacyScreen(navController: NavController) {
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName ?: "1.0.0"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        // --- HEADER & INTRO ---
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Seijaku List",
                fontSize = 18.asp(),
                color = MaterialTheme.colorScheme.primary,
                fontFamily = PoppinsBold
            )
            Text(
                text = "Última actualización: Mayo 2026",
                fontSize = 13.asp(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontFamily = PoppinsRegular,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            PolicyHighlight(
                "Seijaku List respeta tu privacidad. Al usar la aplicación, aceptas la recopilación " +
                        "y el uso de información de acuerdo con esta política."
            )
        }

        // --- 1. INFORMACIÓN QUE RECOPILAMOS ---
        item {
            PolicyTitle("1. Información que Recopilamos")

            PolicySubTitle("1.1 Información de Cuenta")
            PolicyBody("Cuando creas una cuenta, recopilamos:")
            PolicyBulletPoint(
                listOf(
                    "Correo electrónico",
                    "Nombre de usuario",
                    "Contraseña cifrada",
                    "Foto de perfil y biografía (opcional)"
                )
            )

            PolicySubTitle("1.2 Datos de Uso")
            PolicyBulletPoint(
                listOf(
                    "Lista de animes y mangas en tu colección",
                    "Estados (viendo, completado, pendiente, etc.)",
                    "Progreso de episodios vistos",
                    "Calificaciones y reseñas",
                    "Logros desbloqueados",
                    "Preferencias de la aplicación"
                )
            )

            PolicySubTitle("1.3 Información Técnica")
            PolicyBulletPoint(
                listOf(
                    "Modelo y tipo de dispositivo",
                    "Versión del sistema operativo",
                    "Identificador único del dispositivo",
                    "Registros de errores (crashlytics)"
                )
            )

            PolicySubTitle("1.4 Datos de Terceros")
            PolicyBody("Usamos la API de Jikan (MyAnimeList) para obtener información pública sobre animes, mangas, personajes y estudios. No incluye datos personales.")
        }

        // --- 2. CÓMO USAMOS TU INFORMACIÓN ---
        item {
            PolicyTitle("2. Cómo Usamos tu Información")
            PolicyBulletPoint(
                listOf(
                    "Proporcionar y mantener las funciones de la app",
                    "Crear y gestionar tu cuenta",
                    "Sincronizar datos entre dispositivos",
                    "Personalizar tu experiencia",
                    "Detectar y solucionar problemas técnicos",
                    "Generar estadísticas anónimas para mejorar la app"
                )
            )
        }

        // --- 3. ALMACENAMIENTO Y PROTECCIÓN ---
        item {
            PolicyTitle("3. Almacenamiento y Protección")
            PolicySubTitle("3.1 Almacenamiento Local")
            PolicyBody("Tus datos se almacenan en una base de datos local (Room/SQLite) protegida por las medidas de seguridad de Android.")

            PolicySubTitle("3.2 Almacenamiento en la Nube")
            PolicyBody("Usamos Firebase (Google Cloud) para sincronizar tus datos. La información se cifra durante la transmisión y en el servidor.")

            PolicySubTitle("3.3 Seguridad")
            PolicyBody("Implementamos cifrado HTTPS/SSL y autenticación segura. Sin embargo, ningún método de transmisión por internet es completamente seguro.")
        }

        // --- 4. COMPARTIR INFORMACIÓN ---
        item {
            PolicyTitle("4. Compartir Información")
            PolicyHighlight("NO vendemos, intercambiamos ni transferimos tu información personal a terceros.")
            PolicyBody("Solo compartimos información con servicios esenciales (Firebase), por requerimientos legales, o mediante datos anónimos agregados.")
        }

        // --- 5. SERVICIOS DE TERCEROS ---
        item {
            PolicyTitle("5. Servicios de Terceros")
            PolicySubTitle("5.1 Firebase (Google)")
            PolicyBody("Usamos Firebase para autenticación, almacenamiento y análisis. Política de privacidad: firebase.google.com/support/privacy")

            PolicySubTitle("5.2 Jikan API")
            PolicyBody("Obtiene información pública de MyAnimeList sobre animes y mangas.")
        }

        // --- 6 AL 9: RETENCIÓN, DERECHOS Y MENORES ---
        item {
            PolicyTitle("6. Retención de Datos")
            PolicyBody("Mantenemos tu información mientras tu cuenta esté activa. Al eliminar tu cuenta, tus datos personales se borrarán de nuestros servidores.")

            PolicyTitle("7. Tus Derechos")
            PolicyBody("Tienes derecho a acceder, corregir, eliminar o exportar tus datos personales en cualquier momento.")

            PolicyTitle("8. Privacidad de Menores")
            PolicyBody("Seijaku List no está dirigida a menores de 13 años. Si descubrimos datos de un menor sin consentimiento parental, los eliminaremos de inmediato.")

            PolicyTitle("9. Cambios a esta Política")
            PolicyBody("Podemos actualizar esta política ocasionalmente. Te notificaremos mediante la sección de novedades en la app.")
        }

        // --- 10 AL 11: COOKIES Y TRANSFERENCIAS ---
        item {
            PolicyTitle("10. Cookies y Tecnologías")
            PolicyBody("Usamos almacenamiento local para preferencias y sesiones. No rastreamos tu actividad fuera de la app.")

            PolicyTitle("11. Transferencias Internacionales")
            PolicyBody("Tus datos pueden almacenarse en servidores de Google Cloud ubicados fuera de tu país, cumpliendo con las leyes de protección de datos aplicables.")
        }

        // --- 12. CONTACTO ---
        item {
            PolicyTitle("12. Contacto")
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Si tienes preguntas sobre esta política, contáctanos:",
                        fontFamily = PoppinsRegular,
                        fontSize = 14.asp(),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "seijakulist@gmail.com",
                        fontFamily = PoppinsBold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.asp(),
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:seijakulist@gmail.com")
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }

        // --- 13. CONSENTIMIENTO ---
        item {
            PolicyTitle("13. Consentimiento")
            PolicyBody("Al usar Seijaku List, aceptas esta Política de Privacidad y sus términos.")

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "© 2026 Seijaku List · Versión $versionName",
                fontSize = 12.asp(),
                fontFamily = PoppinsRegular,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.adp()))
        }
    }
}

@Composable
fun PolicyTitle(text: String) {
    Column(modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)) {
        Text(
            text = text,
            fontSize = 20.asp(),
            fontFamily = PoppinsBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(6.dp))
        HorizontalDivider(
            thickness = 3.dp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(40.dp)
        )
    }
}

@Composable
fun PolicySubTitle(text: String) {
    Text(
        text = text,
        fontSize = 16.asp(),
        fontFamily = PoppinsBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun PolicyBody(text: String) {
    Text(
        text = text,
        fontSize = 14.asp(),
        fontFamily = PoppinsRegular,
        lineHeight = 22.asp(),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun PolicyBulletPoint(items: List<String>) {
    Column(
        modifier = Modifier.padding(start = 16.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach { item ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "•",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.asp(),
                    fontFamily = PoppinsBold
                )
                Text(
                    text = item,
                    fontSize = 14.asp(),
                    fontFamily = PoppinsRegular,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun PolicyHighlight(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
    ) {
        Text(
            text = text,
            fontSize = 14.asp(),
            fontFamily = PoppinsMedium,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 22.asp(),
            modifier = Modifier.padding(16.dp)
        )
    }
}