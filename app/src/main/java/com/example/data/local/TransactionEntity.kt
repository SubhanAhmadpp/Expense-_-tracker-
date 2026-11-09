package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.TransactionType

@Entity(tableName = "transactions")
data class TransactionEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val title: String,
  val amount: Double,
  val type: String, // "EXPENSE" or "INCOME"
  val categoryId: String,
  val categoryNameEn: String,
  val categoryNameUr: String,
  val dateMillis: Long,
  val paymentMethod: String,
  val note: String = "",
  val createdAt: Long = System.currentTimeMillis()
) {
  val transactionType: TransactionType
    get() = try {
      TransactionType.valueOf(type)
    } catch (_: Exception) {
      TransactionType.EXPENSE
    }
}
