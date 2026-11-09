package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = PrimaryBlueDark,
  onPrimary = Color(0xFF0F172A),
  primaryContainer = PrimaryContainerBlueDark,
  onPrimaryContainer = Color(0xFFDBEAFE),
  secondary = IncomeGreen,
  onSecondary = Color.White,
  tertiary = ExpenseRed,
  onTertiary = Color.White,
  background = SurfaceDark,
  surface = SurfaceCardDark,
  onBackground = TextPrimaryDark,
  onSurface = TextPrimaryDark,
  surfaceVariant = Color(0xFF1E293B),
  onSurfaceVariant = TextSecondaryDark,
  outline = BorderDark
)

private val LightColorScheme = lightColorScheme(
  primary = PrimaryBlue,
  onPrimary = Color.White,
  primaryContainer = PrimaryContainerBlue,
  onPrimaryContainer = OnPrimaryContainerBlue,
  secondary = IncomeGreen,
  onSecondary = Color.White,
  tertiary = ExpenseRed,
  onTertiary = Color.White,
  background = SurfaceLight,
  surface = SurfaceCardLight,
  onBackground = TextPrimaryLight,
  onSurface = TextPrimaryLight,
  surfaceVariant = Color(0xFFF1F5F9),
  onSurfaceVariant = TextSecondaryLight,
  outline = BorderLight
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our tailored financial palette for consistent UX
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
