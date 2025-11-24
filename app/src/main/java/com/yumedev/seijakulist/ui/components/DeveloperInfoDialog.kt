package com.yumedev.seijakulist.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yumedev.seijakulist.ui.theme.RobotoBold
import com.yumedev.seijakulist.ui.theme.RobotoRegular

@Composable
fun DeveloperInfoDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header con botón de cerrar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(40.dp))

                    Text(
                        text = "Desarrollador",
                        fontFamily = RobotoBold,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                // Icono
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = "Acerca de",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )

                // Nombre
                Text(
                    text = "Bustamante Pedro",
                    fontFamily = RobotoBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                // Descripción
                Text(
                    text = "Hola, soy el Desarrollador de Seijaku List, una aplicacion de registro personal de animes y mangas ( en desarrollo activo ), actualmente me desenvuelvo profesionalmente como Desarrollador Android, me gusta el anime, la musica y la programacion.\n\nPD: Mi anime favorito es K-on!",
                    fontFamily = RobotoRegular,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Justify,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Título de redes sociales
                Text(
                    text = "Redes Sociales",
                    fontFamily = RobotoBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                // Enlaces a redes sociales
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // GitHub
                    SocialMediaLink(
                        icon = Icons.Default.Code,
                        label = "GitHub",
                        url = "https://github.com/Pedro-kt",
                        onClick = { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    )

                    // Email
                    SocialMediaLink(
                        icon = Icons.Default.Email,
                        label = "Email",
                        url = "mailto:bustamante.dev.mobile@gmail.com",
                        onClick = { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    )

                    SocialMediaLink(
                        icon = Icons.Default.Web,
                        label = "Portfolio",
                        url = "https://my-portfolio-azze.vercel.app/",
                        onClick = { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    )
                    // Puedes agregar más redes sociales aquí
                    // LinkedIn, Twitter/X, etc.
                }
            }
        }
    }
}

@Composable
private fun SocialMediaLink(
    icon: ImageVector,
    label: String,
    url: String,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(url) }
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = label,
            fontFamily = RobotoRegular,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
