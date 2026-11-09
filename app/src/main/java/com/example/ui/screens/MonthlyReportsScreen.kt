package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TransactionEntity
import com.example.ui.components.CategoryBreakdownCard
import com.example.ui.components.IncomeExpenseRatioBar
import com.example.ui.components.TransactionItemCard
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.IncomeGreen
import com.example.ui.viewmodel.MonthReportData
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MonthlyReportsScreen(
  reportData: MonthReportData,
  currency: String,
  language: AppLanguage,
  onChangeMonth: (Int) -> Unit,
  onEditTransaction: (TransactionEntity) -> Unit,
  onDeleteTransaction: (TransactionEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  val numberFormat = NumberFormat.getNumberInstance(Locale.US)

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("monthly_reports_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Month Navigator Selector
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("month_selector_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = { onChangeMonth(-1) },
            modifier = Modifier.testTag("prev_month_btn")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Previous Month"
            )
          }

          Text(
            text = reportData.displayMonthName,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onSurface
          )

          IconButton(
            onClick = { onChangeMonth(1) },
            modifier = Modifier.testTag("next_month_btn")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = "Next Month"
            )
          }
        }
      }
    }

    // Monthly Overview Banner
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("monthly_summary_banner"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = AppStrings.monthlyReportTitle(language),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(16.dp))

          // 2x2 stats grid
          Row(modifier = Modifier.fillMaxWidth()) {
            // Income
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(IncomeGreen.copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = IncomeGreen,
                    modifier = Modifier.size(14.dp)
                  )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = AppStrings.totalIncome(language),
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "$currency ${numberFormat.format(reportData.totalIncome)}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = IncomeGreen
              )
            }

            // Expense
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(ExpenseRed.copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.TrendingDown,
                    contentDescription = null,
                    tint = ExpenseRed,
                    modifier = Modifier.size(14.dp)
                  )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = AppStrings.totalExpense(language),
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "$currency ${numberFormat.format(reportData.totalExpense)}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = ExpenseRed
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Net Savings & Savings Rate
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Savings,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = AppStrings.netSavings(language),
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = "$currency ${numberFormat.format(reportData.netSavings)}",
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = if (reportData.netSavings >= 0) IncomeGreen else ExpenseRed
                )
              }
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = AppStrings.savingsRate(language),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = "${reportData.savingsRate.toInt()}%",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = if (reportData.savingsRate >= 20) IncomeGreen else MaterialTheme.colorScheme.primary
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Income vs Expense Comparison Bar
          IncomeExpenseRatioBar(
            income = reportData.totalIncome,
            expense = reportData.totalExpense,
            language = language
          )
        }
      }
    }

    // Category Spending Breakdown
    item {
      CategoryBreakdownCard(
        categorySummaries = reportData.categorySummaries,
        currency = currency,
        language = language
      )
    }

    // Transactions list for this month
    item {
      Text(
        text = "${if (language == AppLanguage.URDU) "اس مہینے کے اندراجات" else "Transactions in"} ${reportData.displayMonthName} (${reportData.transactions.size})",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(top = 8.dp)
      )
    }

    if (reportData.transactions.isEmpty()) {
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
        items = reportData.transactions,
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
