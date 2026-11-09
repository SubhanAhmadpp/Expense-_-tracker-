package com.example

import com.example.data.local.TransactionEntity
import com.example.data.model.CategoryPresets
import com.example.data.model.TransactionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testBalanceCalculation() {
    val items = listOf(
      TransactionEntity(
        id = 1,
        title = "Salary",
        amount = 100000.0,
        type = "INCOME",
        categoryId = "salary",
        categoryNameEn = "Salary",
        categoryNameUr = "تنخواہ",
        dateMillis = System.currentTimeMillis(),
        paymentMethod = "Bank"
      ),
      TransactionEntity(
        id = 2,
        title = "Grocery",
        amount = 15000.0,
        type = "EXPENSE",
        categoryId = "groceries",
        categoryNameEn = "Groceries",
        categoryNameUr = "راشن اور سودا",
        dateMillis = System.currentTimeMillis(),
        paymentMethod = "Cash"
      ),
      TransactionEntity(
        id = 3,
        title = "Electricity",
        amount = 5000.0,
        type = "EXPENSE",
        categoryId = "bills",
        categoryNameEn = "Bills & Utilities",
        categoryNameUr = "بجلی، گیس و بل",
        dateMillis = System.currentTimeMillis(),
        paymentMethod = "JazzCash"
      )
    )

    val income = items.filter { it.transactionType == TransactionType.INCOME }.sumOf { it.amount }
    val expense = items.filter { it.transactionType == TransactionType.EXPENSE }.sumOf { it.amount }
    val balance = income - expense

    assertEquals(100000.0, income, 0.001)
    assertEquals(20000.0, expense, 0.001)
    assertEquals(80000.0, balance, 0.001)
    assertTrue(balance > 0)
  }

  @Test
  fun testCategoriesPresets() {
    val foodCat = CategoryPresets.findCategory("food")
    assertNotNull(foodCat)
    assertEquals("Food & Dining", foodCat.nameEn)
    assertEquals("کھانا پینا", foodCat.nameUr)

    val salaryCat = CategoryPresets.findCategory("salary")
    assertNotNull(salaryCat)
    assertEquals(TransactionType.INCOME, salaryCat.type)
  }
}
