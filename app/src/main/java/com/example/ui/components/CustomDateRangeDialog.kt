package com.example.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CustomDateRangeDialog(
  initialStart: Long? = null,
  initialEnd: Long? = null,
  language: AppLanguage,
  onDismiss: () -> Unit,
  onApply: (startMillis: Long, endMillis: Long) -> Unit
) {
  val context = LocalContext.current
  val cal = Calendar.getInstance()

  var startMillis by remember {
    mutableLongStateOf(initialStart ?: (System.currentTimeMillis() - 7L * 24 * 3600 * 1000))
  }
  var endMillis by remember {
    mutableLongStateOf(initialEnd ?: System.currentTimeMillis())
  }

  val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .testTag("custom_date_range_dialog"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = if (language == AppLanguage.URDU) "مخصوص تاریخ کا انتخاب" else "Custom Date Range",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Start Date Selector
        Text(
          text = if (language == AppLanguage.URDU) "شروع کی تاریخ" else "Start Date",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
              val c = Calendar.getInstance().apply { timeInMillis = startMillis }
              DatePickerDialog(
                context,
                { _, y, m, d ->
                  val picked = Calendar.getInstance().apply {
                    set(Calendar.YEAR, y)
                    set(Calendar.MONTH, m)
                    set(Calendar.DAY_OF_MONTH, d)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                  }
                  startMillis = picked.timeInMillis
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
              ).show()
            }
            .padding(14.dp)
            .testTag("pick_start_date_btn"),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = dateFormat.format(Date(startMillis)),
            fontWeight = FontWeight.SemiBold
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // End Date Selector
        Text(
          text = if (language == AppLanguage.URDU) "ختم کی تاریخ" else "End Date",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
              val c = Calendar.getInstance().apply { timeInMillis = endMillis }
              DatePickerDialog(
                context,
                { _, y, m, d ->
                  val picked = Calendar.getInstance().apply {
                    set(Calendar.YEAR, y)
                    set(Calendar.MONTH, m)
                    set(Calendar.DAY_OF_MONTH, d)
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                  }
                  endMillis = picked.timeInMillis
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
              ).show()
            }
            .padding(14.dp)
            .testTag("pick_end_date_btn"),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = dateFormat.format(Date(endMillis)),
            fontWeight = FontWeight.SemiBold
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f)
          ) {
            Text(AppStrings.cancel(language))
          }

          Button(
            onClick = {
              val effectiveStart = minOf(startMillis, endMillis)
              val effectiveEnd = maxOf(startMillis, endMillis)
              onApply(effectiveStart, effectiveEnd)
              onDismiss()
            },
            modifier = Modifier
              .weight(1f)
              .testTag("apply_custom_date_filter_btn")
          ) {
            Text(if (language == AppLanguage.URDU) "لاگو کریں" else "Apply")
          }
        }
      }
    }
  }
}
