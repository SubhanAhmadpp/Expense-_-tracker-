package com.example.data.repository

import com.example.data.local.TransactionDao
import com.example.data.local.TransactionEntity
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val transactionDao: TransactionDao) {
  val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()

  fun getTransactionsBetween(startMillis: Long, endMillis: Long): Flow<List<TransactionEntity>> {
    return transactionDao.getTransactionsBetween(startMillis, endMillis)
  }

  suspend fun insert(transaction: TransactionEntity): Long {
    return transactionDao.insert(transaction)
  }

  suspend fun update(transaction: TransactionEntity) {
    transactionDao.update(transaction)
  }

  suspend fun delete(transaction: TransactionEntity) {
    transactionDao.delete(transaction)
  }

  suspend fun deleteById(id: Long) {
    transactionDao.deleteById(id)
  }

  suspend fun getCount(): Int {
    return transactionDao.getCount()
  }
}
