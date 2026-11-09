package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.i18n.AppLanguage
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.IncomeGreen
import com.example.ui.viewmodel.CategorySummary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CategoryBreakdownCard(
  categorySummaries: List<CategorySummary>,
  currency: String,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val numberFormat = NumberFormat.getNumberInstance(Locale.US)

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("category_breakdown_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (language == AppLanguage.URDU) "اقسام کے لحاظ سے خرچ" else "Spending by Category",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          color = MaterialTheme.colorScheme.onSurface
        )

        Text(
          text = "${categorySummaries.size} ${if (language == AppLanguage.URDU) "اقسام" else "Categories"}",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      if (categorySummaries.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (language == AppLanguage.URDU) "کوئی اخراجات ریکارڈ نہیں ہوئے" else "No expense records in this period",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
          )
        }
      } else {
        // Multi-segment horizontal bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .height(14.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
          categorySummaries.take(5).forEach { summary ->
            val fraction = summary.percentage.coerceIn(0.01f, 1f)
            Box(
              modifier = Modifier
                .weight(fraction)
                .fillMaxHeight()
                .background(summary.category.color)
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Breakdown items
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
          categorySummaries.forEach { summary ->
            CategorySummaryRow(
              summary = summary,
              currency = currency,
              language = language
            )
          }
        }
      }
    }
  }
}

@Composable
fun CategorySummaryRow(
  summary: CategorySummary,
  currency: String,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val numberFormat = NumberFormat.getNumberInstance(Locale.US)
  val percentInt = (summary.percentage * 100).toInt()

  Column(modifier = modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(summary.category.color.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = CategoryIconHelper.getIcon(summary.category.iconKey),
            contentDescription = null,
            tint = summary.category.color,
            modifier = Modifier.size(16.dp)
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
          Text(
            text = summary.category.getName(language),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "${summary.count} ${if (language == AppLanguage.URDU) "اندراجات" else "transactions"}",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "$currency ${numberFormat.format(summary.totalAmount)}",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "$percentInt%",
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          color = summary.category.color
        )
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    // Individual progress bar
    val animatedProgress by animateFloatAsState(
      targetValue = summary.percentage.coerceIn(0f, 1f),
      label = "category_progress"
    )

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(6.dp)
        .clip(RoundedCornerShape(3.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth(animatedProgress)
          .fillMaxHeight()
          .background(summary.category.color)
      )
    }
  }
}

@Composable
fun IncomeExpenseRatioBar(
  income: Double,
  expense: Double,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val total = income + expense
  val incomeRatio = if (total > 0) (income / total).toFloat() else 0.5f
  val expenseRatio = if (total > 0) (expense / total).toFloat() else 0.5f

  Column(modifier = modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "${if (language == AppLanguage.URDU) "آمدنی" else "Income"}: ${(incomeRatio * 100).toInt()}%",
        color = IncomeGreen,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "${if (language == AppLanguage.URDU) "اخراجات" else "Expenses"}: ${(expenseRatio * 100).toInt()}%",
        color = ExpenseRed,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )
    }

    Spacer(modifier = Modifier.height(6.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(10.dp)
        .clip(RoundedCornerShape(5.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
      if (total > 0) {
        Box(
          modifier = Modifier
            .weight(incomeRatio.coerceAtLeast(0.01f))
            .fillMaxHeight()
            .background(IncomeGreen)
        )
        Box(
          modifier = Modifier
            .weight(expenseRatio.coerceAtLeast(0.01f))
            .fillMaxHeight()
            .background(ExpenseRed)
        )
      } else {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceVariant)
        )
      }
    }
  }
}
