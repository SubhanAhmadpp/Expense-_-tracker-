package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TransactionType
import com.example.ui.components.AddEditTransactionDialog
import com.example.ui.components.CustomDateRangeDialog
import com.example.ui.components.DeleteConfirmDialog
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.MonthlyReportsScreen
import com.example.ui.screens.TransactionsScreen
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.IncomeGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ExpenseViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: ExpenseViewModel by viewModels()

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme {
        val language by viewModel.language.collectAsStateWithLifecycle()
        val currency by viewModel.currency.collectAsStateWithLifecycle()
        val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

        // Filter states
        val dateFilter by viewModel.dateFilter.collectAsStateWithLifecycle()
        val typeFilter by viewModel.typeFilter.collectAsStateWithLifecycle()
        val categoryFilter by viewModel.categoryFilter.collectAsStateWithLifecycle()
        val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

        // Financial & Report Data
        val summaryStats by viewModel.summaryStats.collectAsStateWithLifecycle()
        val allTransactions by viewModel.allTransactions.collectAsStateWithLifecycle()
        val filteredTransactions by viewModel.filteredTransactions.collectAsStateWithLifecycle()
        val expenseCategoryBreakdown by viewModel.expenseCategoryBreakdown.collectAsStateWithLifecycle()
        val currentMonthReport by viewModel.currentMonthReport.collectAsStateWithLifecycle()

        // Dialog states
        val isAddExpenseOpen by viewModel.isAddExpenseDialogOpen.collectAsStateWithLifecycle()
        val isAddIncomeOpen by viewModel.isAddIncomeDialogOpen.collectAsStateWithLifecycle()
        val editingTransaction by viewModel.editingTransaction.collectAsStateWithLifecycle()
        val transactionToDelete by viewModel.transactionToDelete.collectAsStateWithLifecycle()
        val isCustomDateRangeOpen by viewModel.isCustomDateRangeDialogOpen.collectAsStateWithLifecycle()

        // BackHandler: Return to Dashboard tab before exiting
        BackHandler(enabled = selectedTab != 0) {
          viewModel.setTab(0)
        }

        Scaffold(
          modifier = Modifier
            .fillMaxSize()
            .testTag("main_app_scaffold"),
          topBar = {
            CenterAlignedTopAppBar(
              title = {
                Text(
                  text = AppStrings.appTitle(language),
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp
                )
              },
              colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
              ),
              actions = {
                // Language quick toggle chip in TopBar
                Box(
                  modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { viewModel.toggleLanguage() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("appbar_language_toggle"),
                  contentAlignment = Alignment.Center
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Language,
                      contentDescription = "Language",
                      tint = MaterialTheme.colorScheme.onPrimaryContainer,
                      modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = if (language == AppLanguage.ENGLISH) "اردو" else "English",
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                  }
                }
              }
            )
          },
          bottomBar = {
            NavigationBar(
              containerColor = MaterialTheme.colorScheme.surface,
              tonalElevation = 6.dp,
              modifier = Modifier.testTag("bottom_nav_bar")
            ) {
              NavigationBarItem(
                selected = selectedTab == 0,
                onClick = { viewModel.setTab(0) },
                icon = { Icon(Icons.Default.Dashboard, contentDescription = AppStrings.dashboard(language)) },
                label = { Text(AppStrings.dashboard(language), fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_tab_dashboard")
              )

              NavigationBarItem(
                selected = selectedTab == 1,
                onClick = { viewModel.setTab(1) },
                icon = { Icon(Icons.Default.ReceiptLong, contentDescription = AppStrings.transactions(language)) },
                label = { Text(AppStrings.transactions(language), fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_tab_transactions")
              )

              NavigationBarItem(
                selected = selectedTab == 2,
                onClick = { viewModel.setTab(2) },
                icon = { Icon(Icons.Default.Assessment, contentDescription = AppStrings.monthlyReports(language)) },
                label = { Text(AppStrings.monthlyReports(language), fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_tab_reports")
              )

              NavigationBarItem(
                selected = selectedTab == 3,
                onClick = { viewModel.setTab(3) },
                icon = { Icon(Icons.Default.Category, contentDescription = AppStrings.categories(language)) },
                label = { Text(AppStrings.categories(language), fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_tab_categories")
              )
            }
          },
          floatingActionButton = {
            if (selectedTab == 1 || selectedTab == 2) {
              ExtendedFloatingActionButton(
                onClick = { viewModel.openAddExpenseDialog() },
                containerColor = ExpenseRed,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text(AppStrings.addExpense(language), fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("fab_add_expense")
              )
            }
          }
        ) { paddingValues ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(paddingValues)
          ) {
            when (selectedTab) {
              0 -> DashboardScreen(
                balance = summaryStats.third,
                income = summaryStats.first,
                expense = summaryStats.second,
                currency = currency,
                language = language,
                dateFilter = dateFilter,
                categorySummaries = expenseCategoryBreakdown,
                recentTransactions = filteredTransactions,
                onDateFilterChange = { viewModel.setDateFilter(it) },
                onOpenCustomRangeDialog = { viewModel.openCustomDateRangeDialog() },
                onAddExpense = { viewModel.openAddExpenseDialog() },
                onAddIncome = { viewModel.openAddIncomeDialog() },
                onEditTransaction = { viewModel.openEditDialog(it) },
                onDeleteTransaction = { viewModel.requestDelete(it) },
                onSeeAllTransactions = { viewModel.setTab(1) }
              )

              1 -> TransactionsScreen(
                transactions = filteredTransactions,
                currency = currency,
                language = language,
                dateFilter = dateFilter,
                typeFilter = typeFilter,
                categoryFilter = categoryFilter,
                searchQuery = searchQuery,
                onDateFilterChange = { viewModel.setDateFilter(it) },
                onOpenCustomRangeDialog = { viewModel.openCustomDateRangeDialog() },
                onTypeFilterChange = { viewModel.setTypeFilter(it) },
                onCategoryFilterChange = { viewModel.setCategoryFilter(it) },
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                onEditTransaction = { viewModel.openEditDialog(it) },
                onDeleteTransaction = { viewModel.requestDelete(it) }
              )

              2 -> MonthlyReportsScreen(
                reportData = currentMonthReport,
                currency = currency,
                language = language,
                onChangeMonth = { viewModel.changeReportMonth(it) },
                onEditTransaction = { viewModel.openEditDialog(it) },
                onDeleteTransaction = { viewModel.requestDelete(it) }
              )

              3 -> CategoriesScreen(
                allTransactions = allTransactions,
                currency = currency,
                language = language,
                onCurrencyChange = { viewModel.setCurrency(it) },
                onLanguageToggle = { viewModel.toggleLanguage() }
              )
            }
          }
        }

        // Add Expense Dialog
        if (isAddExpenseOpen) {
          AddEditTransactionDialog(
            initialType = TransactionType.EXPENSE,
            currency = currency,
            language = language,
            onDismiss = { viewModel.closeAddExpenseDialog() },
            onSave = { id, title, amount, type, categoryId, dateMillis, paymentMethod, note ->
              viewModel.saveTransaction(
                id = id,
                title = title,
                amount = amount,
                type = type,
                categoryId = categoryId,
                dateMillis = dateMillis,
                paymentMethod = paymentMethod,
                note = note
              )
            }
          )
        }

        // Add Income Dialog
        if (isAddIncomeOpen) {
          AddEditTransactionDialog(
            initialType = TransactionType.INCOME,
            currency = currency,
            language = language,
            onDismiss = { viewModel.closeAddIncomeDialog() },
            onSave = { id, title, amount, type, categoryId, dateMillis, paymentMethod, note ->
              viewModel.saveTransaction(
                id = id,
                title = title,
                amount = amount,
                type = type,
                categoryId = categoryId,
                dateMillis = dateMillis,
                paymentMethod = paymentMethod,
                note = note
              )
            }
          )
        }

        // Edit Transaction Dialog
        editingTransaction?.let { item ->
          AddEditTransactionDialog(
            initialType = item.transactionType,
            editingItem = item,
            currency = currency,
            language = language,
            onDismiss = { viewModel.closeEditDialog() },
            onSave = { id, title, amount, type, categoryId, dateMillis, paymentMethod, note ->
              viewModel.saveTransaction(
                id = id,
                title = title,
                amount = amount,
                type = type,
                categoryId = categoryId,
                dateMillis = dateMillis,
                paymentMethod = paymentMethod,
                note = note
              )
            }
          )
        }

        // Delete Confirmation Dialog
        if (transactionToDelete != null) {
          DeleteConfirmDialog(
            language = language,
            onConfirm = { viewModel.confirmDelete() },
            onDismiss = { viewModel.dismissDeleteDialog() }
          )
        }

        // Custom Date Range Dialog
        if (isCustomDateRangeOpen) {
          CustomDateRangeDialog(
            language = language,
            onDismiss = { viewModel.closeCustomDateRangeDialog() },
            onApply = { startMillis, endMillis ->
              viewModel.setCustomDateRange(startMillis, endMillis)
            }
          )
        }
      }
    }
  }
}
