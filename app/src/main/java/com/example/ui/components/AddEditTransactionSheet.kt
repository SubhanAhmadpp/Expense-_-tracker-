package com.example.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.TransactionEntity
import com.example.data.model.CategoryItem
import com.example.data.model.CategoryPresets
import com.example.data.model.TransactionType
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.IncomeGreen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditTransactionDialog(
  initialType: TransactionType = TransactionType.EXPENSE,
  editingItem: TransactionEntity? = null,
  currency: String,
  language: AppLanguage,
  onDismiss: () -> Unit,
  onSave: (
    id: Long,
    title: String,
    amount: Double,
    type: TransactionType,
    categoryId: String,
    dateMillis: Long,
    paymentMethod: String,
    note: String
  ) -> Unit
) {
  val context = LocalContext.current

  var selectedType by remember {
    mutableStateOf(editingItem?.transactionType ?: initialType)
  }

  var amountText by remember {
    mutableStateOf(editingItem?.amount?.let { if (it % 1 == 0.0) it.toLong().toString() else it.toString() } ?: "")
  }

  var titleText by remember {
    mutableStateOf(editingItem?.title ?: "")
  }

  val categories = if (selectedType == TransactionType.EXPENSE) {
    CategoryPresets.expenseCategories
  } else {
    CategoryPresets.incomeCategories
  }

  var selectedCategory by remember(selectedType) {
    mutableStateOf(
      editingItem?.let { item ->
        categories.find { it.id == item.categoryId }
      } ?: categories.first()
    )
  }

  var selectedDateMillis by remember {
    mutableLongStateOf(editingItem?.dateMillis ?: System.currentTimeMillis())
  }

  val paymentMethods = listOf("Cash", "Bank", "Card", "Easypaisa", "JazzCash")
  var selectedPaymentMethod by remember {
    mutableStateOf(editingItem?.paymentMethod ?: "Cash")
  }

  var noteText by remember {
    mutableStateOf(editingItem?.note ?: "")
  }

  var amountError by remember { mutableStateOf<String?>(null) }
  var titleError by remember { mutableStateOf<String?>(null) }

  val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .heightIn(max = 680.dp)
        .padding(vertical = 16.dp)
        .testTag("add_edit_transaction_dialog"),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (editingItem != null) {
              AppStrings.editTransaction(language)
            } else {
              if (selectedType == TransactionType.EXPENSE) AppStrings.addExpense(language)
              else AppStrings.addIncome(language)
            },
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
          )

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_dialog_button")
          ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Type Switcher (Expense / Income)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
        ) {
          // Expense Tab
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (selectedType == TransactionType.EXPENSE) ExpenseRed else Color.Transparent)
              .clickable {
                selectedType = TransactionType.EXPENSE
                selectedCategory = CategoryPresets.expenseCategories.first()
              }
              .padding(vertical = 10.dp)
              .testTag("select_type_expense"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = AppStrings.addExpense(language),
              color = if (selectedType == TransactionType.EXPENSE) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }

          // Income Tab
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (selectedType == TransactionType.INCOME) IncomeGreen else Color.Transparent)
              .clickable {
                selectedType = TransactionType.INCOME
                selectedCategory = CategoryPresets.incomeCategories.first()
              }
              .padding(vertical = 10.dp)
              .testTag("select_type_income"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = AppStrings.addIncome(language),
              color = if (selectedType == TransactionType.INCOME) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Amount Field
        OutlinedTextField(
          value = amountText,
          onValueChange = {
            amountText = it
            amountError = null
          },
          label = { Text(AppStrings.amount(language)) },
          prefix = {
            Text(
              text = "$currency ",
              fontWeight = FontWeight.Bold,
              color = if (selectedType == TransactionType.EXPENSE) ExpenseRed else IncomeGreen
            )
          },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
          isError = amountError != null,
          supportingText = amountError?.let { { Text(it, color = ExpenseRed) } },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("transaction_amount_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Title Field
        OutlinedTextField(
          value = titleText,
          onValueChange = {
            titleText = it
            titleError = null
          },
          label = { Text(AppStrings.title(language)) },
          isError = titleError != null,
          supportingText = titleError?.let { { Text(it, color = ExpenseRed) } },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("transaction_title_input")
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category Picker Section
        Text(
          text = AppStrings.category(language),
          fontWeight = FontWeight.SemiBold,
          fontSize = 14.sp,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          categories.forEach { cat ->
            val isSelected = selectedCategory.id == cat.id
            Row(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(
                  if (isSelected) cat.color.copy(alpha = 0.2f)
                  else MaterialTheme.colorScheme.surfaceVariant
                )
                .border(
                  width = if (isSelected) 1.5.dp else 0.dp,
                  color = if (isSelected) cat.color else Color.Transparent,
                  shape = RoundedCornerShape(10.dp)
                )
                .clickable { selectedCategory = cat }
                .padding(horizontal = 10.dp, vertical = 7.dp)
                .testTag("category_chip_${cat.id}"),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = CategoryIconHelper.getIcon(cat.iconKey),
                contentDescription = null,
                tint = if (isSelected) cat.color else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = cat.getName(language),
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) cat.color else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Payment Method Chips
        Text(
          text = AppStrings.paymentMethod(language),
          fontWeight = FontWeight.SemiBold,
          fontSize = 14.sp,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          paymentMethods.forEach { method ->
            val isSelected = selectedPaymentMethod == method
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(
                  if (isSelected) MaterialTheme.colorScheme.primaryContainer
                  else MaterialTheme.colorScheme.surfaceVariant
                )
                .border(
                  width = if (isSelected) 1.5.dp else 0.dp,
                  color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                  shape = RoundedCornerShape(10.dp)
                )
                .clickable { selectedPaymentMethod = method }
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("payment_chip_$method")
            ) {
              Text(
                text = when (method) {
                  "Cash" -> AppStrings.cash(language)
                  "Bank" -> AppStrings.bankTransfer(language)
                  "Card" -> AppStrings.card(language)
                  "Easypaisa" -> AppStrings.easypaisa(language)
                  "JazzCash" -> AppStrings.jazzCash(language)
                  else -> method
                },
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Date Picker Button
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
              val cal = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }
              DatePickerDialog(
                context,
                { _, year, month, day ->
                  val picked = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day)
                  }
                  selectedDateMillis = picked.timeInMillis
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
              ).show()
            }
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .testTag("date_picker_button"),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.CalendarToday,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = AppStrings.date(language),
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Text(
            text = dateFormat.format(Date(selectedDateMillis)),
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Optional Note Field
        OutlinedTextField(
          value = noteText,
          onValueChange = { noteText = it },
          label = { Text(AppStrings.note(language)) },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("transaction_note_input")
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Dialog Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("cancel_transaction_button"),
            shape = RoundedCornerShape(14.dp)
          ) {
            Text(text = AppStrings.cancel(language))
          }

          Button(
            onClick = {
              val amt = amountText.toDoubleOrNull()
              var valid = true
              if (amt == null || amt <= 0) {
                amountError = AppStrings.amountError(language)
                valid = false
              }
              if (titleText.trim().isEmpty()) {
                titleError = AppStrings.titleError(language)
                valid = false
              }

              if (valid && amt != null) {
                onSave(
                  editingItem?.id ?: 0L,
                  titleText.trim(),
                  amt,
                  selectedType,
                  selectedCategory.id,
                  selectedDateMillis,
                  selectedPaymentMethod,
                  noteText.trim()
                )
                onDismiss()
              }
            },
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("save_transaction_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = if (selectedType == TransactionType.EXPENSE) ExpenseRed else IncomeGreen
            )
          ) {
            Text(
              text = AppStrings.save(language),
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }
    }
  }
}
