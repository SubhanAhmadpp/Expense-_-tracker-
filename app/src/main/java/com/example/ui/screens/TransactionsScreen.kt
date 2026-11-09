package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TransactionEntity
import com.example.data.model.CategoryPresets
import com.example.data.model.TransactionType
import com.example.ui.components.DateFilterBar
import com.example.ui.components.TransactionItemCard
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.viewmodel.DateFilterType

@Composable
fun TransactionsScreen(
  transactions: List<TransactionEntity>,
  currency: String,
  language: AppLanguage,
  dateFilter: DateFilterType,
  typeFilter: TransactionType?,
  categoryFilter: String?,
  searchQuery: String,
  onDateFilterChange: (DateFilterType) -> Unit,
  onOpenCustomRangeDialog: () -> Unit,
  onTypeFilterChange: (TransactionType?) -> Unit,
  onCategoryFilterChange: (String?) -> Unit,
  onSearchQueryChange: (String) -> Unit,
  onEditTransaction: (TransactionEntity) -> Unit,
  onDeleteTransaction: (TransactionEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("transactions_screen")
  ) {
    // Search Bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = onSearchQueryChange,
      placeholder = { Text(AppStrings.searchPlaceholder(language), fontSize = 13.sp) },
      leadingIcon = {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = "Search",
          tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
      },
      trailingIcon = if (searchQuery.isNotEmpty()) {
        {
          IconButton(
            onClick = { onSearchQueryChange("") },
            modifier = Modifier.testTag("clear_search_btn")
          ) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
          }
        }
      } else null,
      singleLine = true,
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp)
        .testTag("transactions_search_input")
    )

    // Date Filter Bar
    DateFilterBar(
      selectedFilter = dateFilter,
      onSelectFilter = onDateFilterChange,
      onOpenCustomRangeDialog = onOpenCustomRangeDialog,
      language = language,
      modifier = Modifier.padding(horizontal = 12.dp)
    )

    // Type Filter Chips: All / Expenses / Income
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      FilterChip(
        selected = typeFilter == null,
        onClick = { onTypeFilterChange(null) },
        label = { Text(AppStrings.allTypes(language), fontSize = 12.sp) },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = MaterialTheme.colorScheme.primary,
          selectedLabelColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.testTag("type_filter_all")
      )

      FilterChip(
        selected = typeFilter == TransactionType.EXPENSE,
        onClick = { onTypeFilterChange(TransactionType.EXPENSE) },
        label = { Text(AppStrings.expenseOnly(language), fontSize = 12.sp) },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = MaterialTheme.colorScheme.tertiary,
          selectedLabelColor = MaterialTheme.colorScheme.onTertiary
        ),
        modifier = Modifier.testTag("type_filter_expense")
      )

      FilterChip(
        selected = typeFilter == TransactionType.INCOME,
        onClick = { onTypeFilterChange(TransactionType.INCOME) },
        label = { Text(AppStrings.incomeOnly(language), fontSize = 12.sp) },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = MaterialTheme.colorScheme.secondary,
          selectedLabelColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = Modifier.testTag("type_filter_income")
      )
    }

    // Category Filter Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp, vertical = 2.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      FilterChip(
        selected = categoryFilter == null,
        onClick = { onCategoryFilterChange(null) },
        label = { Text(if (language == AppLanguage.URDU) "تمام اقسام" else "All Categories", fontSize = 11.sp) }
      )

      val categoriesToShow = if (typeFilter == TransactionType.INCOME) {
        CategoryPresets.incomeCategories
      } else if (typeFilter == TransactionType.EXPENSE) {
        CategoryPresets.expenseCategories
      } else {
        CategoryPresets.allCategories
      }

      categoriesToShow.forEach { cat ->
        val isSelected = categoryFilter == cat.id
        FilterChip(
          selected = isSelected,
          onClick = {
            onCategoryFilterChange(if (isSelected) null else cat.id)
          },
          label = { Text(cat.getName(language), fontSize = 11.sp) }
        )
      }
    }

    // Filter summary counter
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "${transactions.size} ${if (language == AppLanguage.URDU) "ریکارڈز ملے" else "records found"}",
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // Transactions LazyColumn
    if (transactions.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.ReceiptLong,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = AppStrings.noTransactions(language),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(
          items = transactions,
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
}
