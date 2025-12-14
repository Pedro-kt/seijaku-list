package com.yumedev.seijakulist.ui.components

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yumedev.seijakulist.ui.theme.RobotoBold
import com.yumedev.seijakulist.ui.theme.RobotoRegular

/**
 * Tipos de diálogo predefinidos con iconos y colores correspondientes
 */
enum class DialogType {
    SUCCESS,    // Éxito - Icono de check verde
    ERROR,      // Error - Icono de error rojo
    WARNING,    // Advertencia - Icono de advertencia amarillo
    INFO,       // Información - Icono de info azul
    DELETE,     // Confirmación de eliminación - Icono de eliminar rojo
    CUSTOM      // Personalizado - requiere pasar un icono
}

/**
 * Diálogo personalizado reutilizable de SeijakuList
 *
 * @param onDismissRequest Acción al cerrar el diálogo
 * @param onConfirm Acción al confirmar (botón principal)
 * @param onDismiss Acción al cancelar (botón secundario), null si no hay botón de cancelar
 * @param title Título del diálogo
 * @param message Mensaje del diálogo
 * @param confirmButtonText Texto del botón de confirmar (por defecto "Aceptar")
 * @param dismissButtonText Texto del botón de cancelar (por defecto "Cancelar")
 * @param type Tipo de diálogo (SUCCESS, ERROR, WARNING, INFO, DELETE, CUSTOM)
 * @param customIcon Icono personalizado cuando type es CUSTOM
 * @param dismissOnBackPress Permite cerrar con el botón atrás (por defecto true)
 * @param dismissOnClickOutside Permite cerrar tocando fuera del diálogo (por defecto true)
 */
@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: (() -> Unit)? = null,
    title: String,
    message: String,
    confirmButtonText: String = "Aceptar",
    dismissButtonText: String = "Cancelar",
    type: DialogType = DialogType.INFO,
    customIcon: ImageVector? = null,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true
) {
    // Determinar el icono según el tipo
    val icon = when (type) {
        DialogType.SUCCESS -> Icons.Default.CheckCircle
        DialogType.ERROR -> Icons.Default.Error
        DialogType.WARNING -> Icons.Default.Warning
        DialogType.INFO -> Icons.Default.Info
        DialogType.DELETE -> Icons.Default.Delete
        DialogType.CUSTOM -> customIcon ?: Icons.Default.Info
    }

    // Determinar el color del icono según el tipo
    val iconTint = when (type) {
        DialogType.SUCCESS -> MaterialTheme.colorScheme.primary
        DialogType.ERROR -> MaterialTheme.colorScheme.error
        DialogType.WARNING -> MaterialTheme.colorScheme.tertiary
        DialogType.INFO -> MaterialTheme.colorScheme.primary
        DialogType.DELETE -> MaterialTheme.colorScheme.error
        DialogType.CUSTOM -> MaterialTheme.colorScheme.primary
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside
        )
    ) {
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
                // Icono
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(64.dp)
                )

                // Título
                Text(
                    text = title,
                    fontFamily = RobotoBold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                // Mensaje
                Text(
                    text = message,
                    fontFamily = RobotoRegular,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    lineHeight = 21.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón de cancelar (si existe)
                    if (onDismiss != null) {
                        OutlinedButton(
                            onClick = {
                                onDismiss()
                                onDismissRequest()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(
                                text = dismissButtonText,
                                fontFamily = RobotoBold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    // Botón de confirmar
                    Button(
                        onClick = {
                            onConfirm()
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == DialogType.ERROR || type == DialogType.DELETE) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            contentColor = if (type == DialogType.ERROR || type == DialogType.DELETE) {
                                MaterialTheme.colorScheme.onError
                            } else {
                                MaterialTheme.colorScheme.onPrimary
                            }
                        )
                    ) {
                        Text(
                            text = confirmButtonText,
                            fontFamily = RobotoBold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
