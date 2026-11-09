package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.viewmodel.DateFilterType

@Composable
fun DateFilterBar(
  selectedFilter: DateFilterType,
  onSelectFilter: (DateFilterType) -> Unit,
  onOpenCustomRangeDialog: () -> Unit,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val filters = listOf(
    DateFilterType.THIS_MONTH to AppStrings.thisMonth(language),
    DateFilterType.TODAY to AppStrings.today(language),
    DateFilterType.THIS_WEEK to AppStrings.thisWeek(language),
    DateFilterType.LAST_MONTH to AppStrings.lastMonth(language),
    DateFilterType.ALL to AppStrings.allTime(language),
    DateFilterType.CUSTOM to AppStrings.customRange(language)
  )

  Row(
    modifier = modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState())
      .padding(horizontal = 4.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    filters.forEach { (type, label) ->
      val isSelected = selectedFilter == type
      FilterChip(
        selected = isSelected,
        onClick = {
          if (type == DateFilterType.CUSTOM) {
            onOpenCustomRangeDialog()
          } else {
            onSelectFilter(type)
          }
        },
        label = {
          Text(
            text = label,
            fontSize = 12.sp
          )
        },
        leadingIcon = if (type == DateFilterType.CUSTOM) {
          {
            Icon(
              imageVector = Icons.Default.DateRange,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
          }
        } else null,
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = MaterialTheme.colorScheme.primary,
          selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
          selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.testTag("filter_chip_${type.name.lowercase()}")
      )
    }
  }
}
