package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

@Database(entities = [TransactionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun transactionDao(): TransactionDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "expense_tracker_db"
        )
          .addCallback(DatabaseCallback(scope))
          .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabaseCallback(
      private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateInitialData(database.transactionDao())
          }
        }
      }
    }

    suspend fun populateInitialData(dao: TransactionDao) {
      if (dao.getCount() > 0) return

      val cal = Calendar.getInstance()
      val now = cal.timeInMillis

      // Day offsets helper
      fun timeAgo(daysAgo: Int, hour: Int = 12): Long {
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_YEAR, -daysAgo)
        c.set(Calendar.HOUR_OF_DAY, hour)
        c.set(Calendar.MINUTE, 30)
        return c.timeInMillis
      }

      val seeds = listOf(
        TransactionEntity(
          title = "Monthly Salary",
          amount = 120000.0,
          type = "INCOME",
          categoryId = "salary",
          categoryNameEn = "Salary",
          categoryNameUr = "تنخواہ",
          dateMillis = timeAgo(6, 9),
          paymentMethod = "Bank",
          note = "Company payroll deposit"
        ),
        TransactionEntity(
          title = "Grocery & Ration",
          amount = 14500.0,
          type = "EXPENSE",
          categoryId = "groceries",
          categoryNameEn = "Groceries",
          categoryNameUr = "راشن اور سودا",
          dateMillis = timeAgo(5, 14),
          paymentMethod = "Cash",
          note = "Monthly supermarket haul"
        ),
        TransactionEntity(
          title = "Electricity Bill",
          amount = 8200.0,
          type = "EXPENSE",
          categoryId = "bills",
          categoryNameEn = "Bills & Utilities",
          categoryNameUr = "بجلی، گیس و بل",
          dateMillis = timeAgo(4, 11),
          paymentMethod = "JazzCash",
          note = "Paid via app"
        ),
        TransactionEntity(
          title = "Car Fuel / Petrol",
          amount = 5500.0,
          type = "EXPENSE",
          categoryId = "transport",
          categoryNameEn = "Transportation",
          categoryNameUr = "ٹرانسپورٹ و پیٹرول",
          dateMillis = timeAgo(3, 17),
          paymentMethod = "Card",
          note = "Full tank"
        ),
        TransactionEntity(
          title = "Freelance UI Project",
          amount = 35000.0,
          type = "INCOME",
          categoryId = "freelance",
          categoryNameEn = "Freelance",
          categoryNameUr = "فری لانس کام",
          dateMillis = timeAgo(2, 16),
          paymentMethod = "Bank",
          note = "Client milestone payment"
        ),
        TransactionEntity(
          title = "Dinner with Family",
          amount = 4800.0,
          type = "EXPENSE",
          categoryId = "food",
          categoryNameEn = "Food & Dining",
          categoryNameUr = "کھانا پینا",
          dateMillis = timeAgo(1, 20),
          paymentMethod = "Card",
          note = "Weekend dinner"
        ),
        TransactionEntity(
          title = "Pharmacy & Vitamins",
          amount = 2300.0,
          type = "EXPENSE",
          categoryId = "health",
          categoryNameEn = "Health & Medical",
          categoryNameUr = "صحت و ادویات",
          dateMillis = now - 3600_000 * 4,
          paymentMethod = "Cash",
          note = "Monthly prescription"
        ),
        TransactionEntity(
          title = "Office Lunch & Tea",
          amount = 650.0,
          type = "EXPENSE",
          categoryId = "food",
          categoryNameEn = "Food & Dining",
          categoryNameUr = "کھانا پینا",
          dateMillis = now - 3600_000 * 2,
          paymentMethod = "Easypaisa",
          note = "Daily lunch"
        )
      )

      dao.insertAll(seeds)
    }
  }
}
