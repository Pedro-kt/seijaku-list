package com.yumedev.seijakulist.ui.components.confirm_dialog

import androidx.compose.runtime.Composable
import com.yumedev.seijakulist.ui.components.CustomDialog
import com.yumedev.seijakulist.ui.components.DialogType

/**
 * Diálogo simple y genérico para confirmar cambio de estado
 */
@Composable
fun SimpleStatusChangeDialog(
    newStatus: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CustomDialog(
        onDismissRequest = onDismiss,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        title = "Cambiar estado",
        message = "¿Cambiar este anime a $newStatus?",
        confirmButtonText = "Sí",
        dismissButtonText = "No",
        type = DialogType.INFO
    )
}
