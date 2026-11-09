package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TransactionEntity
import com.example.ui.components.BalanceCard
import com.example.ui.components.CategoryBreakdownCard
import com.example.ui.components.DateFilterBar
import com.example.ui.components.QuickActionButtons
import com.example.ui.components.StatCardsRow
import com.example.ui.components.TransactionItemCard
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.viewmodel.CategorySummary
import com.example.ui.viewmodel.DateFilterType

@Composable
fun DashboardScreen(
  balance: Double,
  income: Double,
  expense: Double,
  currency: String,
  language: AppLanguage,
  dateFilter: DateFilterType,
  categorySummaries: List<CategorySummary>,
  recentTransactions: List<TransactionEntity>,
  onDateFilterChange: (DateFilterType) -> Unit,
  onOpenCustomRangeDialog: () -> Unit,
  onAddExpense: () -> Unit,
  onAddIncome: () -> Unit,
  onEditTransaction: (TransactionEntity) -> Unit,
  onDeleteTransaction: (TransactionEntity) -> Unit,
  onSeeAllTransactions: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("dashboard_screen"),
    contentPadding = androidx.compose.foundation.layout.PaddingValues(
      start = 16.dp,
      end = 16.dp,
      top = 8.dp,
      bottom = 96.dp
    ),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Balance Card
    item {
      BalanceCard(
        balance = balance,
        income = income,
        expense = expense,
        currency = currency,
        language = language
      )
    }

    // Quick Actions
    item {
      QuickActionButtons(
        onAddExpense = onAddExpense,
        onAddIncome = onAddIncome,
        language = language
      )
    }

    // Date Filter Chips
    item {
      Column {
        Text(
          text = if (language == AppLanguage.URDU) "فلٹر کی مدت" else "Filter Period",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(bottom = 6.dp)
        )
        DateFilterBar(
          selectedFilter = dateFilter,
          onSelectFilter = onDateFilterChange,
          onOpenCustomRangeDialog = onOpenCustomRangeDialog,
          language = language
        )
      }
    }

    // Income & Expense Split Stat Cards
    item {
      StatCardsRow(
        income = income,
        expense = expense,
        currency = currency,
        language = language
      )
    }

    // Spending by Category
    item {
      CategoryBreakdownCard(
        categorySummaries = categorySummaries,
        currency = currency,
        language = language
      )
    }

    // Recent Transactions Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = AppStrings.recentTransactions(language),
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
          color = MaterialTheme.colorScheme.onSurface
        )

        TextButton(
          onClick = onSeeAllTransactions,
          modifier = Modifier.testTag("see_all_transactions_button")
        ) {
          Text(text = AppStrings.seeAll(language))
        }
      }
    }

    // Recent Transactions List
    if (recentTransactions.isEmpty()) {
      item {
        Text(
          text = AppStrings.noTransactions(language),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 13.sp,
          modifier = Modifier.padding(vertical = 12.dp)
        )
      }
    } else {
      items(
        items = recentTransactions.take(5),
        key = { it.id }
      ) { item ->
        TransactionItemCard(
          transaction = item,
          currency = currency,
          language = language,
          onEdit = onEditTransaction,
          onDelete = onDeleteTransaction
        )
      }
    }
  }
}
