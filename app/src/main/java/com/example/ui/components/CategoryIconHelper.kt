package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

object CategoryIconHelper {
  fun getIcon(key: String): ImageVector {
    return when (key.lowercase()) {
      "restaurant", "food" -> Icons.Default.Restaurant
      "shopping_cart", "groceries" -> Icons.Default.ShoppingCart
      "directions_car", "transport" -> Icons.Default.DirectionsCar
      "receipt_long", "bills" -> Icons.Default.ReceiptLong
      "shopping_bag", "shopping" -> Icons.Default.ShoppingBag
      "medical_services", "health" -> Icons.Default.MedicalServices
      "school", "education" -> Icons.Default.School
      "movie", "entertainment" -> Icons.Default.Movie
      "home", "rent" -> Icons.Default.Home
      "work", "salary" -> Icons.Default.Work
      "store", "business" -> Icons.Default.Store
      "laptop", "freelance" -> Icons.Default.Laptop
      "trending_up", "investments" -> Icons.Default.TrendingUp
      "domain", "rental_income" -> Icons.Default.Domain
      "card_giftcard", "gift_bonus" -> Icons.Default.CardGiftcard
      "attach_money", "other_income" -> Icons.Default.AttachMoney
      "account_balance" -> Icons.Default.AccountBalance
      "more_horiz" -> Icons.Default.MoreHoriz
      else -> Icons.Default.Category
    }
  }
}
