package com.yumedev.seijakulist.ui.components.confirm_dialog

import androidx.compose.runtime.Composable
import com.yumedev.seijakulist.ui.components.CustomDialog
import com.yumedev.seijakulist.ui.components.DialogType

@Composable
fun ConfirmChangePlannedDialog(
    newStatus: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    CustomDialog(
        onDismissRequest = onDismiss,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        title = "Cambiar estado",
        message = "Al cambiar el estado a \"$newStatus\" se eliminarán la prioridad y la nota de plan que tenías guardadas.\n\n¿Querés continuar?",
        confirmButtonText = "Sí, cambiar",
        dismissButtonText = "Cancelar",
        type = DialogType.WARNING
    )
}