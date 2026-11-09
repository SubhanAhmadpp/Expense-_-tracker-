package com.example.ui.i18n

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
  ENGLISH("en", "English", "English"),
  URDU("ur", "Urdu", "اردو")
}

object AppStrings {
  // Navigation & Headers
  fun appTitle(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Daily Expense Tracker"
    AppLanguage.URDU -> "روزمرہ اخراجات ٹریکر"
  }

  fun dashboard(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Dashboard"
    AppLanguage.URDU -> "ڈیش بورڈ"
  }

  fun transactions(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Transactions"
    AppLanguage.URDU -> "اندراجات"
  }

  fun monthlyReports(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Reports"
    AppLanguage.URDU -> "رپورٹس"
  }

  fun categories(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Categories"
    AppLanguage.URDU -> "اقسام"
  }

  // Dashboard Stats
  fun totalBalance(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Total Balance"
    AppLanguage.URDU -> "کل بقایا رقم"
  }

  fun totalIncome(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Total Income"
    AppLanguage.URDU -> "کل آمدنی"
  }

  fun totalExpense(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Total Expenses"
    AppLanguage.URDU -> "کل اخراجات"
  }

  fun netSavings(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Net Savings"
    AppLanguage.URDU -> "خالص بچت"
  }

  fun savingsRate(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Savings Rate"
    AppLanguage.URDU -> "بچت کی شرح"
  }

  // Actions
  fun addExpense(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Add Expense"
    AppLanguage.URDU -> "خرچ شامل کریں"
  }

  fun addIncome(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Add Income"
    AppLanguage.URDU -> "آمدنی شامل کریں"
  }

  fun editTransaction(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Edit Record"
    AppLanguage.URDU -> "ترمیم کریں"
  }

  fun deleteTransaction(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Delete Record"
    AppLanguage.URDU -> "حذف کریں"
  }

  fun confirmDeleteTitle(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Delete Record?"
    AppLanguage.URDU -> "کیا آپ ریکارڈ حذف کرنا چاہتے ہیں؟"
  }

  fun confirmDeleteMessage(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Are you sure you want to delete this record? This action cannot be undone."
    AppLanguage.URDU -> "کیا آپ واقعی یہ ریکارڈ مٹانا چاہتے ہیں؟ یہ عمل واپس نہیں لیا جا سکے گا۔"
  }

  fun save(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Save"
    AppLanguage.URDU -> "محفوظ کریں"
  }

  fun cancel(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Cancel"
    AppLanguage.URDU -> "منسوخ کریں"
  }

  fun delete(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Delete"
    AppLanguage.URDU -> "مٹائیں"
  }

  fun edit(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Edit"
    AppLanguage.URDU -> "ترمیم"
  }

  fun filter(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Filter"
    AppLanguage.URDU -> "فلٹر"
  }

  fun clearFilters(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Clear Filters"
    AppLanguage.URDU -> "فلٹر ختم کریں"
  }

  // Filter periods
  fun allTime(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "All Time"
    AppLanguage.URDU -> "تمام وقت"
  }

  fun today(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Today"
    AppLanguage.URDU -> "آج"
  }

  fun thisWeek(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "This Week"
    AppLanguage.URDU -> "اس ہفتے"
  }

  fun thisMonth(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "This Month"
    AppLanguage.URDU -> "اس مہینے"
  }

  fun lastMonth(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Last Month"
    AppLanguage.URDU -> "پچھلے مہینے"
  }

  fun customRange(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Custom Range"
    AppLanguage.URDU -> "مخصوص تاریخ"
  }

  // Form Fields
  fun amount(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Amount"
    AppLanguage.URDU -> "رقم"
  }

  fun title(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Title / Description"
    AppLanguage.URDU -> "عنوان / تفصیل"
  }

  fun category(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Category"
    AppLanguage.URDU -> "قسم / کیٹگری"
  }

  fun date(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Date"
    AppLanguage.URDU -> "تاریخ"
  }

  fun paymentMethod(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Payment Method"
    AppLanguage.URDU -> "ادائیگی کا طریقہ"
  }

  fun note(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Notes (Optional)"
    AppLanguage.URDU -> "اضافی نوٹ (اختیاری)"
  }

  // Payment methods
  fun cash(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Cash"
    AppLanguage.URDU -> "نقد رقم (Cash)"
  }

  fun bankTransfer(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Bank"
    AppLanguage.URDU -> "بینک اکاؤنٹ"
  }

  fun card(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Card"
    AppLanguage.URDU -> "کارڈ"
  }

  fun easypaisa(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Easypaisa"
    AppLanguage.URDU -> "ایزی پیسہ"
  }

  fun jazzCash(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "JazzCash"
    AppLanguage.URDU -> "جاز کیش"
  }

  // Reports & Analytics
  fun monthlyReportTitle(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Monthly Overview"
    AppLanguage.URDU -> "ماہانہ رپورٹ کا جائزہ"
  }

  fun spendingBreakdown(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Spending by Category"
    AppLanguage.URDU -> "اقسام کے لحاظ سے خرچ"
  }

  fun topExpenseCategory(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Top Expense"
    AppLanguage.URDU -> "سب سے بڑا خرچ"
  }

  fun recentTransactions(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Recent Records"
    AppLanguage.URDU -> "حالیہ اندراجات"
  }

  fun seeAll(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "See All"
    AppLanguage.URDU -> "تمام دیکھیں"
  }

  fun noTransactions(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "No records found for the selected period."
    AppLanguage.URDU -> "اس مدت کے لیے کوئی اندراج نہیں ملا۔"
  }

  fun searchPlaceholder(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Search by title or note..."
    AppLanguage.URDU -> "عنوان یا نوٹ تلاش کریں..."
  }

  fun amountError(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Please enter a valid amount greater than 0"
    AppLanguage.URDU -> "براہ کرم 0 سے زیادہ درست رقم درج کریں"
  }

  fun titleError(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Please enter a title"
    AppLanguage.URDU -> "براہ کرم عنوان درج کریں"
  }

  fun currencyName(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Currency"
    AppLanguage.URDU -> "کرنسی"
  }

  fun languageSelector(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Language / زبان"
    AppLanguage.URDU -> "زبان / Language"
  }

  fun quickSummary(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Quick Summary"
    AppLanguage.URDU -> "فوری خلاصہ"
  }

  fun allTypes(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "All Types"
    AppLanguage.URDU -> "تمام اقسام"
  }

  fun expenseOnly(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Expenses Only"
    AppLanguage.URDU -> "صرف اخراجات"
  }

  fun incomeOnly(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Income Only"
    AppLanguage.URDU -> "صرف آمدنی"
  }
}
