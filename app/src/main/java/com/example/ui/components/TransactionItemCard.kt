package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TransactionEntity
import com.example.data.model.CategoryPresets
import com.example.data.model.TransactionType
import com.example.ui.i18n.AppLanguage
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.IncomeGreen
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionItemCard(
  transaction: TransactionEntity,
  currency: String,
  language: AppLanguage,
  onEdit: (TransactionEntity) -> Unit,
  onDelete: (TransactionEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  val numberFormat = NumberFormat.getNumberInstance(Locale.US)
  val category = CategoryPresets.findCategory(transaction.categoryId)
  val isIncome = transaction.transactionType == TransactionType.INCOME

  val dateFormat = SimpleDateFormat("MMM d, yyyy • hh:mm a", Locale.getDefault())
  val formattedDate = dateFormat.format(Date(transaction.dateMillis))

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("transaction_item_${transaction.id}")
      .clickable { onEdit(transaction) },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Category Icon with tinted circle
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(CircleShape)
          .background(category.color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = CategoryIconHelper.getIcon(category.iconKey),
          contentDescription = category.getName(language),
          tint = category.color,
          modifier = Modifier.size(24.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Title & Subtitle Info
      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = transaction.title,
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          // Category label
          Text(
            text = category.getName(language),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
          )

          Text(
            text = "•",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
          )

          // Payment method badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = transaction.paymentMethod,
              fontSize = 10.sp,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Date
        Text(
          text = formattedDate,
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Amount & Action buttons
      Column(
        horizontalAlignment = Alignment.End
      ) {
        Text(
          text = if (isIncome) "+$currency ${numberFormat.format(transaction.amount)}"
                 else "-$currency ${numberFormat.format(transaction.amount)}",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = if (isIncome) IncomeGreen else ExpenseRed
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          horizontalArrangement = Arrangement.spacedBy(2.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = { onEdit(transaction) },
            modifier = Modifier
              .size(32.dp)
              .testTag("edit_transaction_btn_${transaction.id}")
          ) {
            Icon(
              imageVector = Icons.Default.Edit,
              contentDescription = "Edit",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(16.dp)
            )
          }

          IconButton(
            onClick = { onDelete(transaction) },
            modifier = Modifier
              .size(32.dp)
              .testTag("delete_transaction_btn_${transaction.id}")
          ) {
            Icon(
              imageVector = Icons.Default.DeleteOutline,
              contentDescription = "Delete",
              tint = ExpenseRed,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}
