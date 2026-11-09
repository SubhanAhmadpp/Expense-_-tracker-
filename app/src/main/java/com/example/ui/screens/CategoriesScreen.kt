package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TransactionEntity
import com.example.data.model.CategoryItem
import com.example.data.model.CategoryPresets
import com.example.data.model.TransactionType
import com.example.ui.components.CategoryIconHelper
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CategoriesScreen(
  allTransactions: List<TransactionEntity>,
  currency: String,
  language: AppLanguage,
  onCurrencyChange: (String) -> Unit,
  onLanguageToggle: () -> Unit,
  modifier: Modifier = Modifier
) {
  val numberFormat = NumberFormat.getNumberInstance(Locale.US)
  var selectedTab by remember { mutableStateOf(0) } // 0: Expense, 1: Income

  val categories = if (selectedTab == 0) CategoryPresets.expenseCategories else CategoryPresets.incomeCategories

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("categories_screen"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // App Settings & Preferences Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("settings_preferences_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = if (language == AppLanguage.URDU) "ترتیبات اور ترجیحات" else "Settings & Preferences",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Language Selector row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .clickable { onLanguageToggle() }
              .padding(14.dp)
              .testTag("toggle_language_btn"),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Language,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = AppStrings.languageSelector(language),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text(
                text = if (language == AppLanguage.ENGLISH) "English (Change)" else "اردو (تبدیل کریں)",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Currency selector row
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Paid,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = AppStrings.currencyName(language),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
              )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              listOf("Rs", "$", "€", "£").forEach { curr ->
                val isSelected = currency == curr
                FilterChip(
                  selected = isSelected,
                  onClick = { onCurrencyChange(curr) },
                  label = { Text(curr, fontWeight = FontWeight.Bold) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                  ),
                  modifier = Modifier.testTag("currency_chip_$curr")
                )
              }
            }
          }
        }
      }
    }

    // Category Type Switcher (Expenses / Income)
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .padding(4.dp)
      ) {
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(if (selectedTab == 0) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable { selectedTab = 0 }
            .padding(vertical = 10.dp)
            .testTag("categories_tab_expense"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (language == AppLanguage.URDU) "اخراجات کی اقسام" else "Expense Categories",
            color = if (selectedTab == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(if (selectedTab == 1) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable { selectedTab = 1 }
            .padding(vertical = 10.dp)
            .testTag("categories_tab_income"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (language == AppLanguage.URDU) "آمدنی کی اقسام" else "Income Categories",
            color = if (selectedTab == 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }
    }

    // List of categories
    items(categories) { cat ->
      val matching = allTransactions.filter { it.categoryId == cat.id }
      val totalAmount = matching.sumOf { it.amount }

      CategoryDetailCard(
        category = cat,
        totalAmount = totalAmount,
        recordCount = matching.size,
        currency = currency,
        language = language
      )
    }
  }
}

@Composable
fun CategoryDetailCard(
  category: CategoryItem,
  totalAmount: Double,
  recordCount: Int,
  currency: String,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val numberFormat = NumberFormat.getNumberInstance(Locale.US)

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(category.color.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = CategoryIconHelper.getIcon(category.iconKey),
            contentDescription = null,
            tint = category.color,
            modifier = Modifier.size(22.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Text(
            text = category.getName(language),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = if (language == AppLanguage.URDU) category.nameEn else category.nameUr,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "$currency ${numberFormat.format(totalAmount)}",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = category.color
        )
        Text(
          text = "$recordCount ${if (language == AppLanguage.URDU) "اندراجات" else "records"}",
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
