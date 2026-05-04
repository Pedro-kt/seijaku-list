package com.yumedev.seijakulist.ui.screens.policy_privacy

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

@Composable
fun PolicyPrivacyScreen(navController: NavController) {
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName ?: "1.0.0" // Esta es la versión de tu Gradle
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        // --- HEADER & INTRO ---
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Seijaku List - Tu compañero de anime y manga",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = PoppinsMedium
            )
            Text(
                text = "Fecha de última actualización: 22 de Febrero de 2026",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontFamily = PoppinsRegular
            )

            PolicyHighlight(
                "Seijaku List (\"nosotros\", \"nuestro\" o \"la aplicación\") respeta la privacidad de nuestros usuarios. " +
                        "Al usar Seijaku List, usted acepta la recopilación y el uso de información de acuerdo con esta política."
            )
        }

        // --- 1. INFORMACIÓN QUE RECOPILAMOS ---
        item {
            PolicyTitle("1. Información que Recopilamos")

            PolicySubTitle("1.1 Información de Registro")
            PolicyBody("Cuando crea una cuenta en Seijaku List, recopilamos:")
            PolicyBulletPoint(listOf(
                "Dirección de correo electrónico",
                "Nombre de usuario",
                "Contraseña (almacenada de forma cifrada)",
                "Información de perfil (nombre, foto de perfil, biografía)"
            ))

            PolicySubTitle("1.2 Información de Uso")
            PolicyBulletPoint(listOf(
                "Lista de animes y mangas que agrega a su colección",
                "Estados de visualización (viendo, completado, pendiente, etc.)",
                "Episodios vistos y progreso de visualización",
                "Calificaciones y reseñas personales",
                "Logros desbloqueados",
                "Preferencias y configuraciones de la aplicación"
            ))

            PolicySubTitle("1.3 Información Técnica")
            PolicyBulletPoint(listOf(
                "Tipo de dispositivo y modelo",
                "Versión del sistema operativo",
                "Identificadores únicos de dispositivo",
                "Datos de registro de errores y fallos (crashlytics)"
            ))

            PolicySubTitle("1.4 Información de Terceros")
            PolicyBody("Utilizamos la API de Jikan (MyAnimeList) para obtener información sobre animes, mangas, personajes y estudios. Esta información es pública y no incluye datos personales.")
        }

        // --- 2. CÓMO USAMOS SU INFORMACIÓN ---
        item {
            PolicyTitle("2. Cómo Usamos su Información")
            PolicyBulletPoint(listOf(
                "Proporcionar y mantener la funcionalidad de la aplicación",
                "Crear y gestionar su cuenta de usuario",
                "Sincronizar sus datos entre dispositivos",
                "Personalizar su experiencia y mejorar la aplicación",
                "Detectar y solucionar problemas técnicos",
                "Generar estadísticas de uso anónimas"
            ))
        }

        // --- 3. ALMACENAMIENTO Y PROTECCIÓN ---
        item {
            PolicyTitle("3. Almacenamiento y Protección de Datos")
            PolicySubTitle("3.1 Almacenamiento Local")
            PolicyBody("Sus datos se almacenan mediante una base de datos Room (SQLite), protegida por las medidas de seguridad de Android.")

            PolicySubTitle("3.2 Almacenamiento en la Nube")
            PolicyBody("Utilizamos Firebase (Google Cloud Platform) para almacenar sus datos. Todos los datos están cifrados en tránsito y en reposo.")

            PolicySubTitle("3.3 Seguridad")
            PolicyBody("Implementamos cifrado HTTPS/SSL y autenticación segura. Sin embargo, ningún método de transmisión por internet es 100% seguro.")
        }

        // --- 4. COMPARTIR INFORMACIÓN ---
        item {
            PolicyTitle("4. Compartir Información")
            PolicyHighlight("NO vendemos, intercambiamos ni transferimos su información personal a terceros.")
            PolicyBody("Podemos compartir información solo con proveedores esenciales (Firebase), por requisitos legales o mediante datos agregados anónimos.")
        }

        // --- 5. SERVICIOS DE TERCEROS ---
        item {
            PolicyTitle("5. Servicios de Terceros")
            PolicySubTitle("5.1 Firebase (Google)")
            PolicyBody("Utilizamos Firebase para autenticación y análisis. Puede revisar su política en: https://firebase.google.com/support/privacy")

            PolicySubTitle("5.2 Jikan API")
            PolicyBody("Utilizada para obtener información pública de MyAnimeList.")
        }

        // --- 6 AL 9: RETENCIÓN, DERECHOS Y MENORES ---
        item {
            PolicyTitle("6. Retención de Datos")
            PolicyBody("Mantenemos su información mientras su cuenta esté activa. Si elimina su cuenta, sus datos personales serán eliminados de nuestros servidores.")

            PolicyTitle("7. Sus Derechos")
            PolicyBody("Usted tiene derecho a acceder, corregir, eliminar o exportar sus datos personales.")

            PolicyTitle("8. Privacidad de Menores")
            PolicyBody("Seijaku List no está dirigida a menores de 13 años. Si descubrimos datos de un menor sin consentimiento, los eliminaremos inmediatamente.")

            PolicyTitle("9. Cambios a esta Política")
            PolicyBody("Podemos actualizar esta política periódicamente. Le notificaremos publicando los cambios en esta sección y en novedades.")
        }

        // --- 10 AL 11: COOKIES Y TRANSFERENCIAS ---
        item {
            PolicyTitle("10. Cookies y Tecnologías")
            PolicyBody("Usamos almacenamiento local para preferencias y tokens de sesión. No rastreamos su actividad fuera de la aplicación.")

            PolicyTitle("11. Transferencias Internacionales")
            PolicyBody("Sus datos pueden almacenarse en servidores de Google Cloud ubicados fuera de su país, cumpliendo con leyes de protección de datos.")
        }

        // --- 12. CONTACTO ---
        item {
            PolicyTitle("12. Contacto")
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    PolicyBody("Si tiene preguntas, contáctenos en:")
                    val context = LocalContext.current
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
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // --- 13. CONSENTIMIENTO ---
        item {
            PolicyTitle("13. Consentimiento")
            PolicyBody("Al usar Seijaku List, usted consiente nuestra Política de Privacidad y acepta sus términos.")

            Spacer(modifier = Modifier.height(40.adp()))
            Text(
                text = "© 2026 Seijaku List. Versión: $versionName",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.adp()))
        }
    }
    }

@Composable
fun PolicyTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontFamily = PoppinsBold,
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
    )
    HorizontalDivider(
        thickness = 3.dp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.width(40.dp).padding(bottom = 16.dp)
    )
}

@Composable
fun PolicySubTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge.copy(
            fontFamily = PoppinsBold,
            color = MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun PolicyBody(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontFamily = PoppinsRegular,
            lineHeight = 22.asp(),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.padding(bottom = 12.dp),
        textAlign = TextAlign.Justify
    )
}

@Composable
fun PolicyBulletPoint(items: List<String>) {
    Column(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)) {
        items.forEach { item ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(text = "•", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 8.dp))
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = PoppinsRegular),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun PolicyHighlight(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = PoppinsMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }
}