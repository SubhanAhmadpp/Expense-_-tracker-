package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.TransactionEntity
import com.example.data.model.CategoryItem
import com.example.data.model.CategoryPresets
import com.example.data.model.TransactionType
import com.example.data.repository.ExpenseRepository
import com.example.ui.i18n.AppLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class DateFilterType {
  ALL,
  TODAY,
  THIS_WEEK,
  THIS_MONTH,
  LAST_MONTH,
  CUSTOM
}

data class CategorySummary(
  val category: CategoryItem,
  val totalAmount: Double,
  val percentage: Float,
  val count: Int
)

data class MonthReportData(
  val year: Int,
  val month: Int, // 0-based
  val displayMonthName: String,
  val totalIncome: Double,
  val totalExpense: Double,
  val netSavings: Double,
  val savingsRate: Float,
  val categorySummaries: List<CategorySummary>,
  val transactions: List<TransactionEntity>
)

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: ExpenseRepository

  init {
    val database = AppDatabase.getDatabase(application, viewModelScope)
    repository = ExpenseRepository(database.transactionDao())

    // Ensure seed data if empty
    viewModelScope.launch(Dispatchers.IO) {
      if (repository.getCount() == 0) {
        AppDatabase.populateInitialData(database.transactionDao())
      }
    }
  }

  // Language state
  private val _language = MutableStateFlow(AppLanguage.ENGLISH)
  val language: StateFlow<AppLanguage> = _language.asStateFlow()

  // Currency symbol
  private val _currency = MutableStateFlow("Rs")
  val currency: StateFlow<String> = _currency.asStateFlow()

  // Selected bottom navigation tab: 0=Dashboard, 1=Transactions, 2=Reports, 3=Categories
  private val _selectedTab = MutableStateFlow(0)
  val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

  // Filter States
  private val _dateFilter = MutableStateFlow(DateFilterType.THIS_MONTH)
  val dateFilter: StateFlow<DateFilterType> = _dateFilter.asStateFlow()

  private val _customDateRange = MutableStateFlow<Pair<Long, Long>?>(null)
  val customDateRange: StateFlow<Pair<Long, Long>?> = _customDateRange.asStateFlow()

  private val _typeFilter = MutableStateFlow<TransactionType?>(null)
  val typeFilter: StateFlow<TransactionType?> = _typeFilter.asStateFlow()

  private val _categoryFilter = MutableStateFlow<String?>(null)
  val categoryFilter: StateFlow<String?> = _categoryFilter.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  // Dialog States
  private val _isAddExpenseDialogOpen = MutableStateFlow(false)
  val isAddExpenseDialogOpen: StateFlow<Boolean> = _isAddExpenseDialogOpen.asStateFlow()

  private val _isAddIncomeDialogOpen = MutableStateFlow(false)
  val isAddIncomeDialogOpen: StateFlow<Boolean> = _isAddIncomeDialogOpen.asStateFlow()

  private val _editingTransaction = MutableStateFlow<TransactionEntity?>(null)
  val editingTransaction: StateFlow<TransactionEntity?> = _editingTransaction.asStateFlow()

  private val _transactionToDelete = MutableStateFlow<TransactionEntity?>(null)
  val transactionToDelete: StateFlow<TransactionEntity?> = _transactionToDelete.asStateFlow()

  private val _isCustomDateRangeDialogOpen = MutableStateFlow(false)
  val isCustomDateRangeDialogOpen: StateFlow<Boolean> = _isCustomDateRangeDialogOpen.asStateFlow()

  // Selected month for report screen (timestamp of first day of month)
  private val _selectedReportMonth = MutableStateFlow(Calendar.getInstance().apply {
    set(Calendar.DAY_OF_MONTH, 1)
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
  }.timeInMillis)
  val selectedReportMonth: StateFlow<Long> = _selectedReportMonth.asStateFlow()

  // Raw database flow
  val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  private data class FilterCriteria(
    val dateFilter: DateFilterType,
    val customRange: Pair<Long, Long>?,
    val typeFilter: TransactionType?,
    val categoryFilter: String?,
    val searchQuery: String
  )

  private val filterCriteria: Flow<FilterCriteria> = combine(
    combine(_dateFilter, _customDateRange, _typeFilter) { d, c, t -> Triple(d, c, t) },
    combine(_categoryFilter, _searchQuery) { cat, q -> Pair(cat, q) }
  ) { (d, c, t), (cat, q) ->
    FilterCriteria(d, c, t, cat, q)
  }

  // Filtered transactions for the transactions tab & quick views
  val filteredTransactions: StateFlow<List<TransactionEntity>> = combine(
    allTransactions,
    filterCriteria
  ) { list, criteria ->
    val (startMillis, endMillis) = getDateBounds(criteria.dateFilter, criteria.customRange)

    list.filter { item ->
      val matchesDate = item.dateMillis in startMillis..endMillis
      val matchesType = criteria.typeFilter == null || item.transactionType == criteria.typeFilter
      val matchesCat = criteria.categoryFilter == null || item.categoryId.equals(criteria.categoryFilter, ignoreCase = true)
      val matchesSearch = criteria.searchQuery.isBlank() ||
        item.title.contains(criteria.searchQuery, ignoreCase = true) ||
        item.note.contains(criteria.searchQuery, ignoreCase = true) ||
        item.categoryNameEn.contains(criteria.searchQuery, ignoreCase = true) ||
        item.categoryNameUr.contains(criteria.searchQuery, ignoreCase = true) ||
        item.paymentMethod.contains(criteria.searchQuery, ignoreCase = true)

      matchesDate && matchesType && matchesCat && matchesSearch
    }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  // All-time & filtered summary calculations
  val summaryStats: StateFlow<Triple<Double, Double, Double>> = filteredTransactions
    .map { items ->
      var income = 0.0
      var expense = 0.0
      items.forEach {
        if (it.transactionType == TransactionType.INCOME) income += it.amount
        else expense += it.amount
      }
      val balance = income - expense
      Triple(income, expense, balance)
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = Triple(0.0, 0.0, 0.0)
    )

  // Category breakdown for filtered transactions (Expenses)
  val expenseCategoryBreakdown: StateFlow<List<CategorySummary>> = filteredTransactions
    .map { items ->
      val expenses = items.filter { it.transactionType == TransactionType.EXPENSE }
      val totalExpense = expenses.sumOf { it.amount }
      if (totalExpense == 0.0) return@map emptyList<CategorySummary>()

      expenses.groupBy { it.categoryId }
        .map { (catId, groupItems) ->
          val catTotal = groupItems.sumOf { it.amount }
          val category = CategoryPresets.findCategory(catId)
          CategorySummary(
            category = category,
            totalAmount = catTotal,
            percentage = (catTotal / totalExpense).toFloat(),
            count = groupItems.size
          )
        }
        .sortedByDescending { it.totalAmount }
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  // Monthly Report Data
  val currentMonthReport = combine(allTransactions, _selectedReportMonth) { items, monthMillis ->
    val cal = Calendar.getInstance().apply { timeInMillis = monthMillis }
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)

    val startCal = Calendar.getInstance().apply {
      set(Calendar.YEAR, year)
      set(Calendar.MONTH, month)
      set(Calendar.DAY_OF_MONTH, 1)
      set(Calendar.HOUR_OF_DAY, 0)
      set(Calendar.MINUTE, 0)
      set(Calendar.SECOND, 0)
      set(Calendar.MILLISECOND, 0)
    }
    val endCal = Calendar.getInstance().apply {
      set(Calendar.YEAR, year)
      set(Calendar.MONTH, month)
      set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
      set(Calendar.HOUR_OF_DAY, 23)
      set(Calendar.MINUTE, 59)
      set(Calendar.SECOND, 59)
      set(Calendar.MILLISECOND, 999)
    }

    val monthItems = items.filter { it.dateMillis in startCal.timeInMillis..endCal.timeInMillis }
    val totalIncome = monthItems.filter { it.transactionType == TransactionType.INCOME }.sumOf { it.amount }
    val totalExpense = monthItems.filter { it.transactionType == TransactionType.EXPENSE }.sumOf { it.amount }
    val netSavings = totalIncome - totalExpense
    val savingsRate = if (totalIncome > 0) ((netSavings / totalIncome) * 100).toFloat() else 0f

    val expenses = monthItems.filter { it.transactionType == TransactionType.EXPENSE }
    val summaries = if (totalExpense > 0) {
      expenses.groupBy { it.categoryId }
        .map { (catId, group) ->
          val sum = group.sumOf { it.amount }
          CategorySummary(
            category = CategoryPresets.findCategory(catId),
            totalAmount = sum,
            percentage = (sum / totalExpense).toFloat(),
            count = group.size
          )
        }
        .sortedByDescending { it.totalAmount }
    } else emptyList()

    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    MonthReportData(
      year = year,
      month = month,
      displayMonthName = monthFormat.format(Date(monthMillis)),
      totalIncome = totalIncome,
      totalExpense = totalExpense,
      netSavings = netSavings,
      savingsRate = savingsRate,
      categorySummaries = summaries,
      transactions = monthItems
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = MonthReportData(
      year = 2026,
      month = 10,
      displayMonthName = "November 2026",
      totalIncome = 0.0,
      totalExpense = 0.0,
      netSavings = 0.0,
      savingsRate = 0f,
      categorySummaries = emptyList(),
      transactions = emptyList()
    )
  )

  // Actions
  fun setTab(index: Int) {
    _selectedTab.value = index
  }

  fun toggleLanguage() {
    _language.value = if (_language.value == AppLanguage.ENGLISH) AppLanguage.URDU else AppLanguage.ENGLISH
  }

  fun setLanguage(lang: AppLanguage) {
    _language.value = lang
  }

  fun setCurrency(newCurrency: String) {
    _currency.value = newCurrency
  }

  fun setDateFilter(filter: DateFilterType) {
    _dateFilter.value = filter
  }

  fun setCustomDateRange(startMillis: Long, endMillis: Long) {
    _customDateRange.value = Pair(startMillis, endMillis)
    _dateFilter.value = DateFilterType.CUSTOM
  }

  fun setTypeFilter(type: TransactionType?) {
    _typeFilter.value = type
  }

  fun setCategoryFilter(categoryId: String?) {
    _categoryFilter.value = categoryId
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun changeReportMonth(offsetMonths: Int) {
    val cal = Calendar.getInstance().apply {
      timeInMillis = _selectedReportMonth.value
      add(Calendar.MONTH, offsetMonths)
    }
    _selectedReportMonth.value = cal.timeInMillis
  }

  // Dialog Controls
  fun openAddExpenseDialog() {
    _isAddExpenseDialogOpen.value = true
  }

  fun closeAddExpenseDialog() {
    _isAddExpenseDialogOpen.value = false
  }

  fun openAddIncomeDialog() {
    _isAddIncomeDialogOpen.value = true
  }

  fun closeAddIncomeDialog() {
    _isAddIncomeDialogOpen.value = false
  }

  fun openEditDialog(transaction: TransactionEntity) {
    _editingTransaction.value = transaction
  }

  fun closeEditDialog() {
    _editingTransaction.value = null
  }

  fun requestDelete(transaction: TransactionEntity) {
    _transactionToDelete.value = transaction
  }

  fun dismissDeleteDialog() {
    _transactionToDelete.value = null
  }

  fun openCustomDateRangeDialog() {
    _isCustomDateRangeDialogOpen.value = true
  }

  fun closeCustomDateRangeDialog() {
    _isCustomDateRangeDialogOpen.value = false
  }

  // Database CRUD operations
  fun saveTransaction(
    id: Long = 0,
    title: String,
    amount: Double,
    type: TransactionType,
    categoryId: String,
    dateMillis: Long,
    paymentMethod: String,
    note: String
  ) {
    val cat = CategoryPresets.findCategory(categoryId)
    val entity = TransactionEntity(
      id = id,
      title = title.trim(),
      amount = amount,
      type = type.name,
      categoryId = categoryId,
      categoryNameEn = cat.nameEn,
      categoryNameUr = cat.nameUr,
      dateMillis = dateMillis,
      paymentMethod = paymentMethod,
      note = note.trim()
    )

    viewModelScope.launch(Dispatchers.IO) {
      if (id == 0L) {
        repository.insert(entity)
      } else {
        repository.update(entity)
      }
    }
  }

  fun confirmDelete() {
    val toDelete = _transactionToDelete.value ?: return
    viewModelScope.launch(Dispatchers.IO) {
      repository.delete(toDelete)
      _transactionToDelete.value = null
    }
  }

  // Helper function for date bounds
  private fun getDateBounds(
    filter: DateFilterType,
    customRange: Pair<Long, Long>?
  ): Pair<Long, Long> {
    val cal = Calendar.getInstance()

    return when (filter) {
      DateFilterType.ALL -> Pair(0L, Long.MAX_VALUE)
      DateFilterType.TODAY -> {
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        val end = cal.timeInMillis
        Pair(start, end)
      }
      DateFilterType.THIS_WEEK -> {
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.add(Calendar.DAY_OF_WEEK, 6)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        val end = cal.timeInMillis
        Pair(start, end)
      }
      DateFilterType.THIS_MONTH -> {
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        val end = cal.timeInMillis
        Pair(start, end)
      }
      DateFilterType.LAST_MONTH -> {
        cal.add(Calendar.MONTH, -1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        val end = cal.timeInMillis
        Pair(start, end)
      }
      DateFilterType.CUSTOM -> {
        customRange ?: Pair(0L, Long.MAX_VALUE)
      }
    }
  }
}
