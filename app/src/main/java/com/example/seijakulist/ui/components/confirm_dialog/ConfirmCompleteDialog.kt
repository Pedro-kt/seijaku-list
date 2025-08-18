package com.example.seijakulist.ui.components.confirm_dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmCompleteDialog(
    animeTitle: String,
    watched: Int,
    total: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Marcar como completado") },
        text = {
            Text("Actualmente llevas $watched de $total episodios vistos.\n" +
                    "¿Quieres marcar \"$animeTitle\" como Completado?\n" +
                    "Esto ajustará los episodios vistos a $total.")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Sí, completar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}