package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.ExpenseRed

@Composable
fun DeleteConfirmDialog(
  language: AppLanguage,
  onConfirm: () -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Icon(
        imageVector = Icons.Default.Warning,
        contentDescription = null,
        tint = ExpenseRed
      )
    },
    title = {
      Text(text = AppStrings.confirmDeleteTitle(language))
    },
    text = {
      Text(text = AppStrings.confirmDeleteMessage(language))
    },
    confirmButton = {
      Button(
        onClick = onConfirm,
        colors = ButtonDefaults.buttonColors(containerColor = ExpenseRed),
        modifier = Modifier.testTag("confirm_delete_button")
      ) {
        Text(text = AppStrings.delete(language))
      }
    },
    dismissButton = {
      OutlinedButton(
        onClick = onDismiss,
        modifier = Modifier.testTag("cancel_delete_button")
      ) {
        Text(text = AppStrings.cancel(language))
      }
    }
  )
}
