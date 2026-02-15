package com.yumedev.seijakulist.ui.screens.report

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.ui.res.painterResource
import com.yumedev.seijakulist.R
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yumedev.seijakulist.ui.theme.PoppinsBold
import com.yumedev.seijakulist.ui.theme.PoppinsRegular
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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

    val placeholder = """Describe el error que encontraste:

    Pasos para reproducir:
    1.
    2.
    3.
    
    Comportamiento esperado:
    
    Comportamiento actual:
    
    Información adicional:
    """

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
                .height(56.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left_line),
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Reportar Error",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                fontFamily = PoppinsBold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Contenido
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Descripción
            Text(
                text = "Completa los campos para reportar un error. Al enviar se abrirá tu aplicación de email.\n\nReportar los errores que encuentres nos ayuda a corregirlos y mejorar nuestra aplicación y la experiencia de usuario, gracias por tu colaboracion!",
                fontFamily = PoppinsRegular,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Email
            Text(
                text = "Tu Email",
                fontFamily = PoppinsBold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = emailText,
                onValueChange = { emailText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "tu@email.com",
                        fontFamily = PoppinsRegular
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = PoppinsRegular
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Reporte
            Text(
                text = "Descripción del Error",
                fontFamily = PoppinsBold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = reportText,
                onValueChange = { reportText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                placeholder = {
                    Text(
                        placeholder,
                        fontFamily = PoppinsRegular,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = PoppinsRegular
                ),
                maxLines = 15
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Enviar
            Button(
                onClick = {
                    val emailBody = reportText.ifEmpty { placeholder } +
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
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                enabled = reportText.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Enviar",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Enviar Reporte",
                    fontFamily = PoppinsBold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
