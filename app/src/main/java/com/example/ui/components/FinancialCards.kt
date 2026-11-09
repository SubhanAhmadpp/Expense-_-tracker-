package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.ExpenseRedContainer
import com.example.ui.theme.IncomeGreen
import com.example.ui.theme.IncomeGreenContainer
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BalanceCard(
  balance: Double,
  income: Double,
  expense: Double,
  currency: String,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val numberFormat = NumberFormat.getNumberInstance(Locale.US)
  val isPositive = balance >= 0

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("balance_card"),
    shape = RoundedCornerShape(24.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          brush = Brush.linearGradient(
            colors = listOf(
              Color(0xFF0F172A),
              Color(0xFF1E3A8A),
              Color(0xFF1E40AF)
            )
          )
        )
        .padding(24.dp)
    ) {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = AppStrings.totalBalance(language),
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
          )

          // Status Badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (isPositive) Color(0xFF10B981).copy(alpha = 0.25f) else Color(0xFFEF4444).copy(alpha = 0.25f))
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = if (isPositive) {
                if (language == AppLanguage.URDU) "منافع / بچت" else "Surplus"
              } else {
                if (language == AppLanguage.URDU) "خسارہ / خسارہ" else "Deficit"
              },
              color = if (isPositive) Color(0xFF34D399) else Color(0xFFFCA5A5),
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Large Balance Amount
        Row(
          verticalAlignment = Alignment.Bottom
        ) {
          Text(
            text = "$currency ",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = numberFormat.format(balance),
            color = Color.White,
            fontSize = 34.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.testTag("total_balance_text")
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Mini Income & Expense footer inside balance card
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          // Income mini
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color(0xFF10B981).copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.ArrowUpward,
                contentDescription = null,
                tint = Color(0xFF34D399),
                modifier = Modifier.size(16.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = AppStrings.totalIncome(language),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp
              )
              Text(
                text = "+$currency ${numberFormat.format(income)}",
                color = Color(0xFF34D399),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          // Expense mini
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color(0xFFEF4444).copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.ArrowDownward,
                contentDescription = null,
                tint = Color(0xFFF87171),
                modifier = Modifier.size(16.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = AppStrings.totalExpense(language),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp
              )
              Text(
                text = "-$currency ${numberFormat.format(expense)}",
                color = Color(0xFFF87171),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun StatCardsRow(
  income: Double,
  expense: Double,
  currency: String,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val numberFormat = NumberFormat.getNumberInstance(Locale.US)

  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Income Card
    Card(
      modifier = Modifier
        .weight(1f)
        .testTag("income_stat_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = IncomeGreenContainer),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = AppStrings.totalIncome(language),
            color = IncomeGreen,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(IncomeGreen.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.ArrowUpward,
              contentDescription = null,
              tint = IncomeGreen,
              modifier = Modifier.size(18.dp)
            )
          }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "$currency ${numberFormat.format(income)}",
          color = Color(0xFF064E3B),
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.testTag("total_income_text")
        )
      }
    }

    // Expense Card
    Card(
      modifier = Modifier
        .weight(1f)
        .testTag("expense_stat_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = ExpenseRedContainer),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = AppStrings.totalExpense(language),
            color = ExpenseRed,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(ExpenseRed.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.ArrowDownward,
              contentDescription = null,
              tint = ExpenseRed,
              modifier = Modifier.size(18.dp)
            )
          }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "$currency ${numberFormat.format(expense)}",
          color = Color(0xFF7F1D1D),
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.testTag("total_expense_text")
        )
      }
    }
  }
}

@Composable
fun QuickActionButtons(
  onAddExpense: () -> Unit,
  onAddIncome: () -> Unit,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Add Expense Button
    Button(
      onClick = onAddExpense,
      modifier = Modifier
        .weight(1f)
        .height(52.dp)
        .testTag("add_expense_button"),
      shape = RoundedCornerShape(16.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = ExpenseRed,
        contentColor = Color.White
      )
    ) {
      Icon(
        imageVector = Icons.Default.Add,
        contentDescription = null,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = AppStrings.addExpense(language),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
      )
    }

    // Add Income Button
    Button(
      onClick = onAddIncome,
      modifier = Modifier
        .weight(1f)
        .height(52.dp)
        .testTag("add_income_button"),
      shape = RoundedCornerShape(16.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = IncomeGreen,
        contentColor = Color.White
      )
    ) {
      Icon(
        imageVector = Icons.Default.Add,
        contentDescription = null,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = AppStrings.addIncome(language),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
      )
    }
  }
}
