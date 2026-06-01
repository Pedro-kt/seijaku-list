package com.yumedev.seijakulist.ui.screens.report

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yumedev.seijakulist.R
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsMedium
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.yumedev.seijakulist.ui.theme.adp
import com.yumedev.seijakulist.ui.theme.asp

@Composable
fun ReportErrorScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val userEmail = remember { Firebase.auth.currentUser?.email ?: "" }

    var emailText by remember { mutableStateOf(userEmail) }
    var reportText by remember { mutableStateOf("") }
    val localFocusManager = LocalFocusManager.current


    // Obtener versión
    val versionInfo = remember {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            "v${packageInfo.versionName}"
        } catch (e: Exception) {
            "v1.0"
        }
    }

    val placeholder = """Describe el error que encontraste.

Pasos para reproducir:
1.
2.
3.

¿Qué esperabas que pasara?


¿Qué pasó en realidad?


Información adicional (opcional):"""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTapGestures(){
                    localFocusManager.clearFocus()
                }
            }
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.adp())
                .background(MaterialTheme.colorScheme.background)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left_line),
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.adp())
                )
            }
            Text(
                text = "Reportar Error",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 22.asp(),
                fontFamily = PoppinsBold,
                letterSpacing = 0.3.sp,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 56.dp)
            )
        }

        // Contenido
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Descripción en card
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Ayúdanos a mejorar",
                        fontFamily = PoppinsBold,
                        fontSize = 16.asp(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Completa los campos y enviaremos un correo con los detalles. Tu feedback es muy valioso para mejorar la app, ¡gracias por tu colaboración!",
                        fontFamily = PoppinsRegular,
                        fontSize = 13.asp(),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 19.asp()
                    )
                }
            }

            // Campo de Email
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Tu Email",
                    fontFamily = PoppinsMedium,
                    fontSize = 14.asp(),
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = emailText,
                    onValueChange = { emailText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "tu@email.com",
                            fontFamily = PoppinsRegular,
                            fontSize = 14.asp()
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.adp())
                        )
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = PoppinsRegular,
                        fontSize = 14.asp()
                    ),
                    singleLine = true
                )
            }

            // Campo de Reporte
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Descripción del Error",
                    fontFamily = PoppinsMedium,
                    fontSize = 14.asp(),
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = reportText,
                    onValueChange = { reportText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.adp()),
                    placeholder = {
                        Text(
                            placeholder,
                            fontFamily = PoppinsRegular,
                            fontSize = 13.asp(),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            lineHeight = 18.asp()
                        )
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = if (reportText.isEmpty())
                            MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
                        else
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = PoppinsRegular,
                        fontSize = 14.asp(),
                        lineHeight = 20.asp()
                    ),
                    maxLines = 18
                )
            }

            // Texto de ayuda cuando está vacío
            if (reportText.isEmpty()) {
                Text(
                    text = "Por favor describe el error antes de enviar",
                    fontFamily = PoppinsRegular,
                    fontSize = 12.asp(),
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de Enviar
            Button(
                onClick = {
                    val emailBody = reportText +
                            "\n\n---\nVersión: $versionInfo\nUsuario: $userEmail"

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "message/rfc822"
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("seijakulist@gmail.com"))
                        putExtra(Intent.EXTRA_SUBJECT, "Reporte de error - SeijakuList")
                        putExtra(Intent.EXTRA_TEXT, emailBody)
                        if (emailText.isNotEmpty()) {
                            putExtra(Intent.EXTRA_CC, arrayOf(emailText))
                        }
                        // Especificar Gmail directamente
                        setPackage("com.google.android.gm")
                    }

                    try {
                        // Intentar abrir Gmail directamente
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Si Gmail no está instalado, intentar con el selector
                        val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "message/rfc822"
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("seijakulist@gmail.com"))
                            putExtra(Intent.EXTRA_SUBJECT, "Reporte de error - SeijakuList")
                            putExtra(Intent.EXTRA_TEXT, emailBody)
                            if (emailText.isNotEmpty()) {
                                putExtra(Intent.EXTRA_CC, arrayOf(emailText))
                            }
                        }
                        try {
                            context.startActivity(Intent.createChooser(fallbackIntent, "Enviar email"))
                        } catch (e: Exception) {
                            // No hay apps de email
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.adp()),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                enabled = reportText.isNotEmpty()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar",
                        modifier = Modifier.size(20.adp())
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "Enviar Reporte",
                        fontFamily = PoppinsBold,
                        fontSize = 16.asp()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
