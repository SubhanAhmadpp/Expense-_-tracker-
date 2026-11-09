package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.i18n.AppLanguage

enum class TransactionType {
  EXPENSE,
  INCOME;

  fun getDisplayName(lang: AppLanguage): String = when (this) {
    EXPENSE -> if (lang == AppLanguage.URDU) "خرچ" else "Expense"
    INCOME -> if (lang == AppLanguage.URDU) "آمدنی" else "Income"
  }
}

data class CategoryItem(
  val id: String,
  val nameEn: String,
  val nameUr: String,
  val iconKey: String,
  val color: Color,
  val type: TransactionType
) {
  fun getName(lang: AppLanguage): String = if (lang == AppLanguage.URDU) nameUr else nameEn
}

object CategoryPresets {
  val expenseCategories = listOf(
    CategoryItem("food", "Food & Dining", "کھانا پینا", "restaurant", Color(0xFFEA580C), TransactionType.EXPENSE),
    CategoryItem("groceries", "Groceries", "راشن اور سودا", "shopping_cart", Color(0xFF16A34A), TransactionType.EXPENSE),
    CategoryItem("transport", "Transportation", "ٹرانسپورٹ و پیٹرول", "directions_car", Color(0xFF2563EB), TransactionType.EXPENSE),
    CategoryItem("bills", "Bills & Utilities", "بجلی، گیس و بل", "receipt_long", Color(0xFF7C3AED), TransactionType.EXPENSE),
    CategoryItem("shopping", "Shopping", "خریداری", "shopping_bag", Color(0xFFDB2777), TransactionType.EXPENSE),
    CategoryItem("health", "Health & Medical", "صحت و ادویات", "medical_services", Color(0xFFDC2626), TransactionType.EXPENSE),
    CategoryItem("education", "Education", "تعلیم و فیس", "school", Color(0xFF0284C7), TransactionType.EXPENSE),
    CategoryItem("entertainment", "Entertainment", "تفریح", "movie", Color(0xFF9333EA), TransactionType.EXPENSE),
    CategoryItem("rent", "Rent & Housing", "مکان کا کرایہ", "home", Color(0xFFCA8A04), TransactionType.EXPENSE),
    CategoryItem("other_expense", "Other Expense", "دیگر اخراجات", "more_horiz", Color(0xFF64748B), TransactionType.EXPENSE)
  )

  val incomeCategories = listOf(
    CategoryItem("salary", "Salary", "تنخواہ", "work", Color(0xFF059669), TransactionType.INCOME),
    CategoryItem("business", "Business", "کاروبار و منافع", "store", Color(0xFF0D9488), TransactionType.INCOME),
    CategoryItem("freelance", "Freelance", "فری لانس کام", "laptop", Color(0xFF0284C7), TransactionType.INCOME),
    CategoryItem("investments", "Investments", "سرمایہ کاری", "trending_up", Color(0xFF4F46E5), TransactionType.INCOME),
    CategoryItem("rental_income", "Rental Income", "کرایہ کی آمدنی", "domain", Color(0xFFD97706), TransactionType.INCOME),
    CategoryItem("gift_bonus", "Gift / Bonus", "انعام یا تحفہ", "card_giftcard", Color(0xFFE11D48), TransactionType.INCOME),
    CategoryItem("other_income", "Other Income", "دیگر آمدنی", "attach_money", Color(0xFF10B981), TransactionType.INCOME)
  )

  val allCategories = expenseCategories + incomeCategories

  fun findCategory(id: String): CategoryItem {
    return allCategories.find { it.id.equals(id, ignoreCase = true) }
      ?: expenseCategories.last()
  }
}
